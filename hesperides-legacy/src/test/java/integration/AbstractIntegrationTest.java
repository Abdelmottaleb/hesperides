/*
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
 */
package integration;

import java.math.BigInteger;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.elasticsearch.common.Strings;
import org.junit.Before;
import org.junit.Ignore;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by emeric_martineau on 10/03/2017.
 */
@Ignore
public class AbstractIntegrationTest {
    /**
     * Hesperides HTTP connector.
     */
    protected HesperidesClient hesClient;

    /**
     * Redis connector.
     */
    protected JedisPool redisPool;

    /**
     * Redis cache
     */
    protected JedisPool redisCachePool;

    /**
     * Random prefix name for test.
     */
    protected String prefixName;

    /**
     * Disable SSL check
     */
    private static void disableSslVerification() {
        try
        {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
            };

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            HttpsURLConnection.setDefaultHostnameVerifier((hostname, sslSession) -> true);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }

    @Before
    public void setup() {
        disableSslVerification();

        final String username = System.getenv("HESPERIDES_USER");
        final String password = System.getenv("HESPERIDES_PASS");

        if (Strings.isNullOrEmpty(System.getenv("HESPERIDES_URL"))) {
            System.err.println("Please set environment variable HESPERIDES_URL");
            System.err.println("HESPERIDES_URL is url (http or https)");
        } else if (Strings.isNullOrEmpty(System.getenv("REDIS_URL"))) {
            System.err.println("Please set environment variable REDIS_URL");
            System.err.println("REDIS_URL is not really an url. But Ip:Port of redis that contain data");
        } else if (Strings.isNullOrEmpty(System.getenv("REDIS_CACHE_URL"))) {
            System.err.println("Please set environment variable REDIS_CACHE_URL");
            System.err.println("REDIS_CACHE_URL is not really an url. But Ip:Port of redis that contain data");
        }

        if (!(Strings.isNullOrEmpty(username) || Strings.isNullOrEmpty(password))) {
            this.hesClient = new HesperidesClient(System.getenv("HESPERIDES_URL"), username, password);
        } else {
            this.hesClient = new HesperidesClient(System.getenv("HESPERIDES_URL"));
        }

        String redisUrl = System.getenv("REDIS_URL");
        String[] redisHostPort = redisUrl.split(":");

        this.redisPool = new JedisPool(new JedisPoolConfig(), redisHostPort[0], Integer.valueOf(redisHostPort[1]), 30000);


        redisUrl = System.getenv("REDIS_CACHE_URL");
        redisHostPort = redisUrl.split(":");

        this.redisCachePool = new JedisPool(new JedisPoolConfig(), redisHostPort[0], Integer.valueOf(redisHostPort[1]), 30000);

        SecureRandom random = new SecureRandom();

        this.prefixName = new BigInteger(130, random).toString(32);
    }
}
