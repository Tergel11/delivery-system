package mn.delivery.system.repository.mobile;

import mn.delivery.system.model.mobile.DeviceToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author digz6
 */
@Repository
public interface DeviceTokenRepository extends MongoRepository<DeviceToken, String> {

    List<DeviceToken> findByEmail(String email);

    @Nullable
    DeviceToken findByDeviceIdAndEmail(String deviceId, String email);

    @Nullable
    DeviceToken findByTokenAndOsAndEmail(String token, String os, String email);

    @Nullable
    DeviceToken findByTokenAndOs(String token, String os);

    @Nullable
    DeviceToken findTop1ByTokenOrderByIdDesc(String token);

    @Query(value = "{}", fields = "{'token' : 1}")
    Stream<DeviceToken> findTokensOnly();

    @Query("{email: {$ne: null}, 'createdDate': {$gt: ?0}}")
    List<DeviceToken> findCreatedAfter(LocalDateTime createdAfter);

    void deleteByToken(String token);

    void deleteByDeviceId(String deviceId);

    void deleteByTokenAndOs(String token, String os);

}
