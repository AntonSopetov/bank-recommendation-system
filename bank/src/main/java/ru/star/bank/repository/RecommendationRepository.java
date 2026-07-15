package ru.star.bank.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public class RecommendationRepository {
    private final JdbcTemplate jdbcTemplate;

    public RecommendationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean hasProductType(UUID userId, ProductType productType) {
        String sql = "SELECT COUNT(*) FROM TRANSACTIONS t " +
                "JOIN PRODUCTS p ON t.PRODUCT_ID = p.ID " +
                "WHERE t.USER_ID = ? AND p.TYPE = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId.toString(), productType.name());
        return count != null && count > 0;
    }

    public double getTransactionSum(UUID userId, ProductType productType, TransactionType transactionType) {
        String sql = "SELECT COALESCE(SUM(t.AMOUNT), 0) FROM TRANSACTIONS t " +
                "JOIN PRODUCTS p ON t.PRODUCT_ID = p.ID " +
                "WHERE t.USER_ID = ? AND p.TYPE = ? AND t.TYPE = ?";
        Double sum = jdbcTemplate.queryForObject(sql, Double.class, userId.toString(), productType.name(), transactionType.name());
        return sum != null ? sum : 0.0;
    }
}