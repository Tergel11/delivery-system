package mn.delivery.system.service.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mn.delivery.system.model.notification.Notification;
import mn.delivery.system.model.notification.enums.NotificationStatus;
import mn.delivery.system.database.util.MongoUtil;
import mn.delivery.system.exception.notification.NotificationException;
import mn.delivery.system.repository.notification.NotificationRepository;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author digz6666
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {
    private final MongoUtil mongoUtil;

    private final NotificationRepository notificationRepository;

    public Notification get(String id) throws NotificationException {
        Notification notif = notificationRepository.findByIdAndDeletedFalse(id);
        if (notif == null) {
            throw new NotificationException("Өгөгдөл олдсонгүй");
        }

        return notif;
    }

    public Notification read(String notifId, String userId) throws NotificationException {
        Notification notif = notificationRepository.findByIdAndDeletedFalse(notifId);
        if (notif == null || !notif.getUserId().equals(userId)) {
            throw new NotificationException("Өгөгдөл олдсонгүй");
        }

        if (notif.getStatus().equals(NotificationStatus.UNREAD)) {
            throw new NotificationException("Мэдэгдлийн төлөв буруу байна");
        }

        Map<NotificationStatus, LocalDateTime> statusMap = new HashMap<NotificationStatus, LocalDateTime>();
        if (!ObjectUtils.isEmpty(notif.getStatusDate())) {
            statusMap = notif.getStatusDate();
        }

        notif = mongoUtil.findAndModify(
                new Query()
                        .addCriteria(Criteria.where("id").is(notifId)),
                new Update()
                        .set("status", NotificationStatus.READ)
                        .set("statusDate", statusMap.put(NotificationStatus.READ, LocalDateTime.now()))
                        .set("modifiedDate", LocalDateTime.now()),
                MongoUtil.FM_OPTIONS,
                Notification.class);
        if (notif == null) {
            throw new NotificationException("Өгөгдөл хадгалах үед алдаа гарлаа");
        }

        return notif;
    }

    public void clean(String userId) {
        List<Notification> readNotifications = notificationRepository.findAllByUserIdAndStatus(userId,
                NotificationStatus.READ.toString());

        for (Notification notification : readNotifications) {

            Map<NotificationStatus, LocalDateTime> statusMap = new HashMap<NotificationStatus, LocalDateTime>();
            if (!ObjectUtils.isEmpty(notification.getStatusDate())) {
                statusMap = notification.getStatusDate();
            }

            mongoUtil.findAndModify(
                    new Query()
                            .addCriteria(Criteria.where("id").is(notification.getId())),
                    new Update()
                            .set("deleted", true)
                            .set("status", NotificationStatus.DELETED)
                            .set("statusDate", statusMap.put(NotificationStatus.DELETED, LocalDateTime.now()))
                            .set("modifiedDate", LocalDateTime.now()),
                    MongoUtil.FM_OPTIONS,
                    Notification.class);
        }
    }
}
