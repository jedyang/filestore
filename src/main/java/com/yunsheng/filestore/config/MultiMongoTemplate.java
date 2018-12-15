package com.yunsheng.filestore.config;

import com.mongodb.client.MongoDatabase;

//import org.springframework.data.mongodb.MongoDbFactory;
//import org.springframework.data.mongodb.core.MongoTemplate;

public class MultiMongoTemplate {}
        /**
        extends MongoTemplate {
    private static ThreadLocal<MongoDbFactory> mongoDbFactoryThreadLocal;

    public MultiMongoTemplate(MongoDbFactory mongoDbFactory) {
        super(mongoDbFactory);
        if (mongoDbFactoryThreadLocal == null) {
            mongoDbFactoryThreadLocal = new ThreadLocal<>();
        }
    }

    public void setMongoDbFactory(MongoDbFactory factory) {
        mongoDbFactoryThreadLocal.set(factory);
    }

    public void removeMongoDbFactory() {
        mongoDbFactoryThreadLocal.remove();
    }

    @Override
    public MongoDatabase getDb() {
        return mongoDbFactoryThreadLocal.get().getDb();
    }
}
**/