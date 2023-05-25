package mn.delivery.system.config;

import java.util.ArrayList;
import java.util.List;

import jakarta.annotation.PostConstruct;

import mn.delivery.system.dto.IndexDto;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Configuration
@DependsOn("mongoTemplate")
public class MongoIndexConfig {

    private final MongoTemplate mongoTemplate;

    @PostConstruct
    public void initIndexes() {
        List<IndexDto> indexDefs = new ArrayList<>();

        // systemCron
        // indexDefs.add(new IndexDto("systemCron", "cronType"));
        // systemKeyValue
        // indexDefs.add(new IndexDto("systemKeyValue", "key"));

        // deviceToken
        indexDefs.add(new IndexDto("deviceToken", "deviceId"));
        indexDefs.add(new IndexDto("deviceToken", "email"));
        indexDefs.add(new IndexDto("deviceToken", "os"));
        indexDefs.add(new IndexDto("deviceToken", "token"));

        // email
        indexDefs.add(new IndexDto("email", "to"));
        indexDefs.add(new IndexDto("email", "result"));
        indexDefs.add(new IndexDto("email", "queueSend"));
        indexDefs.add(new IndexDto("email", "confirmationEmail"));
        indexDefs.add(new IndexDto("email", "confirmationType"));
        indexDefs.add(new IndexDto("email", "deleted"));

        // emailConfirmation
        indexDefs.add(new IndexDto("emailConfirmation", "email"));
        indexDefs.add(new IndexDto("emailConfirmation", "token"));
        indexDefs.add(new IndexDto("emailConfirmation", "type"));
        indexDefs.add(new IndexDto("emailConfirmation", "deleted"));
        indexDefs.add(new IndexDto("emailConfirmation", "expiredDate"));
        indexDefs.add(new IndexDto("emailConfirmation", "confirmedDate"));

        // location
        indexDefs.add(new IndexDto("location", "order"));
        indexDefs.add(new IndexDto("location", "parentCode"));

        // notification
        indexDefs.add(new IndexDto("notification", "userId"));
        indexDefs.add(new IndexDto("notification", "status"));
        indexDefs.add(new IndexDto("notification", "relatedDataType"));
        indexDefs.add(new IndexDto("notification", "relatedDataId"));

        // pushNotification
        indexDefs.add(new IndexDto("pushNotification", "type"));
        indexDefs.add(new IndexDto("pushNotification", "title"));
        indexDefs.add(new IndexDto("pushNotification", "priority"));
        indexDefs.add(new IndexDto("pushNotification", "sendType"));
        indexDefs.add(new IndexDto("pushNotification", "receiverType"));
        indexDefs.add(new IndexDto("pushNotification", "receiver"));
        indexDefs.add(new IndexDto("pushNotification", "prepareResult"));
        indexDefs.add(new IndexDto("pushNotification", "sendResult"));
        indexDefs.add(new IndexDto("pushNotification", "sentDate"));

        // user
        indexDefs.add(new IndexDto("user", "email", true));
        indexDefs.add(new IndexDto("user", "mobile"));
        indexDefs.add(new IndexDto("user", "externalId"));
        indexDefs.add(new IndexDto("user", "active"));
        indexDefs.add(new IndexDto("user", "role"));
        indexDefs.add(new IndexDto("user", "deleted"));
        indexDefs.add(new IndexDto("user", "emailVerified"));
        indexDefs.add(new IndexDto("user", "mobileVerified"));

        // userCitizenData
        indexDefs.add(new IndexDto("userCitizenData", "userId"));
        indexDefs.add(new IndexDto("userCitizenData", "registryNumber"));

        // userLoginHistory
        indexDefs.add(new IndexDto("userLoginHistory", "userId"));
        indexDefs.add(new IndexDto("userLoginHistory", "deviceId"));
        indexDefs.add(new IndexDto("userLoginHistory", "ip"));
        // addGenericIndexes("userLoginHistory", indexDefs);

        try {
            for (IndexDto indexDto : indexDefs) {
                Index index = new Index();
                if (indexDto.getFields() != null) {
                    StringBuilder fieldNames = new StringBuilder();
                    for (String field : indexDto.getFields()) {
                        fieldNames.append("_").append(field);
                        index = index.on(field, Sort.Direction.ASC);
                    }
                    index = index.named(indexDto.getCollection() + fieldNames);
                } else {
                    index = index
                            .on(indexDto.getField(), Sort.Direction.ASC)
                            .named(indexDto.getCollection() + "_" + indexDto.getField());
                }

                if (indexDto.isUnique()) {
                    index = index.unique();
                }
                mongoTemplate.indexOps(indexDto.getCollection()).ensureIndex(index);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private void addGenericIndexes(String collection, List<IndexDto> indexDefs) {
        String[] genericFields = new String[] { "createdDate", "modifiedDate" };
        for (String genericField : genericFields) {
            indexDefs.add(new IndexDto(collection, genericField));
        }
    }

    private void addGenericUserIndexes(String collection, List<IndexDto> indexDefs) {
        String[] genericFields = new String[] { "createdBy", "modifiedBy" };
        for (String genericField : genericFields) {
            indexDefs.add(new IndexDto(collection, genericField));
        }
    }
}
