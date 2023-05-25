package mn.delivery.system.repository.sso;

import mn.delivery.system.model.sso.SsoRequestLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SsoRequestLogRepository extends MongoRepository<SsoRequestLog, String> {
}
