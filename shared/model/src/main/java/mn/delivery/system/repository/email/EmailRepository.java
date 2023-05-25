package mn.delivery.system.repository.email;

import mn.delivery.system.database.annotations.AllowDiskUse;
import mn.delivery.system.model.email.Email;
import mn.delivery.system.model.email.enums.ConfirmationType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

/**
 * @author digz6666
 */
@Repository
public interface EmailRepository extends MongoRepository<Email, String> {

    @Nullable
    @AllowDiskUse
    @Query(sort = "{createdDate: -1}")
    Email findTop1ByToAndResultTrueAndConfirmationEmailTrueAndConfirmationType(String email, ConfirmationType type);
}
