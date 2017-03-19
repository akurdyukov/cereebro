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
package io.cereebro.spring.annotation;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import io.cereebro.core.Component;
import io.cereebro.core.Dependency;
import io.cereebro.core.Relationship;
import io.cereebro.core.RelationshipDetector;
import io.cereebro.core.annotation.DependencyHint;

/**
 * Implementation of {@link RelationshipDetector} used to detect all
 * {@link DependencyHint} declared in the {@link ApplicationContext}. The class
 * retrieves all {@link Bean} annotated with {@link DependencyHint}.
 * {@link Relationship}
 * 
 * @author lwarrot
 *
 */
public class DependencyHintAnnotationRelationshipDetector extends AnnotationRelationshipDetector<DependencyHint> {

    public DependencyHintAnnotationRelationshipDetector() {
        super(DependencyHint.class);
    }

    @Override
    protected Set<Relationship> extractFromAnnotation(DependencyHint annotation) {
        Dependency dependency = Dependency.on(Component.of(annotation.name(), annotation.type()));
        return new HashSet<>(Arrays.asList(dependency));
    }

    @Override
    protected Set<Relationship> extractFromAnnotationAttributes(Map<String, Object> annotationAttributes) {
        String name = String.class.cast(annotationAttributes.get("name"));
        String type = String.class.cast(annotationAttributes.get("type"));
        Dependency dependency = Dependency.on(Component.of(name, type));
        return new HashSet<>(Arrays.asList(dependency));
    }

}
