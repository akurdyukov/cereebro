package io.cereebro.core;

import java.util.Set;

/**
 * Registry of available {@link Snitch}es.
 * 
 * @author michaeltecourt
 */
public interface SnitchRegistry {

    /**
     * All the snitches declared in the registry.
     * 
     * @return a Set of Snitch objects.
     */
    Set<Snitch> getAll();

}
