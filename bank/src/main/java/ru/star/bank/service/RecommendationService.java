package ru.star.bank.service;

import org.springframework.stereotype.Service;
import ru.star.bank.dto.RecommendationDto;
import ru.star.bank.dto.RecommendationResponseDto;
import ru.star.bank.rule.RecommendationRuleSet;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    private final List<RecommendationRuleSet> rules;

    public RecommendationService(List<RecommendationRuleSet> rules) {
        this.rules = rules;
    }

    public RecommendationResponseDto getRecommendations(UUID userId) {
        List<RecommendationDto> validRecommendations = rules.stream()
                .map(rule -> rule.check(userId))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        return new RecommendationResponseDto(userId, validRecommendations);
    }
}