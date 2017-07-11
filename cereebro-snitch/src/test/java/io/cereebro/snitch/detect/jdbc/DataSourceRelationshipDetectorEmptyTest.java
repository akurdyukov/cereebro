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

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import io.cereebro.snitch.detect.jdbc.DataSourceRelationshipDetectorEmptyTest.NoDataSourceTestApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = NoDataSourceTestApplication.class)
@ActiveProfiles("nodb")
public class DataSourceRelationshipDetectorEmptyTest {

    @Autowired
    private DataSourceRelationshipDetector detector;

    @Test
    public void dataSourceRelationshipWithoutDataSourceAvailable() {
        Assertions.assertThat(detector.detect()).isEmpty();
    }

    @SpringBootApplication
    static class NoDataSourceTestApplication {

    }

}
