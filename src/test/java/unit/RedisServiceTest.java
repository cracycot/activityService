package unit;

import org.example.services.RedisService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.ZSetOperations;

@ExtendWith(MockitoExtension.class)
class RedisServiceTest {

    @Mock
    private ZSetOperations<String, Long> zSetOperations;

    @InjectMocks
    private RedisService redisService;

    @Test
    void testIncrementElementScoreWhenExistingScore() {
        Long userId = 1L;
        int increment = 5;
        Mockito.when(zSetOperations.score("user:ranking", userId)).thenReturn(10.0);

        redisService.incrementElementScore(userId, increment);

        Mockito.verify(zSetOperations).add("user:ranking", userId, 15.0);
    }

    @Test
    void testIncrementElementScoreWhenNullScore() {
        Long userId = 2L;
        int increment = 5;
        Mockito.when(zSetOperations.score("user:ranking", userId)).thenReturn(null);

        redisService.incrementElementScore(userId, increment);

        Mockito.verify(zSetOperations).add("user:ranking", userId, 5.0);
    }

    @Test
    void testDecrementElementScorePositiveResult() {
        Long userId = 3L;
        int decrement = 4;
        Mockito.when(zSetOperations.score("user:ranking", userId)).thenReturn(10.0);

        redisService.decrementElementScore(userId, decrement);

        Mockito.verify(zSetOperations).add("user:ranking", userId, 6.0);
    }

    @Test
    void testDecrementElementScoreRemoveElement() {
        Long userId = 4L;
        int decrement = 15;
        Mockito.when(zSetOperations.score("user:ranking", userId)).thenReturn(10.0);

        redisService.decrementElementScore(userId, decrement);

        Mockito.verify(zSetOperations).remove("user:ranking", userId);
    }
}
