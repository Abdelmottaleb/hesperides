/*
 *
 *  * This file is part of the Hesperides distribution.
 *  * (https://github.com/voyages-sncf-technologies/hesperides)
 *  * Copyright (c) 2016 VSCT.
 *  *
 *  * Hesperides is free software: you can redistribute it and/or modify
 *  * it under the terms of the GNU General Public License as
 *  * published by the Free Software Foundation, version 3.
 *  *
 *  * Hesperides is distributed in the hope that it will be useful, but
 *  * WITHOUT ANY WARRANTY; without even the implied warranty of
 *  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *  * General Public License for more details.
 *  *
 *  * You should have received a copy of the GNU General Public License
 *  * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 *
 *
 */

package com.vsct.dt.hesperides.feedback;

import com.bazaarvoice.dropwizard.assets.AssetsConfiguration;
import com.google.common.eventbus.EventBus;
import com.vsct.dt.hesperides.MainApplication;
import com.vsct.dt.hesperides.exception.runtime.HesperidesException;
import com.vsct.dt.hesperides.feedback.jsonObject.FeedbackJson;
import com.vsct.dt.hesperides.security.model.User;
import com.vsct.dt.hesperides.storage.EventStore;
import com.vsct.dt.hesperides.storage.SingleThreadAggregate;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import javax.net.ssl.SSLContext;
import java.awt.image.BufferedImage;
import java.io.*;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by stephane_fret on 07/03/2017.
 */
public class FeedbacksAggregate extends SingleThreadAggregate implements Feedbacks {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainApplication.class);
    FeedbackConfiguration feedbackConfiguration;
    AssetsConfiguration assetsConfiguration;

    /**
     * The constructor of the aggregator
     * @param feedbackConfiguration
     * @param assetsConfiguration
     */
    public FeedbacksAggregate (final FeedbackConfiguration feedbackConfiguration, final AssetsConfiguration assetsConfiguration, final EventBus eventBus, final EventStore eventStore){
        super(new String(), eventBus, eventStore);
        this.feedbackConfiguration = feedbackConfiguration;
        this.assetsConfiguration = assetsConfiguration;
    }

    @Override
    protected String getStreamPrefix() {
        return "";
    }

    @Override
    public void sendFeedbackToHipchat(final User user,
                                      final FeedbackJson template) {

        LOGGER.debug("Feedback from "
                +user.getUsername()
                +" to hipchat room\n"
                +template.toJsonString());

        String imageData = template.getFeedback().getImg().split(",")[1];

        String imageName =
                "feedback_" + template.getFeedback().getTimestamp() + ".png";

        try {
            Iterator<Map.Entry<String, String>> itOverride = assetsConfiguration.getOverrides().iterator();

            String applicationPath;

            if (itOverride.hasNext()) {
                applicationPath = itOverride.next().getValue();
            } else {
                throw new HesperidesException("Could  not create Feedback: asserts configuration not valid");
            }

            StringBuilder serverPathImageName = new StringBuilder()
                    .append(applicationPath)
                    .append("/")
                    .append(feedbackConfiguration.getImagePathStorage())
                    .append("/")
                    .append(imageName);

            LOGGER.debug("Server path : "+serverPathImageName.toString());

            StringBuilder urlDownloadImage = new StringBuilder()
                    .append(template.getFeedback().getUrl().split("/")[0])
                    .append("/")
                    .append("/")
                    .append(template.getFeedback().getUrl().split("/")[2])
                    .append("/")
                    .append(feedbackConfiguration.getImagePathStorage())
                    .append("/")
                    .append(imageName);

            LOGGER.debug("Download URL : "+urlDownloadImage.toString());

            StringBuilder hipchatMessage = new StringBuilder()
                    .append("<p>When access to <a href='")
                    .append(template.getFeedback().getUrl())
                    .append("'>")
                    .append(template.getFeedback().getUrl())
                    .append("</a></p>");

            // Encapsulate each line ended by \n with <p><\p>
            List<String> wordList = Arrays.asList(template.getFeedback().getNote().split("\n"));
            Iterator itWordList = wordList.iterator();

            while (itWordList.hasNext()) {
                hipchatMessage.append("<p>")
                        .append(itWordList.next())
                        .append(("</p>"));
            }

            hipchatMessage.append("<a href='")
                    .append(urlDownloadImage)
                    .append("'>Download screenshot</a>");

            writeImage(serverPathImageName.toString(), imageData);

            CloseableHttpClient httpClient = getHttpClient();

            StringBuilder stringBody = new StringBuilder();
            stringBody.append("{\"from\": \"")
                    .append(user.getUsername())
                    .append("\",\"color\": \"")
                    .append("green")
                    .append("\",\"message\": \"")
                    .append(hipchatMessage.toString())
                    .append("\",\"notify\": \"")
                    .append("true")
                    .append("\",\"message_format\": \"")
                    .append("html")
                    .append("\"}");

            StringBuilder urlVersion = new StringBuilder("https://");
            urlVersion.append(feedbackConfiguration.getHipchatSubdomain())
                    .append(".hipchat.com/v2/room/")
                    .append(feedbackConfiguration.getHipchatId())
                    .append("/notification?auth_token=")
                    .append(feedbackConfiguration.getHipchatToken());

            HttpPost postRequest = new HttpPost(urlVersion.toString());

            StringEntity input = new StringEntity(stringBody.toString());
            input.setContentType("application/json");
            postRequest.setEntity(input);

            // LOG
            LOGGER.debug("------------- Post send Hipchat request ------------------------------------------");
            LOGGER.debug(postRequest.toString());
            LOGGER.debug("------------- Post send Hipchat content request ---------------------------------");
            LOGGER.debug(getStringContent(postRequest.getEntity()));

            HttpResponse postResponse = httpClient.execute(postRequest);

            // LOG
            LOGGER.debug("------------- Post send Hipchat response ------------------------------------------");
            LOGGER.debug(postResponse.toString());

            if (postResponse.getStatusLine().getStatusCode() != 204) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + postResponse.getStatusLine().getStatusCode());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void writeImage(String pathImageName, String imageString) {

        // create a buffered image
        BufferedImage bufferedImage;
        byte[] imageByte;

        try {
            BASE64Decoder decoder = new BASE64Decoder();
            imageByte = decoder.decodeBuffer(imageString);
            ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
            bufferedImage = ImageIO.read(bis);
            bis.close();

            File imageDirectory = new File(pathImageName).getParentFile() ;
            if (!imageDirectory.exists()) {
                imageDirectory.mkdirs();
            }

            // write the image to a file
            FileOutputStream outputfile =
                    new FileOutputStream(pathImageName, false);
            ImageIO.write(bufferedImage, "png", outputfile);

            outputfile.close();

        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public static CloseableHttpClient getHttpClient() {

        CloseableHttpClient httpClient;

        try {
            SSLContext sslContext = new SSLContextBuilder()
                    .loadTrustMaterial(null, (certificate, authType) -> true).build();

            httpClient = HttpClients.custom()
                    .setSSLContext(sslContext)
                    .setSSLHostnameVerifier(new NoopHostnameVerifier())
                    .build();

        } catch (KeyManagementException e) {
            throw new RuntimeException("KeyManagementException :"+e.toString());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("NoSuchAlgorithmException :"+e.toString());
        } catch (KeyStoreException e) {
            throw new RuntimeException("KeyStoreException :"+e.toString());
        }
        return httpClient;
    }

    public static String getStringContent(HttpEntity httpEntity) {

        BufferedReader bufferedReader;
        StringBuilder content = new StringBuilder();

        try {
            bufferedReader = new BufferedReader(
                    new InputStreamReader((httpEntity.getContent()) , "UTF-8"));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line);
            }
            IOUtils.closeQuietly(bufferedReader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }

}

