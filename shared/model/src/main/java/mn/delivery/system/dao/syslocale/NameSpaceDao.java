package mn.delivery.system.dao.syslocale;

import lombok.RequiredArgsConstructor;
import mn.delivery.system.model.syslocale.NameSpace;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

@RequiredArgsConstructor
@Repository
public class NameSpaceDao {

    private final MongoTemplate mongoTemplate;

    public long count(String name, String value, Boolean active) {
        return mongoTemplate.count(buildQuery(name, value, active), NameSpace.class);
    }

    public Iterable<NameSpace> list(String name, String value, Boolean active, Pageable pageable) {
        Query query = buildQuery(name, value, active);
        if (pageable != null) {
            query = query.with(pageable);
        }
        return mongoTemplate.find(query, NameSpace.class);
    }

    private Query buildQuery(String name, String value, Boolean active) {
        Query query = new Query();


        if (!ObjectUtils.isEmpty(name))
            query.addCriteria(Criteria.where("name").regex(name, "i"));
        
        if (!ObjectUtils.isEmpty(value))
            query.addCriteria(Criteria.where("value").is(value));

        if (!ObjectUtils.isEmpty(active))
            query.addCriteria(Criteria.where("active").is(active));

        query.addCriteria(Criteria.where("deleted").is(false));

        return query;
    }
}

