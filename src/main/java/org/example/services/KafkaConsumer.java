package org.example.services;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class KafkaConsumer {
    RedisService redisService;
    @KafkaListener(topics = "activity", groupId = "1")
    public void listen(ConsumerRecord<Long, String> record) {
        Optional<String> value = Optional.ofNullable(record.value());
        if (value.isPresent()) {
            System.out.println(value.get());
            List<String> result = new ArrayList<>(List.of(value.get().split(" ")));
            if (result.getFirst().equals("add:")) {
                redisService.incrementElementScore(Long.parseLong(result.get(1)), Integer.parseInt(result.get(2)));
            } else if (result.getFirst().equals("remove:")) {
                redisService.decrementElementScore(Long.parseLong(result.get(1)), Integer.parseInt(result.get(2)));
            } else {
                throw new RuntimeException();
            }
        }
    }

    @Autowired
    public void setRedisService(RedisService redisService) {
        this.redisService = redisService;
    }
}
