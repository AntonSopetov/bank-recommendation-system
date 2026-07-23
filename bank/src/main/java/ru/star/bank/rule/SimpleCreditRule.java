package ru.star.bank.rule;

import org.springframework.stereotype.Component;
import ru.star.bank.dto.RecommendationDto;
import ru.star.bank.repository.ProductType;
import ru.star.bank.repository.RecommendationRepository;
import ru.star.bank.repository.TransactionType;
import java.util.Optional;
import java.util.UUID;

@Component
public class SimpleCreditRule implements RecommendationRuleSet {
    private static final UUID PRODUCT_ID = UUID.fromString("ab138afb-f3ba-4a93-b74f-0fcee86d447f");
    private static final String PRODUCT_NAME = "Простой кредит";
    private static final String PRODUCT_TEXT = "Откройте мир выгодных кредитов с нами!";

    private final RecommendationRepository repository;

    public SimpleCreditRule(RecommendationRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<RecommendationDto> check(UUID userId) {
        boolean hasCredit = repository.hasProductType(userId, ProductType.CREDIT);
        double debitDepositSum = repository.getTransactionSum(userId, ProductType.DEBIT, TransactionType.DEPOSIT);
        double debitWithdrawSum = repository.getTransactionSum(userId, ProductType.DEBIT, TransactionType.WITHDRAW);

        if (!hasCredit && (debitDepositSum > debitWithdrawSum) && (debitWithdrawSum > 100000)) {
            return Optional.of(new RecommendationDto(PRODUCT_ID, PRODUCT_NAME, PRODUCT_TEXT));
        }
        return Optional.empty();
    }
}