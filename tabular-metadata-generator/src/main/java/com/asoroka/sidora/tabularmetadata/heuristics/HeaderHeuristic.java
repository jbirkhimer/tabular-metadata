
package com.asoroka.sidora.tabularmetadata.heuristics;

import static com.asoroka.sidora.tabularmetadata.datatype.ValueType.parseableAs;
import static com.google.common.collect.Iterables.all;
import static java.util.Collections.singleton;

import javax.inject.Provider;

import com.asoroka.sidora.tabularmetadata.datatype.ValueType;
import com.google.common.base.Predicate;

/**
 * Tests a row of fields to see if they represent a header. <br/>
 * TODO allow this type to access more information than a single row of fields with which to make its determination
 * 
 * @author ajs6f
 */
public interface HeaderHeuristic<T extends HeaderHeuristic<T>> extends Predicate<Iterable<String>>, Provider<T> {

    /**
     * As the name implies, a {@link HeaderHeuristic} that treats each field in the row the same and applies a single
     * test to each. Only if every field value passes the field-test does the row pass this test.
     * 
     * @author ajs6f
     * @param <T>
     */
    public static abstract class TreatsEachFieldAlikeHeaderHeuristic<T extends TreatsEachFieldAlikeHeaderHeuristic<T>>
            implements HeaderHeuristic<T> {

        @Override
        public boolean apply(final Iterable<String> inputRow) {
            return all(inputRow, fieldTest());
        }

        protected abstract Predicate<? super String> fieldTest();

    }

    /**
     * This is a very simple test of whether a line is a header line. Only any line in which each field parses only as
     * a {@link ValueType#String} will be accepted.
     */
    public static class Default extends TreatsEachFieldAlikeHeaderHeuristic<Default> {

        @Override
        protected Predicate<? super String> fieldTest() {
            return isOnlyString;
        }

        private static final Predicate<String> isOnlyString = new Predicate<String>() {

            @Override
            public boolean apply(final String value) {
                return parseableAs(value).equals(singleton(ValueType.String));
            }
        };

        @Override
        public Default get() {
            return this;
        }
    }
}
