package mn.delivery.system.dao.merchant;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mn.delivery.system.model.merchant.Merchant;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MerchantDao {

    private final MongoTemplate mongoTemplate;

    public long count(String userId, String name, String bundleId, String wareHouseId) {
        return mongoTemplate.count(buildQuery(userId, name, bundleId, wareHouseId, null), Merchant.class);
    }

    public List<Merchant> list(String userId, String name, String bundleId, String wareHouseId, Pageable pageable) {
        return mongoTemplate.find(buildQuery(userId, name, bundleId, wareHouseId, pageable), Merchant.class);
    }

    private Query buildQuery(String userId, String name, String bundleId, String wareHouseId, Pageable pageable) {

        Query query = new Query();
        if (!ObjectUtils.isEmpty(userId))
            query.addCriteria(Criteria.where("userId").is(userId));

        if (!ObjectUtils.isEmpty(name))
            query.addCriteria(Criteria.where("name").regex(name, "i"));

        if (!ObjectUtils.isEmpty(bundleId))
            query.addCriteria(Criteria.where("bundleId").is(bundleId));

        if (!ObjectUtils.isEmpty(wareHouseId))
            query.addCriteria(Criteria.where("wareHouseId").is(wareHouseId));
        if (!ObjectUtils.isEmpty(pageable))
            query.with(pageable);

        query.addCriteria(Criteria.where("deleted").is(false));
        return query;
    }
}
