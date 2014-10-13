
package com.asoroka.sidora.tabularmetadata.heuristics;

import static com.asoroka.sidora.tabularmetadata.datatype.ValueType.sortByHierarchy;
import static com.google.common.collect.Sets.filter;
import static java.util.Objects.hash;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.Set;
import java.util.SortedSet;

import org.slf4j.Logger;

import com.asoroka.sidora.tabularmetadata.datatype.ValueType;
import com.google.common.base.Predicate;

/**
 * A {@link DataTypeHeuristic} that uses some test that maps types to boolean acceptance values. The test is created
 * by overriding {@link #candidacy(ValueType)}. Types are passed or rejected without any particular order other than
 * specificity of type within the hierarchy.
 * 
 * @author ajs6f
 * @param <T>
 */
public abstract class PerTypeHeuristic<T extends PerTypeHeuristic<T>> extends ValueCountingHeuristic<T> {

    private static final Logger log = getLogger(PerTypeHeuristic.class);

    @Override
    public SortedSet<ValueType> typesAsLikely() {
        // develop a set of candidate types in a manner specific to the subclass
        final Set<ValueType> possibleTypes = filter(ValueType.valuesSet(), candidacyPredicate);
        // order by hierarchy
        final SortedSet<ValueType> sortedCandidates = sortByHierarchy(possibleTypes);
        log.trace("Found candidate types: {}", sortedCandidates);

        return sortedCandidates;
    }

    private Predicate<ValueType> candidacyPredicate = new Predicate<ValueType>() {

        @Override
        public boolean apply(final ValueType type) {
            return candidacy(type);
        }
    };

    /**
     * Subclasses must override this method with an algorithm that uses the gathered statistics (and possibly other
     * information) to make a determination about the most likely type of the proffered values.
     * 
     * @return Whether this type should be considered as a candidate for selection.
     */
    protected abstract boolean candidacy(final ValueType type);

    @Override
    public int hashCode() {
        return super.hashCode() + 2 * hash(totalNumValues());
    }
}
