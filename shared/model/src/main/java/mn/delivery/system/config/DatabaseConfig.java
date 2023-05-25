package mn.delivery.system.config;

import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.lang.NonNull;
import org.bson.UuidRepresentation;
import org.bson.types.Decimal128;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author MethoD
 */
@Configuration
@EnableMongoAuditing
public class DatabaseConfig extends AbstractMongoClientConfiguration {

    @Value("${primary.mongodb.dbName}")
    private String dbName;

    @Value("${primary.mongodb.uri}")
    private String primaryUri;

    @Override
    @NonNull
    protected String getDatabaseName() {
        return dbName;
    }

    @Primary
    @Bean
    @Override
    @NonNull
    public MongoClient mongoClient() {
        return MongoClients.create(
                MongoClientSettings.builder()
                        .applyConnectionString(new ConnectionString(primaryUri))
                        .uuidRepresentation(UuidRepresentation.STANDARD)
                        .build());
    }

    @Primary
    @Bean(name = "mongoTemplate")
    public MongoTemplate mongoTemplate(MongoClient mongoClient) {
        return new MongoTemplate(mongoClient, getDatabaseName());
    }

//    @Primary
//    @Bean(name = "mongoOperations")
//    public MongoOperations mongoOperations(MongoClient mongoClient) {
//        return new MongoTemplate(mongoClient, getDatabaseName());
//    }

    @Primary
    @Bean
    public MongoTransactionManager transactionManager(MongoDatabaseFactory dbFactory) {
        return new MongoTransactionManager(
                dbFactory,
                TransactionOptions.builder()
                        .readConcern(ReadConcern.MAJORITY)
                        .writeConcern(WriteConcern.MAJORITY)
                        .build()
        );
    }

    @Bean
    @Override
    @NonNull
    public MappingMongoConverter mappingMongoConverter(
            @NonNull MongoDatabaseFactory databaseFactory,
            @NonNull MongoCustomConversions customConversions,
            @NonNull MongoMappingContext mappingContext) {
        MappingMongoConverter mappingMongoConverter = super.mappingMongoConverter(databaseFactory,
                customConversions, mappingContext);
//        converter.setTypeMapper(typeMapper());
        mappingMongoConverter.setCustomConversions(mongoCustomConversions());
        mappingMongoConverter.setMapKeyDotReplacement(".");
        return mappingMongoConverter;
    }

    @Bean
    public MongoCustomConversions mongoCustomConversions() {
        return new MongoCustomConversions(
                List.of(
                        new BigDecimalDecimal128Converter(),
                        new Decimal128BigDecimalConverter(),
                        new EnumsConverter.ApplicationRoleConverter(),
                        new EnumsConverter.SharedStatusConverter(),
                        new EnumsConverter.SystemCronTypeConverter()
                )
        );
    }

    @WritingConverter
    private static class BigDecimalDecimal128Converter implements Converter<BigDecimal, Decimal128> {
        @Override
        public Decimal128 convert(@NonNull BigDecimal source) {
            return new Decimal128(source);
        }
    }

    @ReadingConverter
    private static class Decimal128BigDecimalConverter implements Converter<Decimal128, BigDecimal> {
        @Override
        public BigDecimal convert(@NonNull Decimal128 source) {
            return source.bigDecimalValue();
        }
    }

//    @Bean
//    public MongoTransactionManager txManager(MongoDatabaseFactory dbFactory) {
//        return new MongoTransactionManager(dbFactory);
//    }
//
//    @Primary
//    @Bean
//    @ConfigurationProperties(prefix = "primary.mongodb")
//    public MongoProperties getPrimary() {
//        return new MongoProperties();
//    }
//
//    @Primary
//    @Bean(name = "mongoTemplate")
//    public MongoTemplate mongoTemplate() {
//        MongoTemplate mongoTemplate = new MongoTemplate(new SimpleMongoClientDatabaseFactory(getPrimary().getUri()));
//        mongoTemplate.setReadPreference(ReadPreference.primary());
//
//        MappingMongoConverter mappingMongoConverter = (MappingMongoConverter) mongoTemplate.getConverter();
//        mappingMongoConverter.setCustomConversions(mongoCustomConversions());
//
//        return mongoTemplate;
//    }
}
