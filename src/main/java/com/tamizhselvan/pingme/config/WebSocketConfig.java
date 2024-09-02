package com.tamizhselvan.pingme.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.messaging.converter.DefaultContentTypeResolver;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.List;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketConfig.class);
    private final ObjectMapper objectMapper;

    public WebSocketConfig(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        logger.info("Configuring message broker");
        registry.enableSimpleBroker("/user");
        registry.setApplicationDestinationPrefixes("/app");
        registry.setUserDestinationPrefix("/user");
        logger.info("Message broker configured with user prefix: /user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        logger.info("Registering STOMP endpoints");
        registry.addEndpoint("/ws")
                .setAllowedOrigins("http://localhost:3000")
                .withSockJS()
                .setInterceptors(new HttpSessionHandshakeInterceptor() {
                    @Override
                    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex) {
                        logger.debug("WebSocket handshake successful for endpoint /ws");
                    }
                });
        logger.info("STOMP endpoints registered with SockJS support");
    }

    @Override
    public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
        logger.info("Configuring message converters");
        DefaultContentTypeResolver resolver = new DefaultContentTypeResolver();
        resolver.setDefaultMimeType(MimeTypeUtils.APPLICATION_JSON);
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setObjectMapper(new ObjectMapper());
        converter.setContentTypeResolver(resolver);
        converter.getObjectMapper().registerModule(new JavaTimeModule());
        messageConverters.add(converter);
        logger.info("Message converters configured");
        return false;
    }
}
