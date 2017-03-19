/*
 * Copyright © 2009 the original authors (http://cereebro.io)
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
package io.cereebro.core;

import java.util.HashSet;
import java.util.Set;

public class TestHelper {

    public static Component componentA() {
        return Component.of("a", "a");
    }

    public static Component componentB() {
        return Component.of("b", "b");
    }

    public static Component componentC() {
        return Component.of("c", "c");
    }

    public static Dependency dependencyOnB() {
        return Dependency.on(componentB());
    }

    public static Consumer consumedByC() {
        return Consumer.by(componentC());
    }

    public static Set<Relationship> relationshipSetOfDependencyBAndConsumerC() {
        Set<Relationship> deps = new HashSet<>();
        deps.add(dependencyOnB());
        deps.add(consumedByC());
        return deps;
    }
}
