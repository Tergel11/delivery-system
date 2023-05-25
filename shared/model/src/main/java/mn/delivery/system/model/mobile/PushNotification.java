package mn.delivery.system.model.mobile;

import lombok.*;
import mn.delivery.system.model.mobile.enums.PushNotificationReceiverType;
import mn.delivery.system.model.mobile.enums.PushNotificationSendType;
import mn.delivery.system.model.mobile.enums.PushNotificationType;
import mn.delivery.system.model.BaseEntityWithUser;
import mn.delivery.system.constants.GlobalDateFormat;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Sharded;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Sharded(shardKey = {"id"})
public class PushNotification extends BaseEntityWithUser {

    public static final int PRIORITY_HIGH = 100;
    public static final int PRIORITY_NORMAL = 50;
    public static final int PRIORITY_LOW = 0;

    private PushNotificationType type; // DEFAULT, SERVICE, PAYMENT, NEWS, EMERGENCY
    @NotBlank
    private String title; // гарчиг
    @NotBlank
    private String body; // агуулга
    private Map<String, String> data;
    private int priority;
    @NotNull
    private PushNotificationSendType sendType; // DIRECT, CRON
    private LocalDateTime scheduledDate;
    @NotNull
    private PushNotificationReceiverType receiverType; // USERNAME, TOKEN, ALL
    private String receiver; // username or token

    private long batchCount; // багцын тоо (500 500 аар хуваасан)
    private long batchCountSent; // илгээсэн багцийн тоо

    private Boolean sendResult; // илгээсэн эсэх
    private int successCount;
    private int failureCount;
    private String resultMessage; // илгээхэд гарсан алдааны мессеж
    private LocalDateTime sentDate; // илгээсэн огноо

    private boolean read; // уншсан эсэх

    @Transient
    public String getSentDateStr() {
        if (sentDate != null) {
            return GlobalDateFormat.DATE_TIME_ONLY.format(sentDate);
        } else {
            return null;
        }
    }
}
