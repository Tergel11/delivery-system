package mn.delivery.system.repository.mobile;

import mn.delivery.system.model.mobile.PushNotificationResult;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author digz6
 */
@Repository
public interface PushNotificationResultRepository extends MongoRepository<PushNotificationResult, String> {
}
