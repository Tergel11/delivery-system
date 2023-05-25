package mn.delivery.system.repository.email;

import java.time.LocalDateTime;
import java.util.List;

import mn.delivery.system.database.annotations.AllowDiskUse;
import mn.delivery.system.model.email.EmailRequest;
import mn.delivery.system.model.email.enums.ConfirmationType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

/**
 * @author Tergel
 */
@Repository
public interface EmailRequestRepository extends MongoRepository<EmailRequest, String> {

    @Nullable
    @AllowDiskUse
    @Query(value = "{}", sort = "{createdDate: -1}")
    EmailRequest findTop1ByToAndResultTrueAndConfirmationEmailTrueAndConfirmationType(String email,
            ConfirmationType type);

    @Query("{'sendType': 'SCHEDULED', 'sendResult': null, 'scheduledDate': {$gte: ?0}, 'deleted': false}")
    List<EmailRequest> findScheduled(LocalDateTime date, Pageable pageable);

    @Query("{'sendType': 'DIRECT', 'sendResult': null, 'deleted': false}")
    List<EmailRequest> findDirect(Pageable pageable);

}
