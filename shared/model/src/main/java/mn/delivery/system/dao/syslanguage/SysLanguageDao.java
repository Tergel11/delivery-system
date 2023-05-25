package mn.delivery.system.dao.syslanguage;

import lombok.RequiredArgsConstructor;
import mn.delivery.system.model.syslanguage.SysLanguage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

@RequiredArgsConstructor
@Repository
public class SysLanguageDao {

    private final MongoTemplate mongoTemplate;

    public long count(String name, String code, Boolean active) {
        return mongoTemplate.count(buildQuery(name, code, active), SysLanguage.class);
    }

    public Iterable<SysLanguage> list(String name, String code, Boolean active, Pageable pageable) {
        Query query = buildQuery(name, code, active);
        if (pageable != null) {
            query = query.with(pageable);
        }
        return mongoTemplate.find(query, SysLanguage.class);
    }

    private Query buildQuery(String name, String code, Boolean active) {
        Query query = new Query();


        if (!ObjectUtils.isEmpty(name))
            query.addCriteria(Criteria.where("name").regex(name, "i"));

        if (!ObjectUtils.isEmpty(code))
            query.addCriteria(Criteria.where("code").is(code));

        if (!ObjectUtils.isEmpty(active))
            query.addCriteria(Criteria.where("active").is(active));

        query.addCriteria(Criteria.where("deleted").is(false));

        return query;
    }
}

