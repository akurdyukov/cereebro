package io.cereebro.spring.boot.autoconfigure.actuate;

import java.net.URI;

import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import io.cereebro.core.ComponentType;
import io.cereebro.spring.boot.autoconfigure.CereebroAutoConfiguration;
import io.cereebro.spring.boot.autoconfigure.actuate.SnitchEndpointSpringApplicationNameTest.SnitchEndpointSpringApplicationNameTestApplication;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { SnitchEndpointSpringApplicationNameTestApplication.class, CereebroAutoConfiguration.class,
        CereebroWebMvcEndpointConfiguration.class }, webEnvironment = WebEnvironment.RANDOM_PORT, value = {
                "spring.application.name=springappname" })
public class SnitchEndpointSpringApplicationNameTest {

    @Value("http://localhost:${local.server.port}/cereebro/snitch")
    URI snitchURI;

    @Autowired
    private CereebroSnitchMvcEndpoint endpoint;

    /**
     * Verify that the component name returned by the snitch endpoint is the
     * value of {@literal spring.application.name}
     */
    @Test
    public void applicationComponentNameShouldBeSpringApplicationName() {
        // @formatter:off
        RestAssured
            .given()
            .when()
                .get(snitchURI)
            .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("componentRelationships[0].component.name", Matchers.is("springappname"))
                .body("componentRelationships[0].component.type", Matchers.is(ComponentType.HTTP_APPLICATION));
        // @formatter:on
    }

    @Test
    public void endpointShouldBeSensitiveByDefault() {
        Assertions.assertThat(endpoint.isSensitive()).isTrue();
    }

    @Test
    public void endpointPathShouldBeEqualToDefaultPath() {
        Assertions.assertThat(endpoint.getPath()).isEqualTo(CereebroSnitchMvcEndpoint.DEFAULT_PATH);
    }

    @Test
    public void endpointShouldBeEnabledByDefault() {
        Assertions.assertThat(endpoint.isEnabled()).isTrue();
    }

    @SpringBootApplication
    static class SnitchEndpointSpringApplicationNameTestApplication {

    }

}
