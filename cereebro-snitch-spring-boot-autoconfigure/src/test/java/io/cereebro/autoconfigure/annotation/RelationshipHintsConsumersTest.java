package io.cereebro.autoconfigure.annotation;

import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import io.cereebro.autoconfigure.RelationshipHintsApplication;
import io.cereebro.core.Component;
import io.cereebro.core.Consumer;
import io.cereebro.core.Relationship;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RelationshipHintsApplication.class, properties = "spring.application.name=whatever")
@ActiveProfiles("consumers")
public class RelationshipHintsConsumersTest {

    private static final Relationship[] EXPECTED_RELATIONSHIPS = new Relationship[] {
            Consumer.by(Component.of("firstConsumer", "firstType")),
            Consumer.by(Component.of("secondConsumer", "secondType")) };

    @Autowired
    private RelationshipHintsAnnotationRelationshipDetector detector;

    @Test
    public void testTheSize() {
        Set<Relationship> rels = detector.detect();
        Assertions.assertThat(rels).isNotEmpty();
        Assertions.assertThat(rels).hasSize(2);
    }

    @Test
    public void testTheContents() {
        Set<Relationship> rels = detector.detect();
        for (Relationship r : rels) {
            Assertions.assertThat(r).isExactlyInstanceOf(Consumer.class);
        }
        Assertions.assertThat(rels).containsExactlyInAnyOrder(EXPECTED_RELATIONSHIPS);
    }

}
