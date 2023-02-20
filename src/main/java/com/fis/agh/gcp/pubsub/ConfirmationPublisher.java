package com.fis.agh.gcp.pubsub;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutureCallback;
import com.google.api.core.ApiFutures;
import com.google.api.gax.rpc.ApiException;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.TopicName;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConfirmationPublisher {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfirmationPublisher.class);
    private final PublisherFactory publisherFactory;

    public void publish(TopicName topicName, ByteString message) {
        Publisher publisher = publisherFactory.create(topicName);
        try {
            publishMessage(publisher, message);
        } finally {
            publisher.shutdown();
        }
    }

    public void publishMessage(Publisher publisher, ByteString message) {
        PubsubMessage pubsubMessage =
                PubsubMessage.newBuilder().setData(message).build();
        ApiFuture<String> future = publisher.publish(pubsubMessage);
        ApiFutures.addCallback(future, createApiFutureCallbacks(), MoreExecutors.directExecutor());
    }

    private ApiFutureCallback<String> createApiFutureCallbacks() {
        return new ApiFutureCallback<>() {
            @Override
            public void onFailure(Throwable throwable) {
                if (throwable instanceof ApiException) {
                    logApiException(throwable);
                }
                LOGGER.error("Error publishing message");
            }

            @Override
            public void onSuccess(String messageId) {
                LOGGER.info("Success: {}", messageId);
            }
        };
    }

    private void logApiException(Throwable throwable) {
        ApiException apiException = ((ApiException) throwable);
        LOGGER.error(String.valueOf(apiException.getStatusCode().getCode()));
        LOGGER.error(String.valueOf(apiException.isRetryable()));
    }
}

