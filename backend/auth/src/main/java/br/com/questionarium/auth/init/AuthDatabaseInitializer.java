package br.com.questionarium.auth.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class AuthDatabaseInitializer {

    private static final Logger logger = LoggerFactory.getLogger(AuthDatabaseInitializer.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    @EventListener(ApplicationReadyEvent.class)
    public void resetDatabase() {
        logger.info("Limpando collections do MongoDB (auth_db2)");

        mongoTemplate.getCollectionNames().forEach(collectionName -> {
            logger.info("Dropping collection: {}", collectionName);
            mongoTemplate.dropCollection(collectionName);
        });

        logger.info("Limpeza concluída");
    }
}