package ru.star.bank.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.UUID;

public class RecommendationResponseDto {
    @JsonProperty("user_id")
    private final UUID userId;
    private final List<RecommendationDto> recommendations;

    public RecommendationResponseDto(UUID userId, List<RecommendationDto> recommendations) {
        this.userId = userId;
        this.recommendations = recommendations;
    }

    public UUID getUserId() {
        return userId;
    }

    public List<RecommendationDto> getRecommendations() {
        return recommendations;
    }
}