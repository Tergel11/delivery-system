package mn.delivery.system.repository.merchant;

import mn.delivery.system.model.merchant.Merchant;
import org.springframework.data.mongodb.core.mapping.Unwrapped.Nullable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MerchantRepository extends MongoRepository<Merchant, String> {
    @Nullable
    Merchant findByNameAndDeletedFalse(String name);
    @Nullable
    Merchant findByIdAndDeletedFalse(String id);
}
