package mn.delivery.system.model.sso;

import lombok.Data;
import lombok.EqualsAndHashCode;
import mn.delivery.system.model.BaseEntity;
import org.springframework.data.mongodb.core.mapping.Sharded;

import java.util.Map;

/**
 * ДАН-аар нэвтэрсэн хүсэлтийн лог
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Sharded(shardKey = {"id"})
public class SsoRequestLog extends BaseEntity {

    private String uuid;
    private String userId;
    private String regnum;

    private Map<String, Integer> xypResultCodes;
}
