package ru.star.bank.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.UUID;

public class RuleDto {
    private UUID id;
    @JsonProperty("product_name")
    private String productName;
    @JsonProperty("product_id")
    private UUID productId;
    @JsonProperty("product_text")
    private String productText;
    @JsonProperty("rule")
    private List<RuleConditionDto> rule;

    public RuleDto() {}

    public RuleDto(UUID id, String productName, UUID productId, String productText, List<RuleConditionDto> rule) {
        this.id = id;
        this.productName = productName;
        this.productId = productId;
        this.productText = productText;
        this.rule = rule;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public UUID getProductId() { return productId; }
    public void setProductId(UUID productId) { this.productId = productId; }

    public String getProductText() { return productText; }
    public void setProductText(String productText) { this.productText = productText; }

    public List<RuleConditionDto> getRule() { return rule; }
    public void setRule(List<RuleConditionDto> rule) { this.rule = rule; }
}