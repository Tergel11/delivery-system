package mn.delivery.system.repository.email;

import java.util.List;

import mn.delivery.system.model.email.EmailConfirmation;
import mn.delivery.system.model.email.enums.ConfirmationType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

/**
 * @author digz6666
 */
@Repository
public interface EmailConfirmationRepository extends MongoRepository<EmailConfirmation, String> {

    @Nullable
    EmailConfirmation findByTokenAndTypeAndDeletedFalse(String token, ConfirmationType type);

    List<EmailConfirmation> findByEmailAndTypeAndDeletedFalse(String email, ConfirmationType type);

    @Nullable
    EmailConfirmation findByTokenAndType(String token, ConfirmationType verifyEmail);
}
