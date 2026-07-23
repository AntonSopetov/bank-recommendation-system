package ru.star.bank.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.star.bank.dto.*;
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

    @PostMapping("/rule")
    public RuleDto createRule(@RequestBody RuleDto dto) {
        return recommendationService.createRule(dto);
    }

    @GetMapping("/rule")
    public RuleListResponseDto getAllRules() {
        return recommendationService.getAllRules();
    }

    @DeleteMapping("/rule/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRule(@PathVariable("id") UUID id) {
        recommendationService.deleteRule(id);
    }
}