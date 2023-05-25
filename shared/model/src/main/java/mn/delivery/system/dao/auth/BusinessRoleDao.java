package mn.delivery.system.dao.auth;

import lombok.RequiredArgsConstructor;
import mn.delivery.system.database.util.MongoUtil;
import mn.delivery.system.model.auth.BusinessRole;
import mn.delivery.system.model.auth.enums.ApplicationRole;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Tergel
 */
@Repository
@RequiredArgsConstructor
public class BusinessRoleDao {

    private final MongoTemplate mongoTemplate;
    private final MongoUtil mongoUtil;

    public long count(String role, ApplicationRole applicationRole, List<String> accessibleRoles, Boolean permitAllRoles) {
        return mongoTemplate.count(buildPredicate(role, applicationRole, accessibleRoles, permitAllRoles, null), BusinessRole.class);
    }

    public Iterable<BusinessRole> list(String role, ApplicationRole applicationRole, List<String> accessibleRoles, Boolean permitAllRoles, Pageable pageable) {
        return mongoTemplate.find(buildPredicate(role, applicationRole, accessibleRoles, permitAllRoles, pageable), BusinessRole.class);
    }

    private Query buildPredicate(String role, ApplicationRole applicationRole, List<String> accessibleRoles, Boolean permitAllRoles, Pageable pageable) {
        Query query = new Query();

        mongoUtil.queryIs(query, role, "role");
        mongoUtil.queryIs(query, applicationRole, "applicationRoles");

        if (!(permitAllRoles != null && permitAllRoles)) {
            if (accessibleRoles != null) {
                query.addCriteria(Criteria.where("role").in(accessibleRoles));
            } else {
                query.addCriteria(Criteria.where("role").is(""));
            }
        }
        mongoUtil.queryPageable(query, pageable);

        return query;
    }
}
