package mn.delivery.system.repository.mobile;

import mn.delivery.system.model.mobile.PushNotification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author digz6
 */
@Repository
public interface PushNotificationRepository extends MongoRepository<PushNotification, String> {

    @Nullable
    PushNotification findByIdAndReceiver(String id, String receiver);

    @Query("{'sendType': 'CRON', 'sendResult': null, 'scheduledDate': {$lt: ?0}}")
    List<PushNotification> findForSend(LocalDateTime date, Pageable pageable);

    @Query("{'sendType': 'CRON', 'sendResult': null, 'scheduledDate': {$gte: ?0}, 'deleted': false}")
    List<PushNotification> findScheduled(LocalDateTime date, Pageable pageable);

    @Query("{'sendType': 'DIRECT', 'sendResult': null, 'deleted': false}")
    List<PushNotification> findDirect(Pageable pageable);

}
