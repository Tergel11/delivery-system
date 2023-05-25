package mn.delivery.system.dao.mobile;

import lombok.RequiredArgsConstructor;
import mn.delivery.system.database.util.MongoUtil;
import mn.delivery.system.model.mobile.PushNotification;
import mn.delivery.system.model.mobile.enums.PushNotificationReceiverType;
import mn.delivery.system.model.mobile.enums.PushNotificationSendType;
import mn.delivery.system.model.mobile.enums.PushNotificationType;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PushNotificationDAO {
    private final MongoTemplate mongoTemplate;
    private final MongoUtil mongoUtil;

    public long count(
            PushNotificationType type,
            PushNotificationSendType sendType,
            PushNotificationReceiverType receiverType,
            String receiver,
            Boolean sendResult,
            LocalDate startDate,
            LocalDate endDate
    ) {
        return mongoTemplate.count(buildQuery(type, sendType, receiverType, receiver, sendResult, startDate, endDate), PushNotification.class);
    }

    public List<PushNotification> list(
            PushNotificationType type,
            PushNotificationSendType sendType,
            PushNotificationReceiverType receiverType,
            String receiver,
            Boolean sendResult,
            LocalDate startDate,
            LocalDate endDate,
            PageRequest pageRequest
    ) {
        Query query = buildQuery(type, sendType, receiverType, receiver, sendResult, startDate, endDate);
        if (pageRequest == null) {
            query = query.with(PageRequest.of(0, 10, Sort.Direction.DESC, "id"));
        } else {
            query = query.with(pageRequest);
        }
        return mongoTemplate.find(query, PushNotification.class);
    }

    private Query buildQuery(
            PushNotificationType type,
            PushNotificationSendType sendType,
            PushNotificationReceiverType receiverType,
            String receiver,
            Boolean sendResult,
            LocalDate startDate,
            LocalDate endDate
    ) {
        Query query = new Query();

        mongoUtil.queryIs(query, type, "type");
        mongoUtil.queryIs(query, sendType, "sendType");
        mongoUtil.queryIs(query, receiverType, "receiverType");
        mongoUtil.queryRegex(query, receiver, "receiver");
        mongoUtil.queryIs(query, sendResult, "sendResult");

        if (startDate != null && endDate != null) {
            query.addCriteria(
                    new Criteria().andOperator(
                            Criteria.where("sentDate").gte(startDate.atTime(LocalTime.MIN)),
                            Criteria.where("sentDate").lte(endDate.atTime(LocalTime.MAX))
                    )
            );
        }

        return query;
    }

    public long countForCustomer(
            String username,
            PushNotificationType type,
            Boolean read
    ) {
        return mongoTemplate.count(buildQueryForCustomer(username, type, read), PushNotification.class);
    }

    public List<PushNotification> listForCustomer(
            String username,
            PushNotificationType type,
            Boolean read,
            PageRequest pageRequest
    ) {
        Query query = buildQueryForCustomer(username, type, read);
        if (pageRequest == null) {
            query = query.with(PageRequest.of(0, 10, Sort.Direction.DESC, "id"));
        } else {
            query = query.with(pageRequest);
        }
        return mongoTemplate.find(query, PushNotification.class);
    }

    private Query buildQueryForCustomer(
            String username,
            PushNotificationType type,
            Boolean read
    ) {
        Query query = new Query();
        query.addCriteria(Criteria.where("receiverType").is(PushNotificationReceiverType.USERNAME));
        query.addCriteria(Criteria.where("receiver").is(username.toLowerCase()));

        if (type != null) {
            query.addCriteria(Criteria.where("type").is(type));
        }

        if (read != null) {
            query.addCriteria(Criteria.where("read").is(read));
        }

        return query;
    }

    public void incBatchCountSent(String id, int successCount, int failureCount) {
        mongoTemplate.findAndModify(
                new Query().addCriteria(Criteria.where("id").is(id)),
                new Update()
                        .inc("batchCountSent", 1)
                        .inc("successCount", successCount)
                        .inc("failureCount", failureCount),
                PushNotification.class);
    }

    public void incCountSent(String id, int successCount, int failureCount) {
        mongoTemplate.updateMulti(
                new Query().addCriteria(Criteria.where("id").is(id)),
                new Update()
                        .inc("successCount", successCount)
                        .inc("failureCount", failureCount),
                PushNotification.class);
    }

    public void setCompleted(String id) {
        mongoTemplate.updateMulti(
                new Query().addCriteria(Criteria.where("id").is(id)),
                new Update()
                        .set("sendResult", true)
                        .set("sentDate", LocalDateTime.now()),
                PushNotification.class);
    }
}
