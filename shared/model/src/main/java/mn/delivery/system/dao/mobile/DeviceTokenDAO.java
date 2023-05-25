package mn.delivery.system.dao.mobile;

import lombok.RequiredArgsConstructor;
import mn.delivery.system.database.util.MongoUtil;
import mn.delivery.system.model.mobile.DeviceToken;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class DeviceTokenDAO {
    private final MongoTemplate mongoTemplate;
    private final MongoUtil mongoUtil;

    public long count(
            String os,
            String token,
            String deviceId,
            String email,
            String ip
    ) {
        return mongoTemplate.count(buildQuery(os, token, deviceId, email, ip), DeviceToken.class);
    }

    public List<DeviceToken> list(
            String os,
            String token,
            String deviceId,
            String email,
            String ip,
            PageRequest pageRequest
    ) {
        Query query = buildQuery(os, token, deviceId, email, ip);
        if (pageRequest == null)
            query = query.with(PageRequest.of(0, 10, Sort.Direction.DESC, "id"));
        else
            query = query.with(pageRequest);

        return mongoTemplate.find(query, DeviceToken.class);
    }

    private Query buildQuery(
            String os,
            String token,
            String deviceId,
            String email,
            String ip
    ) {
        Query query = new Query();

        mongoUtil.queryIs(query, os, "os");
        mongoUtil.queryRegex(query, token, "token");
        mongoUtil.queryIs(query, deviceId, "deviceId");
        mongoUtil.queryRegex(query, email, "email");
        mongoUtil.queryIs(query, ip, "ip");

        return query;
    }
}
