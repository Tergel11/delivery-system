package mn.delivery.system.dao.notification;

import java.util.List;

import mn.delivery.system.model.notification.Notification;
import mn.delivery.system.model.notification.enums.NotificationRelatedDataType;
import mn.delivery.system.model.notification.enums.NotificationStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@RequiredArgsConstructor
public class NotificationDao {

    private final MongoTemplate mongoTemplate;

    public long count(
            String userId,
            NotificationRelatedDataType dataType,
            NotificationStatus status) {
        Query query = buildQuery(
                userId, dataType, status,
                null);
        return mongoTemplate.count(query, Notification.class);
    }

    public List<Notification> list(
            String userId,
            NotificationRelatedDataType dataType,
            NotificationStatus status,
            Pageable pageable) {
        Query query = buildQuery(
                userId, dataType, status,
                pageable);
        return mongoTemplate.find(query, Notification.class);
    }

    private Query buildQuery(
            String userId,
            NotificationRelatedDataType dataType,
            NotificationStatus status,
            Pageable pageable) {
        Query query = new Query();

        if (!ObjectUtils.isEmpty(status)) {
            query.addCriteria(Criteria.where("status").is(status));
        }

        if (!ObjectUtils.isEmpty(userId)) {
            query.addCriteria(Criteria.where("userId").is(userId));
        }

        if (!ObjectUtils.isEmpty(dataType)) {
            query.addCriteria(Criteria.where("relatedDataType").is(dataType));
        }

        query.addCriteria(Criteria.where("deleted").is(false));

        if (pageable != null) {
            query.with(pageable);
        }

        return query;
    }
}
