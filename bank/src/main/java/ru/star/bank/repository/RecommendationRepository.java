package ru.star.bank.repository;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.time.Duration;
import java.util.UUID;

@Repository
public class RecommendationRepository {

    private final JdbcTemplate jdbcTemplate;

    private final Cache<String, Boolean> hasProductCache;
    private final Cache<String, Double> transactionSumCache;
    private final Cache<String, Integer> transactionCountCache;

    public RecommendationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;

        this.hasProductCache = Caffeine.newBuilder().expireAfterWrite(Duration.ofMinutes(10)).build();
        this.transactionSumCache = Caffeine.newBuilder().expireAfterWrite(Duration.ofMinutes(10)).build();
        this.transactionCountCache = Caffeine.newBuilder().expireAfterWrite(Duration.ofMinutes(10)).build();
    }

    public boolean hasProductType(UUID userId, ProductType productType) {
        String cacheKey = userId + "_" + productType.name();

        return hasProductCache.get(cacheKey, key -> {
            String sql = "SELECT COUNT(*) FROM TRANSACTIONS t " +
                    "JOIN PRODUCTS p ON t.PRODUCT_ID = p.ID " +
                    "WHERE t.USER_ID = ? AND p.TYPE = ?";
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId.toString(), productType.name());
            return count != null && count > 0;
        });
    }

    public double getTransactionSum(UUID userId, ProductType productType, TransactionType transactionType) {
        String cacheKey = userId + "_" + productType.name() + "_" + transactionType.name();

        Double result = transactionSumCache.get(cacheKey, key -> {
            String sql = "SELECT COALESCE(SUM(t.AMOUNT), 0) FROM TRANSACTIONS t " +
                    "JOIN PRODUCTS p ON t.PRODUCT_ID = p.ID " +
                    "WHERE t.USER_ID = ? AND p.TYPE = ? AND t.TYPE = ?";
            return jdbcTemplate.queryForObject(sql, Double.class, userId.toString(), productType.name(), transactionType.name());
        });
        return result != null ? result : 0.0;
    }

    public int getTransactionCount(UUID userId, ProductType productType) {
        String cacheKey = userId + "_" + productType.name();

        Integer result = transactionCountCache.get(cacheKey, key -> {
            String sql = "SELECT COUNT(*) FROM TRANSACTIONS t " +
                    "JOIN PRODUCTS p ON t.PRODUCT_ID = p.ID " +
                    "WHERE t.USER_ID = ? AND p.TYPE = ?";
            return jdbcTemplate.queryForObject(sql, Integer.class, userId.toString(), productType.name());
        });
        return result != null ? result : 0;
    }
}