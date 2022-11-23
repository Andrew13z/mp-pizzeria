package com.example.clientapp.kafka;

import com.example.clientapp.repository.OrderRepository;
import com.example.palmettoentities.dto.ClientDto;
import com.example.palmettoentities.dto.OrderDto;
import com.example.palmettoentities.dto.PizzaDto;
import com.example.palmettoentities.enums.OrderStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

@Testcontainers
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc
class OrderIntegrationTest {

  public static final String ORDER_TOPIC = "order_topic";

  public static final String NOTIFICATION_TOPIC = "notification_topic";

  private static final Long ID_ONE = 1L;

  @Container
  static KafkaContainer kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"));

  @Container
  static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
    .withDatabaseName("integration-tests-db")
    .withUsername("sa")
    .withPassword("sa")
    .withInitScript("data.sql");

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private OrderRepository orderRepository;

  @DynamicPropertySource
  static void kafkaProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers);
    registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
    registry.add("spring.datasource.username", postgresContainer::getUsername);
    registry.add("spring.datasource.password", postgresContainer::getPassword);
  }

  @Test
  void orderIsSavedInDbAndSentInKafkaWhenOrderIsCreated() throws Exception {
    var mvcResult = mockMvc.perform(post("/orders")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(getOrderDtoForCreation())))
      .andExpect(status().isCreated())
      .andReturn();

    var orderResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), OrderDto.class);
    var id = orderResponse.getId();

    assertNotNull(id);
    assertEquals(OrderStatus.CREATED, orderResponse.getStatus());

    var orderEntity = orderRepository.findById(id);

    assertTrue(orderEntity.isPresent());

    var kafkaConsumer = getKafkaConsumer();
    kafkaConsumer.subscribe(List.of(ORDER_TOPIC));

    var records = kafkaConsumer.poll(Duration.of(5, ChronoUnit.SECONDS));

    assertEquals(1, records.count());

    for (var record : records) {
      assertEquals(ORDER_TOPIC, record.topic());
      assertEquals(ID_ONE.toString(), record.key());
      assertEquals(orderResponse, record.value());
    }
  }

  @Test
  void orderIsSavedInDBAfterReadFromKafka() {
    var producer = getKafkaProducer();

    var orderDto = getOrderDto(ID_ONE, OrderStatus.DELIVERED);

    producer.send(new ProducerRecord<>(NOTIFICATION_TOPIC, String.valueOf(ID_ONE), orderDto));

    await().pollInterval(Duration.of(1, ChronoUnit.SECONDS))
      .atMost(Duration.of(5, ChronoUnit.SECONDS))
      .untilAsserted(() -> {
        var orderEntity = orderRepository.findById(ID_ONE).get();
        assertEquals(OrderStatus.DELIVERED, orderEntity.getStatus());
      });
  }

  private KafkaConsumer<String, OrderDto> getKafkaConsumer() {
    var jsonDeserializer = new JsonDeserializer<OrderDto>();
    jsonDeserializer.addTrustedPackages(OrderDto.class.getPackageName());

    return new KafkaConsumer<>(
      Map.of(
        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
        kafkaContainer.getBootstrapServers(),
        ConsumerConfig.GROUP_ID_CONFIG,
        "tc-" + UUID.randomUUID(),
        ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,
        "earliest"
      ),
      new StringDeserializer(),
      jsonDeserializer
    );
  }

  private KafkaProducer<String, OrderDto> getKafkaProducer() {
    return new KafkaProducer<>(
      Map.of(
        ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
        kafkaContainer.getBootstrapServers(),
        ProducerConfig.CLIENT_ID_CONFIG,
        UUID.randomUUID().toString()
      ),
      new StringSerializer(),
      new JsonSerializer<>()
    );
  }

  private OrderDto getOrderDtoForCreation() {
    return getOrderDto(null, null);
  }

  private OrderDto getOrderDto(Long id, OrderStatus orderStatus) {
    var orderDto = new OrderDto();
    orderDto.setId(id);
    orderDto.setStatus(orderStatus);

    var clientDto = new ClientDto();
    clientDto.setId(1L);
    orderDto.setClient(clientDto);

    var pizzaDto = new PizzaDto();
    pizzaDto.setId(1L);
    orderDto.getPizzas().add(pizzaDto);

    return orderDto;
  }
}
