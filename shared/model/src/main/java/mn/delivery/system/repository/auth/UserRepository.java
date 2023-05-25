package mn.delivery.system.repository.auth;

import mn.delivery.system.model.auth.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author digz6666
 */
@Repository
public interface UserRepository extends MongoRepository<User, String> {

    @Nullable
    User findByEmailAndDeletedFalse(String email);

    @Nullable
    @Query("{'email': ?0, 'deleted': false}")
    User findByEmail(String email);

    @Nullable
    User findByIdAndDeletedFalse(String id);

    boolean existsByEmailAndDeletedFalse(String email);

    boolean existsByEmailAndIdNotAndDeletedFalse(String email, String id);

    @Nullable
    @Query("{'email': {$regex: /?0/i}, 'deleted': false}")
    List<User> findAllByEmail(String email);

    @Query("{'_id': {$in: ?0}, 'deleted': false}")
    List<User> findAllByIdIn(List<String> ids);

    @NonNull
    @Query("{'deleted': false}")
    List<User> findAll();

    @Query("{'businessRole': 'INTERNAL_API', 'deleted': false, 'systemAccount': true}")
    List<User> findInternalApi(Pageable pageable);
}
