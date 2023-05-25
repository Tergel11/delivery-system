package mn.delivery.system.dao.reference;

import lombok.RequiredArgsConstructor;
import mn.delivery.system.model.reference.ReferenceData;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

@RequiredArgsConstructor
@Repository
public class ReferenceDataDao {

    private final MongoTemplate mongoTemplate;

    public long count(String typeCode,String name,String typeMean,String typeShortMean, String description) {
        return mongoTemplate.count(buildQuery(typeCode,name,typeMean,typeShortMean, description), ReferenceData.class);
    }

    public Iterable<ReferenceData> list(String typeCode, String name, String typeMean, String typeShortMean, String description, Pageable pageable) {
        Query query = buildQuery(typeCode,name,typeMean,typeShortMean, description);
        if (pageable != null) {
            query = query.with(pageable);
        }
        return mongoTemplate.find(query, ReferenceData.class);
    }

    private Query buildQuery(String typeCode,String name,String typeMean,String typeShortMean, String description) {
        Query query = new Query();

        if (!ObjectUtils.isEmpty(typeCode)) {
            query.addCriteria(Criteria.where("typeCode").regex(typeCode, "i"));
        }
        if (!ObjectUtils.isEmpty(name)) {
            query.addCriteria(Criteria.where("name").and("mn").regex(name, "i"));
        }
        if (!ObjectUtils.isEmpty(typeMean)) {
            query.addCriteria(Criteria.where("typeMean").regex(typeMean, "i"));
        }
        if (!ObjectUtils.isEmpty(typeShortMean)) {
            query.addCriteria(Criteria.where("typeShortMean").regex(typeShortMean, "i"));
        }
        if (!ObjectUtils.isEmpty(description)) {
            query.addCriteria(Criteria.where("description").regex(description, "i"));
        }

        query.addCriteria(Criteria.where("deleted").is(false));

        return query;
    }
}
