package ru.star.bank.dto;

import java.util.List;
import java.util.UUID;

public class RecommendationResponseDto {
    private UUID user_id;
    private List<RecommendationDto> recommendations;

    public RecommendationResponseDto(UUID userId, List<RecommendationDto> recommendations) {
        this.user_id = userId;
        this.recommendations = recommendations;
    }

    public UUID getUser_id() {
        return user_id;
    }

    public List<RecommendationDto> getRecommendations() {
        return recommendations;
    }

    public void setUser_id(UUID user_id) {
        this.user_id = user_id;
    }

    public void setRecommendations(List<RecommendationDto> recommendations) {
        this.recommendations = recommendations;
    }
}