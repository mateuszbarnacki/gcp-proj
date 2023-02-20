package com.fis.agh.gcp.pubsub;

import com.google.cloud.pubsub.v1.Publisher;
import com.google.pubsub.v1.TopicName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class PublisherFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(PublisherFactory.class);

    public Publisher create(TopicName topicName) {
        try {
            return buildPublisher(topicName);
        } catch (IOException e) {
            LOGGER.debug("Could not create publisher: {}", e.getMessage());
            return null;
        }
    }

    private Publisher buildPublisher(TopicName topicName) throws IOException {
        return Publisher.newBuilder(topicName)
                .setEndpoint("europe-west3-pubsub.googleapis.com:443")
                .setEnableMessageOrdering(true)
                .build();
    }
}
