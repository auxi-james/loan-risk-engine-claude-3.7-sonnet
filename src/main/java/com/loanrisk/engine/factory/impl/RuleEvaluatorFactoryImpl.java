package com.loanrisk.engine.factory.impl;

import com.loanrisk.engine.RuleEvaluator;
import com.loanrisk.engine.factory.RuleEvaluatorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of the RuleEvaluatorFactory interface
 */
@Component
public class RuleEvaluatorFactoryImpl implements RuleEvaluatorFactory {

    private final Map<String, RuleEvaluator> evaluators = new HashMap<>();

    @Autowired
    public RuleEvaluatorFactoryImpl(List<RuleEvaluator> evaluatorList) {
        // Register all evaluators by their operator type
        for (RuleEvaluator evaluator : evaluatorList) {
            evaluators.put(evaluator.getOperatorType(), evaluator);
        }
    }

    @Override
    public RuleEvaluator getEvaluator(String operator) {
        return evaluators.get(operator);
    }
}