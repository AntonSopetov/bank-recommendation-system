package ru.star.bank.rule;

import org.springframework.stereotype.Component;
import ru.star.bank.dto.RecommendationDto;
import ru.star.bank.repository.ProductType;
import ru.star.bank.repository.RecommendationRepository;
import ru.star.bank.repository.TransactionType;
import java.util.Optional;
import java.util.UUID;

@Component
public class Invest500Rule implements RecommendationRuleSet {
    private static final UUID PRODUCT_ID = UUID.fromString("147f6a0f-3b91-413b-ab99-87f081d60d5a");
    private static final String PRODUCT_NAME = "Invest 500";
    private static final String PRODUCT_TEXT = "Откройте свой путь к успеху с индивидуальным инвестиционным счетом (ИИС) от нашего банка! Воспользуйтесь налоговыми льготами и начните инвестировать с умом.";

    private final RecommendationRepository repository;

    public Invest500Rule(RecommendationRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<RecommendationDto> check(UUID userId) {
        boolean hasDebit = repository.hasProductType(userId, ProductType.DEBIT);
        boolean hasInvest = repository.hasProductType(userId, ProductType.INVEST);
        double savingDepositSum = repository.getTransactionSum(userId, ProductType.SAVING, TransactionType.DEPOSIT);

        if (hasDebit && !hasInvest && savingDepositSum > 1000) {
            return Optional.of(new RecommendationDto(PRODUCT_ID, PRODUCT_NAME, PRODUCT_TEXT));
        }
        return Optional.empty();
    }
}