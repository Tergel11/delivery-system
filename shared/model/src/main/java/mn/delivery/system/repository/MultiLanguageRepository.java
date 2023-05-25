package mn.delivery.system.repository;

import mn.delivery.system.model.MultiLanguage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Odko
 */
@Repository
public interface MultiLanguageRepository extends MongoRepository<MultiLanguage, String> {
}
