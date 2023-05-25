package mn.delivery.system.repository.systemconfig;

import java.util.Optional;

import mn.delivery.system.model.systemconfig.SystemApiConf;
import mn.delivery.system.model.systemconfig.enums.SystemApiModuleType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemApiConfRepository extends
    MongoRepository<SystemApiConf, SystemApiModuleType> {

    Optional<SystemApiConf> findByModuleType(SystemApiModuleType moduleType);
}
