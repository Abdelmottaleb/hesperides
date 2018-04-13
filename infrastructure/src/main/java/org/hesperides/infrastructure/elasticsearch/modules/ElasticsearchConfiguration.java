package org.hesperides.infrastructure.elasticsearch.modules;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@Configuration
@EnableTransactionManagement
@EnableElasticsearchRepositories(basePackages = "org.hesperides.infrastructure.elasticsearch")
@Profile("elasticsearch")
@Import({ElasticsearchAutoConfiguration.class, ElasticsearchDataAutoConfiguration.class})
public class ElasticsearchConfiguration {
}
