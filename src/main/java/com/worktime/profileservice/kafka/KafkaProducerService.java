package com.worktime.profileservice.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendEvent(String topic, String key, Object event){
        kafkaTemplate.send(topic, key, event);

        log.info("{} отправлено с ключом  {} в топик {}", event.getClass().getSimpleName(), key, topic);
    }
}