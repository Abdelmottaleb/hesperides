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
 */

package com.vsct.dt.hesperides.indexation.command;

import com.vsct.dt.hesperides.indexation.ElasticSearchClient;
import com.vsct.dt.hesperides.indexation.ElasticSearchIndexationCommand;
import com.vsct.dt.hesperides.indexation.mapper.ElasticSearchMappers;
import com.vsct.dt.hesperides.indexation.model.TemplateIndexation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by william_montaz on 09/12/2014.
 */
public final class DeleteIndexedTemplateCommand implements ElasticSearchIndexationCommand<TemplateIndexation> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteIndexedTemplateCommand.class);

    private final String namespace;
    private final String name;

    public DeleteIndexedTemplateCommand(final String namespace, final String name) {
        this.namespace = namespace;
        this.name = name;
    }

    @Override
    public Void index(final ElasticSearchClient elasticSearchClient) {
        /* Not very efficient and safe way to get the id */
        TemplateIndexation template = new TemplateIndexation(namespace, name, null, null);
        String url = String.format("/templates/%1$s", template.getId());
        elasticSearchClient.withResponseReader(ElasticSearchMappers.ES_ENTITY_TEMPLATE_READER).delete(url);

        LOGGER.info("Successfully deleted template {} {}", namespace, name);
        return null;
    }
}
