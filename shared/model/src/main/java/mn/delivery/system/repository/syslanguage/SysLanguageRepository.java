package mn.delivery.system.repository.syslanguage;

import mn.delivery.system.model.syslanguage.SysLanguage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SysLanguageRepository extends MongoRepository<SysLanguage, String> {


    boolean existsByCodeAndDeletedFalse(String code);

    boolean existsByIdAndDeletedFalse(String id);

    boolean existsByNameAndDeletedFalse(String name);

    boolean existsByOrder(Integer order);

    List<SysLanguage> findAllByOrderGreaterThan(Integer order);

    Optional<SysLanguage> findByCode(String code);
}
