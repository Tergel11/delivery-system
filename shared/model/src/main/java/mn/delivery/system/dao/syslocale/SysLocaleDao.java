package mn.delivery.system.dao.syslocale;

import lombok.RequiredArgsConstructor;
import mn.delivery.system.model.syslocale.SysLocale;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

@RequiredArgsConstructor
@Repository
public class SysLocaleDao {

    private final MongoTemplate mongoTemplate;

    public long count(String nsId, String lng, String field, String translation, Boolean active) {
        return mongoTemplate.count(buildQuery(nsId, lng, field, translation, active), SysLocale.class);
    }

    public Iterable<SysLocale> list(String nsId, String lng, String field, String translation, Boolean active, Pageable pageable) {
        Query query = buildQuery(nsId, lng, field, translation, active);
        if (pageable != null) {
            query = query.with(pageable);
        }
        return mongoTemplate.find(query, SysLocale.class);
    }

    private Query buildQuery(String nsId, String lng, String field, String translation, Boolean active) {
        Query query = new Query();

        if (!ObjectUtils.isEmpty(nsId))
            query.addCriteria(Criteria.where("nsId").is(nsId));

        if (!ObjectUtils.isEmpty(lng))
            query.addCriteria(Criteria.where("lng").is(lng));

        if (!ObjectUtils.isEmpty(field))
            query.addCriteria(Criteria.where("field").regex(field, "i"));
        
        if (!ObjectUtils.isEmpty(field))
            query.addCriteria(Criteria.where("translation").is(translation));

        if (!ObjectUtils.isEmpty(active))
            query.addCriteria(Criteria.where("active").is(active));

        query.addCriteria(Criteria.where("deleted").is(false));

        return query;
    }
}

