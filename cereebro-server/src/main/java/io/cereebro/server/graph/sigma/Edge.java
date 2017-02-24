package io.cereebro.server.graph.sigma;

import javax.validation.constraints.NotNull;

import io.cereebro.core.Component;
import io.cereebro.core.Consumer;
import io.cereebro.core.Dependency;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "create")
public class Edge {

    public static final String TYPE_ARROW = "arrow";

    @NotNull
    private final String id;
    @NotNull
    private final String source;
    @NotNull
    private final String target;

    private final String type;

    public static Edge to(Component component, Dependency dependency) {
        String from = component.asString();
        String to = dependency.getComponent().asString();
        return Edge.create(from + "-to-" + to, from, to, TYPE_ARROW);
    }

    public static Edge from(Component component, Consumer consumer) {
        String to = component.asString();
        String from = consumer.getComponent().asString();
        return Edge.create(from + "-to-" + to, from, to, TYPE_ARROW);
    }

}
