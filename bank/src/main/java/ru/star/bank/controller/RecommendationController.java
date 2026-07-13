package ru.star.bank.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.star.bank.dto.RecommendationResponseDto;
import ru.star.bank.service.RecommendationService;

import java.util.UUID;

@RestController
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping("/recommendation/{user_id}")
    public RecommendationResponseDto getRecommendations(@PathVariable("user_id") UUID userId) {
        return recommendationService.getRecommendations(userId);
    }
}