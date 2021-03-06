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
package io.cereebro.server.discovery;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.cloud.client.discovery.DiscoveryClient;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.cereebro.core.Snitch;
import io.cereebro.core.SnitchRegistry;

/**
 * Snitch registry based on a service registry like Eureka or Consul. Each
 * service instance holds Cereebro metadata populated on the client side.
 * 
 * @author lucwarrot
 * @author michaeltecourt
 */
public class DiscoveryClientSnitchRegistry implements SnitchRegistry {

    private final DiscoveryClient discoveryClient;
    private final ObjectMapper objectMapper;

    /**
     * Snitch registry based on a service registry like Eureka or Consul. Each
     * service instance holds Cereebro metadata populated on the client side.
     * 
     * @param discoveryClient
     *            Service registry client.
     * @param mapper
     *            JSON object mapper.
     */
    public DiscoveryClientSnitchRegistry(DiscoveryClient discoveryClient, ObjectMapper mapper) {
        this.discoveryClient = Objects.requireNonNull(discoveryClient, "Eureka discovery client required");
        this.objectMapper = Objects.requireNonNull(mapper, "JSON object mapper required");
    }

    @Override
    public List<Snitch> getAll() {
        // @formatter:off
        return discoveryClient.getServices().stream()
            .map(discoveryClient::getInstances)
            .flatMap(List::stream)
            .map(instance -> new ServiceInstanceSnitch(objectMapper, instance))
            .collect(Collectors.toList());
        // @formatter:on
    }

}
