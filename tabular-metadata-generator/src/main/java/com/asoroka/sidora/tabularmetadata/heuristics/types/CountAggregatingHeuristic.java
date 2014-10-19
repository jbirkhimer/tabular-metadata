/**
 * TEMPORARY LICENSE HEADER STANDIN
 * REPLACE WITH APPROPRIATE SIDORA LICENSE
 */

package com.asoroka.sidora.tabularmetadata.heuristics.types;

import static com.asoroka.sidora.tabularmetadata.datatype.DataType.parseableAs;
import static com.google.common.base.Functions.constant;
import static com.google.common.collect.Maps.toMap;
import static java.util.Objects.hash;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;

import org.slf4j.Logger;

import com.asoroka.sidora.tabularmetadata.datatype.DataType;
import com.asoroka.sidora.tabularmetadata.heuristics.Heuristic;

/**
 * A {@link Heuristic} that aggregates candidate type appearance information for its field.
 * 
 * @author ajs6f
 * @param <SelfType>
 */
public abstract class CountAggregatingHeuristic<SelfType extends CountAggregatingHeuristic<SelfType>> extends
        ValueCountingHeuristic<SelfType> {

    /**
     * In this {@link Map}, we aggregate counts of parseable values for each datatype.
     */
    protected EnumMap<DataType, Integer> typeCounts;

    private static final Logger log = getLogger(CountAggregatingHeuristic.class);

    /**
     * Initialize counts for each datatype.
     */
    @Override
    public void reset() {
        this.typeCounts = new EnumMap<>(DataType.class);
        final Map<DataType, Integer> zeroes = toMap(DataType.valuesSet(), constant(0));
        this.typeCounts.putAll(zeroes);
    }

    @Override
    public void addValue(final String value) {
        super.addValue(value);
        incrementCounts(parseableAs(value));
    }

    private void incrementCounts(final Collection<DataType> types) {
        for (final DataType type : types) {
            typeCounts.put(type, typeCounts.get(type) + 1);
        }
    }

    @Override
    public int hashCode() {
        return super.hashCode() + 2 * hash(typeCounts);
    }
}