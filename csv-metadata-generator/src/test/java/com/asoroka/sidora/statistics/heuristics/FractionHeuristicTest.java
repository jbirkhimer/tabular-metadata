
package com.asoroka.sidora.statistics.heuristics;

import static org.junit.Assert.assertEquals;
import static org.slf4j.LoggerFactory.getLogger;

import org.junit.Test;
import org.slf4j.Logger;

import com.asoroka.sidora.datatype.DataType;

public class FractionHeuristicTest extends CountAggregatingHeuristicTestFrame<FractionHeuristic> {

    private static final Logger log = getLogger(FractionHeuristicTest.class);

    @Test
    public void testActionWithGoodValues() {

        log.trace("testActionWithGoodValues()...");
        for (final DataType testType : DataType.values()) {
            testHeuristic = new FractionHeuristic(0.2);
            for (final String testValue : goodValues.get(testType)) {
                testHeuristic.addValue(testValue);
            }
            assertEquals("Didn't get the correct type for datatype " + testType + "!", testType, testHeuristic
                    .mostLikelyType());
        }
    }

    @Test
    public void testActionWithOneBadValue() {
        log.trace("testActionWithOneBadValue()...");
        for (final DataType testType : oneBadValue.keySet()) {
            testHeuristic = new FractionHeuristic(0.2);
            for (final String testValue : oneBadValue.get(testType)) {
                testHeuristic.addValue(testValue);
            }
            assertEquals("Failed to admit datatype but shoud have!", testType, testHeuristic.mostLikelyType());
        }
    }

    @Override
    protected FractionHeuristic newTestInstance() {
        return new FractionHeuristic(0.2);
    }

}
