package mn.delivery.system.repository.auth;

import mn.delivery.system.model.auth.UserLoginHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author digz6
 */
@Repository
public interface UserLoginHistoryRepository extends MongoRepository<UserLoginHistory, String> {
}
