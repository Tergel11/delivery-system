package mn.delivery.system.dao.email;

import lombok.RequiredArgsConstructor;
import mn.delivery.system.database.util.MongoUtil;
import mn.delivery.system.model.email.EmailRequest;
import mn.delivery.system.model.email.enums.EmailReceiverType;
import mn.delivery.system.model.email.enums.EmailSendType;
import mn.delivery.system.model.email.enums.EmailType;
import mn.delivery.system.model.mobile.PushNotification;
import org.springframework.data.domain.Pageable;
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
public class EmailRequestDao {

    private final MongoTemplate mongoTemplate;
    private final MongoUtil mongoUtil;

    public long count(
            EmailType type,
            EmailSendType sendType,
            EmailReceiverType receiverType,
            String receiver,
            Boolean sendResult,
            LocalDate startDate,
            LocalDate endDate) {
        return mongoTemplate.count(
                buildQuery(type, sendType, receiverType, receiver, sendResult, startDate, endDate),
                EmailRequest.class);
    }

    public List<EmailRequest> list(
            EmailType type,
            EmailSendType sendType,
            EmailReceiverType receiverType,
            String receiver,
            Boolean sendResult,
            LocalDate startDate,
            LocalDate endDate,
            Pageable pageable) {
        Query query = buildQuery(type, sendType, receiverType, receiver, sendResult, startDate, endDate);
        mongoUtil.queryPageable(query, pageable);
        return mongoTemplate.find(query, EmailRequest.class);
    }

    private Query buildQuery(
            EmailType type,
            EmailSendType sendType,
            EmailReceiverType receiverType,
            String receiver,
            Boolean sendResult,
            LocalDate startDate,
            LocalDate endDate) {
        Query query = new Query();

        mongoUtil.queryIs(query, type, "type");
        mongoUtil.queryIs(query, sendType, "sendType");
        mongoUtil.queryIs(query, receiverType, "receiverType");
        // mongoUtil.queryRegex(query, receiver, "receiver");
        mongoUtil.queryIs(query, sendResult, "sendResult");

        if (startDate != null && endDate != null) {
            query.addCriteria(
                    new Criteria().andOperator(
                            Criteria.where("sentDate").gte(startDate.atTime(LocalTime.MIN)),
                            Criteria.where("sentDate").lte(endDate.atTime(LocalTime.MAX))));
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
