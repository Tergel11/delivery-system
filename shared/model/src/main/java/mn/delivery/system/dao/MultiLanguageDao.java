package mn.delivery.system.dao;

import mn.delivery.system.model.MultiLanguage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class MultiLanguageDao {


    private final MongoTemplate mongoTemplate;

    public long count(String code,String app, String name,String nomenclatureMN,String nomenclatureEN, Integer order) {
        return mongoTemplate.count(buildQuery(code, app, name, nomenclatureMN, nomenclatureEN, order), MultiLanguage.class);
    }

    public Iterable<MultiLanguage> list(String code,String app, String name,String nomenclatureMN,String nomenclatureEN, Integer order, Pageable pageable) {
        Query query = buildQuery(code, app, name, nomenclatureMN, nomenclatureEN, order);
        if (pageable != null) {
            query = query.with(pageable);
        }
        return mongoTemplate.find(query, MultiLanguage.class);
    }

    private Query buildQuery(String code,String app, String name,String nomenclatureMN,String nomenclatureEN, Integer order) {
        Query query = new Query();

        if (!ObjectUtils.isEmpty(code)) {
            query.addCriteria(Criteria.where("code").regex(code, "i"));
        }
        if (!ObjectUtils.isEmpty(app)) {
            query.addCriteria(Criteria.where("app").regex(app, "i"));
        }
        if (!ObjectUtils.isEmpty(name)) {
            query.addCriteria(Criteria.where("name").regex(name, "i"));
        }
        if (!ObjectUtils.isEmpty(nomenclatureMN)) {
            query.addCriteria(Criteria.where("nomenclatureMN").regex(nomenclatureMN, "i"));
        }
        if (!ObjectUtils.isEmpty(nomenclatureEN)) {
            query.addCriteria(Criteria.where("nomenclatureEN").regex(nomenclatureEN, "i"));
        }

        query.addCriteria(Criteria.where("deleted").is(false));

        return query;
    }
}
