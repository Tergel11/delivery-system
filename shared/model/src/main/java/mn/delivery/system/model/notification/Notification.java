package mn.delivery.system.model.notification;

import java.time.LocalDateTime;
import java.util.Map;

import mn.delivery.system.model.notification.enums.NotificationRelatedDataType;
import mn.delivery.system.model.notification.enums.NotificationStatus;
import mn.delivery.system.model.BaseEntityWithUser;
import org.springframework.data.mongodb.core.mapping.Sharded;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Sharded(shardKey = { "id", "userId" })
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification extends BaseEntityWithUser {
    private String userId;
    private NotificationRelatedDataType relatedDataType;
    private String relatedDataId;

    private String title;
    private String message;

    @Default
    private NotificationStatus status = NotificationStatus.UNREAD;
    private Map<NotificationStatus, LocalDateTime> statusDate;
}
