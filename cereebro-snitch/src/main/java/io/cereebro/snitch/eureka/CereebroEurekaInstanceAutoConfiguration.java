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
package io.cereebro.snitch.eureka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.cloud.netflix.eureka.CloudEurekaInstanceConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.cereebro.core.Snitch;

/**
 * Adds Cereebro metadata when registering an Eureka service (client
 * application).
 * 
 * @author michaeltecourt
 */
@Configuration
@ConditionalOnClass(CloudEurekaInstanceConfig.class)
@ConditionalOnBean(CloudEurekaInstanceConfig.class)
public class CereebroEurekaInstanceAutoConfiguration {

    @Autowired
    private Environment env;

    @Bean
    @ConditionalOnBean(Snitch.class)
    public EurekaMetadataPopulator eurekaMetadataPopulator(Snitch snitch, CloudEurekaInstanceConfig config,
            ObjectMapper mapper) {
        RelaxedPropertyResolver relaxedPropertyResolver = new RelaxedPropertyResolver(env,
                "cereebro.eureka.instance.snitch.");
        EurekaInstanceSnitchProperties props = new EurekaInstanceSnitchProperties();
        props.setUrl(relaxedPropertyResolver.getProperty("url"));
        props.setUrlPath(relaxedPropertyResolver.getProperty("urlPath"));
        EurekaMetadataPopulator metadataPopulator = new EurekaMetadataPopulator(snitch, config, props, mapper);
        metadataPopulator.populate();
        return metadataPopulator;
    }

}
