package com.loanrisk.engine.factory;

import com.loanrisk.engine.RuleEvaluator;
import com.loanrisk.engine.evaluator.*;
import com.loanrisk.engine.factory.impl.RuleEvaluatorFactoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RuleEvaluatorFactoryTest {

    private RuleEvaluatorFactory factory;
    private EqualsEvaluator equalsEvaluator;
    private NotEqualsEvaluator notEqualsEvaluator;
    private GreaterThanEvaluator greaterThanEvaluator;
    private GreaterThanOrEqualEvaluator greaterThanOrEqualEvaluator;
    private LessThanEvaluator lessThanEvaluator;
    private LessThanOrEqualEvaluator lessThanOrEqualEvaluator;
    private ContainsEvaluator containsEvaluator;
    private NotContainsEvaluator notContainsEvaluator;
    private StartsWithEvaluator startsWithEvaluator;
    private EndsWithEvaluator endsWithEvaluator;

    @BeforeEach
    void setUp() {
        equalsEvaluator = new EqualsEvaluator();
        notEqualsEvaluator = new NotEqualsEvaluator();
        greaterThanEvaluator = new GreaterThanEvaluator();
        greaterThanOrEqualEvaluator = new GreaterThanOrEqualEvaluator();
        lessThanEvaluator = new LessThanEvaluator();
        lessThanOrEqualEvaluator = new LessThanOrEqualEvaluator();
        containsEvaluator = new ContainsEvaluator();
        notContainsEvaluator = new NotContainsEvaluator();
        startsWithEvaluator = new StartsWithEvaluator();
        endsWithEvaluator = new EndsWithEvaluator();

        List<RuleEvaluator> evaluators = Arrays.asList(
                equalsEvaluator,
                notEqualsEvaluator,
                greaterThanEvaluator,
                greaterThanOrEqualEvaluator,
                lessThanEvaluator,
                lessThanOrEqualEvaluator,
                containsEvaluator,
                notContainsEvaluator,
                startsWithEvaluator,
                endsWithEvaluator
        );

        factory = new RuleEvaluatorFactoryImpl(evaluators);
    }

    @Test
    void testGetEvaluator() {
        // Test getting each evaluator by operator type
        assertSame(equalsEvaluator, factory.getEvaluator("EQUALS"));
        assertSame(notEqualsEvaluator, factory.getEvaluator("NOT_EQUALS"));
        assertSame(greaterThanEvaluator, factory.getEvaluator("GREATER_THAN"));
        assertSame(greaterThanOrEqualEvaluator, factory.getEvaluator("GREATER_THAN_OR_EQUAL"));
        assertSame(lessThanEvaluator, factory.getEvaluator("LESS_THAN"));
        assertSame(lessThanOrEqualEvaluator, factory.getEvaluator("LESS_THAN_OR_EQUAL"));
        assertSame(containsEvaluator, factory.getEvaluator("CONTAINS"));
        assertSame(notContainsEvaluator, factory.getEvaluator("NOT_CONTAINS"));
        assertSame(startsWithEvaluator, factory.getEvaluator("STARTS_WITH"));
        assertSame(endsWithEvaluator, factory.getEvaluator("ENDS_WITH"));
        
        // Test with unknown operator
        assertNull(factory.getEvaluator("UNKNOWN_OPERATOR"));
    }
}