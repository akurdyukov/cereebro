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
package io.cereebro.spring.cloud.autoconfigure.discovery;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.UrlResource;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.cereebro.core.Snitch;
import io.cereebro.core.SnitchingException;
import io.cereebro.core.SystemFragment;

/**
 * {@link ServiceInstanceSnitch} unit tests.
 * 
 * @author michaeltecourt
 */
@RunWith(MockitoJUnitRunner.class)
public class ServiceInstanceSnitchTest {

    @Mock
    ObjectMapper objectMapperMock;

    @Mock
    ServiceInstance serviceInstanceMock;

    Map<String, String> metadata;

    @Before
    public void setUp() {
        metadata = new HashMap<>();
        Mockito.when(serviceInstanceMock.getMetadata()).thenReturn(metadata);
    }

    @Test(expected = NullPointerException.class)
    public void objectMapperRequired() {
        new ServiceInstanceSnitch(null, serviceInstanceMock);
    }

    @Test(expected = NullPointerException.class)
    public void serviceInstanceRequired() {
        new ServiceInstanceSnitch(objectMapperMock, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void serviceInstanceMetadataRequired() {
        new ServiceInstanceSnitch(objectMapperMock, serviceInstanceMock);
    }

    @Test
    public void hasCereebroMetadata() {
        metadata.put(CereebroMetadata.KEY_SNITCH_URI, "http://cereebro.io");
        Assertions.assertThat(ServiceInstanceSnitch.hasCereebroMetadata(serviceInstanceMock)).isTrue();
    }

    @Test
    public void snitchShouldReadSystemFragmentJson() throws IOException {
        String uri = "http://cereebro.io";
        String json = "{}";
        metadata.put(CereebroMetadata.KEY_SNITCH_URI, uri);
        metadata.put(CereebroMetadata.KEY_SNITCH_SYSTEM_FRAGMENT_JSON, json);
        Mockito.when(objectMapperMock.readValue(json, SystemFragment.class)).thenReturn(SystemFragment.empty());
        ServiceInstanceSnitch snitch = new ServiceInstanceSnitch(objectMapperMock, serviceInstanceMock);
        Assertions.assertThat(snitch.getUri()).isEqualTo(URI.create(uri));
        Assertions.assertThat(snitch.snitch()).isEqualTo(SystemFragment.empty());
    }

    /**
     * An existing classpath file is used so the underyling UrlResource creation
     * works. Note that using {@literal classpath:snitch-test.json} would not
     * work with {@link UrlResource}. The file does not need to contain valid
     * JSON because the parsing is mocked.
     * 
     * @throws IOException
     */
    @Test
    @SuppressWarnings("unchecked")
    public void snitchWithoutFragmentJsonShouldConsumeResourceByUri() throws IOException {
        String uri = "file://" + new ClassPathResource("snitch-test.json").getFile().getAbsolutePath();
        metadata.put(CereebroMetadata.KEY_SNITCH_URI, uri);
        Mockito.when(objectMapperMock.readValue(Mockito.any(InputStream.class), Mockito.any(Class.class)))
                .thenReturn(SystemFragment.empty());
        Snitch snitch = ServiceInstanceSnitch.of(objectMapperMock, serviceInstanceMock);
        Assertions.assertThat(snitch.getUri()).isEqualTo(URI.create(uri));
        Assertions.assertThat(snitch.snitch()).isEqualTo(SystemFragment.empty());
    }

    @Test
    public void snitchErrorShouldThrowSnitchingException() throws IOException {
        String uri = "http://cereebro.io";
        String json = "{ \"error\" : true }";
        metadata.put(CereebroMetadata.KEY_SNITCH_URI, uri);
        metadata.put(CereebroMetadata.KEY_SNITCH_SYSTEM_FRAGMENT_JSON, json);
        Mockito.when(objectMapperMock.readValue(json, SystemFragment.class)).thenThrow(new IOException("unit test"));
        ServiceInstanceSnitch snitch = new ServiceInstanceSnitch(objectMapperMock, serviceInstanceMock);
        try {
            snitch.snitch();
            Assertions.fail("Expected IOException");
        } catch (SnitchingException e) {
            Assertions.assertThat(e.getSnitchUri()).isEqualTo(URI.create(uri));
        }
    }

}
