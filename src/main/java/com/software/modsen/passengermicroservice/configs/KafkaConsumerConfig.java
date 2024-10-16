package com.software.modsen.passengermicroservice.configs;

import com.software.modsen.passengermicroservice.entities.rating.PassengerRatingMessage;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@AllArgsConstructor
@EnableKafka
public class KafkaConsumerConfig {
    private Environment environment;

    public Map<String, Object> consumerFactory() {
        Map<String, Object> kafkaConsumerProps = new HashMap<>();
        kafkaConsumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                environment.getProperty("spring.kafka.consumer.bootstrap-servers"));
        kafkaConsumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                ErrorHandlingDeserializer.class);

        kafkaConsumerProps.put(JsonDeserializer.TRUSTED_PACKAGES,
                "*");
        kafkaConsumerProps.put(ConsumerConfig.GROUP_ID_CONFIG,
                environment.getProperty("spring.kafka.consumer.group-id"));

        return kafkaConsumerProps;
    }

    @Bean
    public ConsumerFactory<String, PassengerRatingMessage> passengerRatingConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerFactory(),
                new StringDeserializer(),
                new JsonDeserializer<>(PassengerRatingMessage.class, false));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PassengerRatingMessage> kafkaListenerContainerFactory(
            ConsumerFactory<String, PassengerRatingMessage> passengerRatingProducerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, PassengerRatingMessage> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(passengerRatingProducerFactory);

        return factory;
    }
}