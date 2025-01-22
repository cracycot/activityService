package org.example.controllers;

import org.example.services.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

@RestController("/activities")
public class ActivityController {
    private static final Logger log = LoggerFactory.getLogger(ActivityController.class);
    RedisService redisService;

    @GetMapping("/get")
    public ResponseEntity<?> getActivityRating(@RequestParam(name = "size", defaultValue = "10") int size) {
        try {
            Set<Long> result = new HashSet<>(redisService.getTopElements(size));
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            log.error("Ошибка при получении рейтинга", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Произошла ошибка");
        }
    }

    @Autowired
    public void setRedisService(RedisService redisService) {
        this.redisService = redisService;
    }
}
