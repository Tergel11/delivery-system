package mn.delivery.system.repository.auth;

import mn.delivery.system.model.auth.BusinessRole;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author digz6666
 */
@Repository
public interface BusinessRoleRepository extends MongoRepository<BusinessRole, String> {
}
