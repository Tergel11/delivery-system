package mn.delivery.system.repository.notification;

import java.util.List;

import mn.delivery.system.model.notification.Notification;
import org.springframework.data.mongodb.core.mapping.Unwrapped.Nullable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author digz6666
 */
@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {
    @Query(value = "{'userId': ?0, 'deleted': false}")
    List<Notification> findAllByUserId(String userId);

    @Query(value = "{'userId': ?0, 'status': ?1, 'deleted': false}")
    List<Notification> findAllByUserIdAndStatus(String userId, String status);

    @Query(value = "{'relatedDataType': ?0, 'deleted': false}")
    List<Notification> findAllByRelatedDataType(String relatedDataType);

    @Nullable
    Notification findByIdAndDeletedFalse(String id);
}
