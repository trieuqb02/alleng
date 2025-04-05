package com.alleng.news.rabbitmq.consumer;

import com.alleng.commonlibrary.constant.RabbitMQConstant;
import com.alleng.commonlibrary.util.JwtUtilCommon;
import com.alleng.commonlibrary.util.PasserUtil;
import com.alleng.news.constant.StatusType;
import com.alleng.news.entity.News;
import com.alleng.news.payload.MediaConsumer;
import com.alleng.news.repository.NewsRepository;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class NewsConsumer {

    PasserUtil passerUtil;

    NewsRepository newsRepository;

    JwtUtilCommon jwtUtilCommon;

    @RabbitListener(queues = RabbitMQConstant.QUEUE_NEWS_COMPLETED)
    @Transactional
    public void handleCompletedFile(Message message) {
        MessageProperties messageProperties = message.getMessageProperties();
        String authorizationHeader = messageProperties.getHeader("Authorization");

        MediaConsumer mediaConsumer = passerUtil.parseToObject(message.getBody(), MediaConsumer.class);

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            authorizationHeader = authorizationHeader.substring(7);

            String token = authorizationHeader;

            try {
                JWTClaimsSet claimsSet = jwtUtilCommon.decodeToken(token);
                String sub = claimsSet.getStringClaim("sub");

                Jwt jwt = Jwt.withTokenValue(authorizationHeader)
                        .header("alg", "RSA")
                        .claim("sub", sub)
                        .build();
                JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(jwt);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } catch (ParseException parseException) {
                log.error(parseException.getMessage());
            }
        }

        Optional<News> news = newsRepository.findById(UUID.fromString(mediaConsumer.newsId()));
        if (news.isPresent()) {
            if(!mediaConsumer.audioPath().isEmpty()){
                news.get().setAudio(mediaConsumer.audioPath());
            }
            if(!mediaConsumer.imagePath().isEmpty()){
                news.get().setThumbnail(mediaConsumer.imagePath());
            }
            news.get().setStatus(StatusType.FINISH);
            newsRepository.save(news.get());
        }
    }

    @RabbitListener(queues = RabbitMQConstant.QUEUE_NEWS_FAILED)
    @Transactional
    public void handleFailedFile(String payload) {
        // send notification for user
        System.out.println("notification: " + payload);
    }

}
