package ru.star.bank.rule;

import org.springframework.stereotype.Component;
import ru.star.bank.dto.RecommendationDto;
import ru.star.bank.repository.ProductType;
import ru.star.bank.repository.RecommendationRepository;
import ru.star.bank.repository.TransactionType;

import java.util.Optional;
import java.util.UUID;

@Component
public class TopSavingRule implements RecommendationRuleSet {
    private static final UUID PRODUCT_ID = UUID.fromString("59efc529-2fff-41af-baff-90ccd7402925");
    private static final String PRODUCT_NAME = "Top Saving";
    private static final String PRODUCT_TEXT = "Откройте свою собственную «Копилку» с нашим банком! «Копилка» — это unique банковский инструмент, который поможет вам легко и удобно накапливать деньги на важные цели. Больше никаких забытых чеков и потерянных квитанций — всё под контролем!\n\nПреимущества «Копилки»:\n\nНакопление средств на конкретные цели. Установите лимит и срок накопления, и банк будет автоматически переводить определенную сумму на ваш счет.\n\nПрозрачность и контроль. Отслеживайте свои доходы и расходы, контролируйте процесс накопления и корректируйте стратегию при необходимости.\n\nБезопасность и надежность. Ваши средства находятся под защитой банка, а доступ к ним возможен только через мобильное приложение или интернет-банкинг.\n\nНачните использовать «Копилку» уже сегодня и станьте ближе к своим финансовым целям!";

    private final RecommendationRepository repository;

    public TopSavingRule(RecommendationRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<RecommendationDto> check(UUID userId) {
        boolean hasDebit = repository.hasProductType(userId, ProductType.DEBIT);

        double debitDepositSum = repository.getTransactionSum(userId, ProductType.DEBIT, TransactionType.DEPOSIT);
        double savingDepositSum = repository.getTransactionSum(userId, ProductType.SAVING, TransactionType.DEPOSIT);
        double debitWithdrawSum = repository.getTransactionSum(userId, ProductType.DEBIT, TransactionType.WITHDRAW);

        boolean condition2 = debitDepositSum >= 50000 || savingDepositSum >= 50000;
        boolean condition3 = debitDepositSum > debitWithdrawSum;

        if (hasDebit && condition2 && condition3) {
            return Optional.of(new RecommendationDto(PRODUCT_ID, PRODUCT_NAME, PRODUCT_TEXT));
        }

        return Optional.empty();
    }
}