package ru.star.bank.entity;

import jakarta.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "query_conditions")
public class QueryConditionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String query;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "condition_arguments", joinColumns = @JoinColumn(name = "condition_id"))
    @Column(name = "argument")
    private List<String> arguments;

    private boolean negate;

    @ManyToOne
    @JoinColumn(name = "rule_id")
    private RuleEntity ruleEntity;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getQuery() { return query; }
    public void setQuery(String query) { this.query = query; }

    public List<String> getArguments() { return arguments; }
    public void setArguments(List<String> arguments) { this.arguments = arguments; }

    public boolean isNegate() { return negate; }
    public void setNegate(boolean negate) { this.negate = negate; }

    public RuleEntity getRuleEntity() { return ruleEntity; }
    public void setRuleEntity(RuleEntity ruleEntity) { this.ruleEntity = ruleEntity; }
}