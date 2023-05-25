package mn.delivery.system.repository.sequence;

import mn.delivery.system.model.sequence.SequenceNumber;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SequenceNumberRepository extends MongoRepository<SequenceNumber, String> {

}
