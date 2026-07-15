package ru.star.bank.rule;

import ru.star.bank.dto.RecommendationDto;
import java.util.Optional;
import java.util.UUID;

public interface RecommendationRuleSet {
    Optional<RecommendationDto> check(UUID userId);
}