/*
 * Copyright © 2017 the original authors (http://cereebro.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.cereebro.snitch.detect.jdbc;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.cereebro.snitch.detect.ConditionalOnEnabledDetector;
import io.cereebro.snitch.detect.Detectors;

@Configuration
@ConditionalOnClass(DataSource.class)
@ConditionalOnEnabledDetector(DataSourceRelationshipDetectorAutoConfiguration.PROP)
public class DataSourceRelationshipDetectorAutoConfiguration {

    static final String PROP = "jdbc";

    @Autowired(required = false)
    private List<DataSource> dataSources;

    @Bean
    @ConfigurationProperties(prefix = Detectors.PREFIX + "." + PROP)
    public DataSourceRelationshipDetector dataSourceRelationshipDetector() {
        return new DataSourceRelationshipDetector(dataSources);
    }

}
