package mn.delivery.system.repository.auth;

import mn.delivery.system.model.auth.UserCitizenData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

/**
 * @author digz6666
 */
@Repository
public interface UserCitizenDataRepository extends MongoRepository<UserCitizenData, String> {

    boolean existsByUserIdAndRegistryNumber(String userId, String registryNumber);

    @Nullable
    UserCitizenData findByUserIdAndRegistryNumber(String userId, String registryNumber);

    boolean existsByUserId(String userId);
}
