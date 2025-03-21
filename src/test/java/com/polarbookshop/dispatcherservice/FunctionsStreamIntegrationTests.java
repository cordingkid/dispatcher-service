package com.polarbookshop.dispatcherservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;

import java.io.IOException;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(TestChannelBinderConfiguration.class)                                           // 테스트 빌더 설정
public class FunctionsStreamIntegrationTests {

    @Autowired
    private InputDestination input;

    @Autowired
    private OutputDestination output;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void whenOrderAcceptedThenDispatched() throws IOException {
        long orderId = 121;

        Message<OrderAcceptedMessage> inputMessage = MessageBuilder
                .withPayload(new OrderAcceptedMessage(orderId)).build();

        Message<OrderDispatchedMessage> expectedOutputMessage  = MessageBuilder
                .withPayload(new OrderDispatchedMessage(orderId)).build();

        // 입력 채널로 메세지 전송
        input.send(inputMessage);
        assertThat(objectMapper.readValue(output.receive().getPayload(), OrderDispatchedMessage.class))
                .isEqualTo(expectedOutputMessage.getPayload());                             // 출력 채널로부터 메세지 받아서 확인
    }
}
