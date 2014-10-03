
package com.asoroka.sidora.statistics.heuristics;

import static com.asoroka.sidora.datatype.DataType.Boolean;
import static com.asoroka.sidora.datatype.DataType.DateTime;
import static com.asoroka.sidora.datatype.DataType.Decimal;
import static com.asoroka.sidora.datatype.DataType.Geographic;
import static com.asoroka.sidora.datatype.DataType.Integer;
import static com.asoroka.sidora.datatype.DataType.NonNegativeInteger;
import static com.asoroka.sidora.datatype.DataType.PositiveInteger;
import static com.asoroka.sidora.datatype.DataType.String;
import static com.google.common.collect.ImmutableList.of;
import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.asoroka.sidora.datatype.DataType;

public abstract class CountAggregatingHeuristicTestFrame<T extends CountAggregatingHeuristic<T>> {

    protected CountAggregatingHeuristic<?> testHeuristic;

    protected static Map<DataType, List<String>> goodValues;

    protected static Map<DataType, List<String>> oneBadValue;
    {
        {
            // these are all good (parseable) values for the datatype to which they are assigned
            goodValues = new HashMap<>(DataType.values().length);
            goodValues.put(String, of("Jane", "John", "Sarah", "Simon"));
            goodValues.put(Decimal, of("0", "0.1", "-1", "34.345"));
            goodValues.put(Integer, of("0", "1", "-1 ", "34"));
            goodValues.put(NonNegativeInteger, of("0", "1", " 11", "34"));
            goodValues.put(PositiveInteger, of("354455", "13452432", "112345235 ", "34529534"));
            goodValues.put(Boolean, of("True", "False", "TruE", "FaLse"));
            goodValues.put(Geographic, of("38.03,-78.478889", " -78.478889,38.03", "1,0", "0,1"));
            goodValues.put(DateTime, of("1990-3-4", "2014-273", "2014-W40-2", "2014-W40",
                    "2014-09-30T18:58:45Z"));

            // copy the good values for reuse
            oneBadValue = new HashMap<>(DataType.values().length);
            for (final DataType type : DataType.values()) {
                oneBadValue.put(type, newArrayList(goodValues.get(type)));

            }
            // here we add one bad value to each list of good values
            oneBadValue.get(Decimal).add("0sd");
            oneBadValue.get(Integer).add("-1.3 ");
            oneBadValue.get(NonNegativeInteger).add("-11");
            oneBadValue.get(PositiveInteger).add("Q");
            oneBadValue.get(Boolean).add("Q");
            oneBadValue.get(Geographic).add("38.03");
            oneBadValue.get(DateTime).add("2014/24");
            // nothing cannot parse as a String
            oneBadValue.remove(String);
        }
    }

    protected abstract T newTestInstance();

    @Test
    public void testClone() {
        testHeuristic = newTestInstance();
        assertTrue(testHeuristic.clone().equals(testHeuristic));
    }

    @Test
    public void testEquals() {
        testHeuristic = newTestInstance();
        assertTrue(testHeuristic.equals(newTestInstance()));
        assertFalse(testHeuristic.equals(new Object()));
        @SuppressWarnings("rawtypes")
        final CountAggregatingHeuristic<?> nonEqualObject = new CountAggregatingHeuristic() {

            @Override
            protected boolean candidacy(final DataType d) {
                // NO OP
                return false;
            }

            @Override
            public CountAggregatingHeuristic clone() {
                // NO OP
                return null;
            }
        };
        nonEqualObject.addValue("test value");
        assertFalse(testHeuristic.equals(nonEqualObject));
    }
}
