package ru.star.bank.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.star.bank.dto.*;
import ru.star.bank.entity.QueryConditionEntity;
import ru.star.bank.entity.RuleEntity;
import ru.star.bank.repository.*;
import ru.star.bank.rule.RecommendationRuleSet;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    private final List<RecommendationRuleSet> staticRules;
    private final RuleRepository ruleRepository;
    private final RecommendationRepository recommendationRepository;

    public RecommendationService(List<RecommendationRuleSet> staticRules,
                                 RuleRepository ruleRepository,
                                 RecommendationRepository recommendationRepository) {
        this.staticRules = staticRules;
        this.ruleRepository = ruleRepository;
        this.recommendationRepository = recommendationRepository;
    }

    public RecommendationResponseDto getRecommendations(UUID userId) {
        List<RecommendationDto> result = new ArrayList<>();

        for (RecommendationRuleSet rule : staticRules) {
            Optional<RecommendationDto> recommendation = rule.check(userId);
            if (recommendation.isPresent()) {
                result.add(recommendation.get());
            }
        }

        List<RuleEntity> dynamicRules = ruleRepository.findAll();
        for (RuleEntity rule : dynamicRules) {
            boolean matches = true;
            for (QueryConditionEntity condition : rule.getRule()) {
                if (!evalCondition(userId, condition)) {
                    matches = false;
                    break;
                }
            }
            if (matches) {
                result.add(new RecommendationDto(rule.getProductId(), rule.getProductName(), rule.getProductText()));
            }
        }
        return new RecommendationResponseDto(userId, result);
    }

    private boolean evalCondition(UUID userId, QueryConditionEntity condition) {
        boolean res = false;
        List<String> args = condition.getArguments();

        try {
            switch (condition.getQuery()) {
                case "USER_OF" -> res = recommendationRepository.hasProductType(userId, ProductType.valueOf(args.get(0)));
                case "ACTIVE_USER_OF" -> res = recommendationRepository.getTransactionCount(userId, ProductType.valueOf(args.get(0))) >= 5;
                case "TRANSACTION_SUM_COMPARE" -> {
                    double sum = recommendationRepository.getTransactionSum(userId, ProductType.valueOf(args.get(0)), TransactionType.valueOf(args.get(1)));
                    res = compareValues(sum, Integer.parseInt(args.get(3)), args.get(2));
                }
                case "TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW" -> {
                    double deposit = recommendationRepository.getTransactionSum(userId, ProductType.valueOf(args.get(0)), TransactionType.DEPOSIT);
                    double withdraw = recommendationRepository.getTransactionSum(userId, ProductType.valueOf(args.get(0)), TransactionType.WITHDRAW);
                    res = compareValues(deposit, withdraw, args.get(1));
                }
            }
        } catch (Exception e) {
            return false;
        }
        return condition.isNegate() ? !res : res;
    }

    private boolean compareValues(double v1, double v2, String op) {
        return switch (op) {
            case ">" -> v1 > v2;
            case "<" -> v1 < v2;
            case "=" -> v1 == v2;
            case ">=" -> v1 >= v2;
            case "<=" -> v1 <= v2;
            default -> false;
        };
    }

    @Transactional
    public RuleDto createRule(RuleDto dto) {
        RuleEntity entity = new RuleEntity();
        entity.setProductName(dto.getProductName());
        entity.setProductId(dto.getProductId());
        entity.setProductText(dto.getProductText());

        List<QueryConditionEntity> conditions = dto.getRule().stream().map(cDto -> {
            QueryConditionEntity cEntity = new QueryConditionEntity();
            cEntity.setQuery(cDto.getQuery());
            cEntity.setArguments(cDto.getArguments());
            cEntity.setNegate(cDto.isNegate());
            cEntity.setRuleEntity(entity);
            return cEntity;
        }).collect(Collectors.toList());

        entity.setRule(conditions);
        RuleEntity saved = ruleRepository.save(entity);
        dto.setId(saved.getId());
        return dto;
    }

    public RuleListResponseDto getAllRules() {
        List<RuleDto> list = ruleRepository.findAll().stream().map(e -> new RuleDto(
                e.getId(), e.getProductName(), e.getProductId(), e.getProductText(),
                e.getRule().stream().map(c -> new RuleConditionDto(c.getQuery(), c.getArguments(), c.isNegate())).collect(Collectors.toList())
        )).collect(Collectors.toList());
        return new RuleListResponseDto(list);
    }

    @Transactional
    public void deleteRule(UUID id) {
        ruleRepository.deleteById(id);
    }
}