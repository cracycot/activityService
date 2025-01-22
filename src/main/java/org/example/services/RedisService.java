package org.example.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RedisService {
    @Autowired
    ZSetOperations<String, Long> zSetOperations;
    private static final String RANKING_KEY = "user:ranking";

    private Double getElementScore(Long value) {
        return zSetOperations.score(RANKING_KEY, value);
    }

    public void decrementElementScore(Long value, int decrement) {
        Double currentScore = getElementScore(value);
        if (currentScore != null) {
            double newScore = currentScore - decrement;
            if (newScore > 0) {
                zSetOperations.add(RANKING_KEY, value, newScore);
            } else {
                // Если значение стало отрицательным или ноль, можно удалить элемент
                zSetOperations.remove(RANKING_KEY, value);
            }
        }
    }
    public void incrementElementScore(Long value, int increment) {
        Double currentScore = getElementScore(value);
        if (currentScore != null) {
            double newScore = currentScore + increment;
            zSetOperations.add(RANKING_KEY, value, newScore);
        } else {
            zSetOperations.add(RANKING_KEY, value, increment);
        }
    }

    private void addElementToZSet(Long value, double score) {
        zSetOperations.add(RANKING_KEY, value, score);
    }

    public Set<Long> getRangeFromZSet(long start, long end) {
        return zSetOperations.range(RANKING_KEY, start, end);
    }

    public void removeElementFromZSet(Object value) {
        zSetOperations.remove(RANKING_KEY, value);
    }

    public Long getZSetSize(String key) {
        return zSetOperations.zCard(key);
    }

    public Set<Long> getTopElements(int count) {
        return zSetOperations.reverseRange(RANKING_KEY, 0, count - 1);
    }
}
