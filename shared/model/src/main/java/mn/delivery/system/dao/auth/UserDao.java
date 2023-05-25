package mn.delivery.system.dao.auth;

import lombok.RequiredArgsConstructor;
import mn.delivery.system.database.util.MongoUtil;
import mn.delivery.system.model.auth.User;
import mn.delivery.system.repository.auth.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * @author Tergel
 */
@Repository
@RequiredArgsConstructor
public class UserDao {
    private final MongoTemplate mongoTemplate;
    private final MongoUtil mongoUtil;
    private final UserRepository userRepository;

    public long count(
            String role,
            String search,
            Boolean using2fa,
            Boolean emailVerified,
            Boolean phoneVerified,
            Boolean active,
            Boolean deleted) {
        return mongoTemplate.count(buildPredicate(
                role,
                search,
                using2fa,
                emailVerified,
                phoneVerified,
                active,
                deleted,
                null), User.class);
    }

    public List<User> list(
            String role,
            String search,
            Boolean using2fa,
            Boolean emailVerified,
            Boolean phoneVerified,
            Boolean active,
            Boolean deleted,
            Pageable pageable) {
        return mongoTemplate.find(buildPredicate(
                role,
                search,
                using2fa,
                emailVerified,
                phoneVerified,
                active,
                deleted,
                pageable), User.class);
    }

    private Query buildPredicate(
            String role,
            String search,
            Boolean using2fa,
            Boolean emailVerified,
            Boolean phoneVerified,
            Boolean active,
            Boolean deleted,
            Pageable pageable) {
        Query query = new Query();

        mongoUtil.queryIs(query, role, "role");
        mongoUtil.queryIs(query, active, "active");
        mongoUtil.queryIs(query, using2fa, "using2fa");
        mongoUtil.queryIs(query, emailVerified, "emailVerified");
        mongoUtil.queryIs(query, phoneVerified, "phoneVerified");
        mongoUtil.queryDeleted(query, deleted);

        if (!ObjectUtils.isEmpty(search)) {
            query.addCriteria(new Criteria().orOperator(
                    Criteria.where("email").regex(search, "i"),
                    Criteria.where("registryNumber").regex(search, "i"),
                    Criteria.where("lastName").regex(search, "i"),
                    Criteria.where("firstName").regex(search, "i"),
                    Criteria.where("mobile").regex(search, "i")
            ));
        }

        mongoUtil.queryPageable(query, pageable);

        return query;
    }

    public Map<String, User> map(List<String> ids) {
        Map<String, User> userMap = new HashMap<>();

        List<User> users = userRepository.findAllByIdIn(ids);
        for (User user : users) {
            userMap.put(user.getId(), user);
        }

        return userMap;
    }

    public List<String> listIds(String search) {
        List<String> userIds = new ArrayList<>();
        if (ObjectUtils.isEmpty(search))
            return userIds;

        Query query = new Query();
        query.addCriteria(where("deleted").is(false));
        query.addCriteria(new Criteria().orOperator(
                Criteria.where("email").regex(search, "i"),
                Criteria.where("registryNumber").regex(search, "i"),
                Criteria.where("lastName").regex(search, "i"),
                Criteria.where("firstName").regex(search, "i"),
                Criteria.where("mobile").regex(search, "i")
        ));
        query.fields().include("id");

        List<User> users = mongoTemplate.find(query, User.class);
        for (User user : users)
            userIds.add(user.getId());

        return userIds;
    }

    public User getInternalApi() {
        List<User> users = userRepository.findInternalApi(PageRequest.of(0, 1));
        return users.size() > 0 ? users.get(0) : null;
    }
}
