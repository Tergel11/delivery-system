package mn.delivery.system.dao.location;

import lombok.RequiredArgsConstructor;
import mn.delivery.system.model.location.Location;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class LocationDao {

    private final MongoTemplate mongoTemplate;

    public List<Location> listCity() {
        Query query = new Query();
        query.addCriteria(Criteria.where("parentCode").isNull());
        query
                .with(Sort.by(Sort.Direction.DESC, "order"))
                .with(Sort.by(Sort.Direction.ASC, "name"));

        return mongoTemplate.find(query, Location.class);
    }

    public List<Location> listByParentCode(String parentCode) {
        Query query = new Query();
        query.addCriteria(Criteria.where("parentCode").is(parentCode));
        query.with(Sort.by(Sort.Direction.ASC, "name"));

        return mongoTemplate.find(query, Location.class);
    }
}
