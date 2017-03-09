package io.cereebro.spring.cloud.autoconfigure.feign;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.cloud.netflix.feign.FeignClient;

import io.cereebro.core.Component;
import io.cereebro.core.ComponentType;
import io.cereebro.core.Dependency;
import io.cereebro.core.Relationship;
import io.cereebro.spring.annotation.AnnotationRelationshipDetector;

public class FeignClientAnnotationRelationshipDetector extends AnnotationRelationshipDetector<FeignClient> {

    public FeignClientAnnotationRelationshipDetector() {
        super(FeignClient.class);
    }

    @Override
    protected Set<Relationship> extractFromAnnotation(FeignClient annotation) {
        Dependency dependency = Dependency.on(Component.of(annotation.name(), ComponentType.WEB_APPLICATION));
        return new HashSet<>(Arrays.asList(dependency));
    }

    @Override
    protected Set<Relationship> extractFromAnnotationAttributes(Map<String, Object> annotationAttributes) {
        Dependency dependency = Dependency
                .on(Component.of(String.class.cast(annotationAttributes.get("name")), ComponentType.WEB_APPLICATION));
        return new HashSet<>(Arrays.asList(dependency));
    }

}
