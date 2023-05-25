package mn.delivery.system.repository.syslocale;

import mn.delivery.system.model.syslocale.NameSpace;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NameSpaceRepository extends MongoRepository<NameSpace, String> {

    boolean existsByIdAndDeletedFalse(String id);

    boolean existsByNameAndDeletedFalse(String name);

    boolean existsByValueAndDeletedFalse(String name);

    Optional<NameSpace> findByValue(String value);

}
