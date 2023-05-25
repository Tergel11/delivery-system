package mn.delivery.system.api.mobile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mn.delivery.system.api.BaseController;
import mn.delivery.system.api.request.antd.AntdPagination;
import mn.delivery.system.api.response.antd.AntdTableDataList;
import mn.delivery.system.dao.mobile.PushNotificationDAO;
import mn.delivery.system.model.auth.User;
import mn.delivery.system.model.mobile.PushNotification;
import mn.delivery.system.model.mobile.enums.PushNotificationReceiverType;
import mn.delivery.system.model.mobile.enums.PushNotificationSendType;
import mn.delivery.system.model.mobile.enums.PushNotificationType;
import mn.delivery.system.repository.mobile.PushNotificationRepository;
import mn.delivery.system.exception.permission.PermissionException;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/v1/push-notification")
@Secured({"ROLE_MANAGE_DEFAULT"})
@RequiredArgsConstructor
public class PushNotificationApi extends BaseController {
    private final PushNotificationRepository pushNotificationRepository;
    private final PushNotificationDAO pushNotificationDAO;

    //    @Secured({"ROLE_PUSH_NOTIFICATION_VIEW", "ROLE_PUSH_NOTIFICATION_MANAGE"})
    @GetMapping
    public ResponseEntity<?> list(
            PushNotificationType type,
            PushNotificationSendType sendType,
            PushNotificationReceiverType receiverType,
            String receiver,
            Boolean sendResult,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate sentDate1,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate sentDate2,
            String sortField,
            String sortOrder,
            AntdPagination pagination
    ) {
        if (Objects.equals(sortOrder, "ascend"))
            pagination.setSortDirection(Sort.Direction.ASC);
        else
            pagination.setSortDirection(Sort.Direction.DESC);

        if (ObjectUtils.isEmpty(sortField))
            pagination.setSortParam("id");
        else
            pagination.setSortParam(sortField);

        try {
            AntdTableDataList<PushNotification> listData = new AntdTableDataList<>();
            pagination.setTotal(pushNotificationDAO.count(type, sendType, receiverType, receiver, sendResult, sentDate1, sentDate2));
            listData.setPagination(pagination);
            listData.setList(pushNotificationDAO.list(type, sendType, receiverType, receiver, sendResult, sentDate1, sentDate2,
                    pagination.toPageRequest()));

            return ResponseEntity.ok(listData);
        } catch (DataAccessException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //    @Secured({"ROLE_PUSH_NOTIFICATION_VIEW", "ROLE_PUSH_NOTIFICATION_MANAGE"})
    @GetMapping("{id}")
    public ResponseEntity<?> get(@PathVariable String id) {
        if (ObjectUtils.isEmpty(id))
            return ResponseEntity.badRequest().body("ID хоосон байна");

        try {
            Optional<PushNotification> pushNotificationOpt = pushNotificationRepository.findById(id);
            if (pushNotificationOpt.isEmpty())
                return ResponseEntity.badRequest().body("Push notification олдсонгүй");

            return ResponseEntity.ok(pushNotificationOpt.get());
        } catch (DataAccessException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //    @Secured({"ROLE_PUSH_NOTIFICATION_MANAGE", "ROLE_INTERNAL_API"})
    @PostMapping("create")
    public ResponseEntity<?> create(@Valid @RequestBody PushNotification request, Principal principal, Locale locale) {
        if ((request.getReceiverType() == PushNotificationReceiverType.USERNAME ||
                request.getReceiverType() == PushNotificationReceiverType.TOKEN)
                && ObjectUtils.isEmpty(request.getReceiver())
        )
            return badRequest("Хүлээн авагч хоосон байна");

        try {
            User user = basePermissionService.user(principal, locale);

            PushNotification pushNotification = new PushNotification();
            pushNotification.setType(request.getType());
            pushNotification.setSendType(request.getSendType());
            pushNotification.setScheduledDate(request.getScheduledDate());
            if (pushNotification.getScheduledDate() == null)
                pushNotification.setScheduledDate(LocalDateTime.now());
            pushNotification.setReceiverType(request.getReceiverType());
            pushNotification.setReceiver(request.getReceiver());
            if (request.getReceiverType() == PushNotificationReceiverType.USERNAME)
                pushNotification.setReceiver(request.getReceiver());
            pushNotification.setPriority(request.getPriority());
            pushNotification.setTitle(request.getTitle());
            pushNotification.setBody(request.getBody());
            pushNotification.setData(request.getData());
            pushNotification.setCreatedDate(LocalDateTime.now());
            pushNotification.setCreatedBy(user.getId());
            pushNotification = pushNotificationRepository.save(pushNotification);

            return ResponseEntity.ok(pushNotification.getId());
        } catch (PermissionException e) {
            return errorInvalidRequest(locale);
        }
    }

    //    @Secured({"ROLE_PUSH_NOTIFICATION_MANAGE"})
    @PostMapping("update")
    public ResponseEntity<?> update(@Valid @RequestBody PushNotification request, Principal principal, Locale locale) {
        if (ObjectUtils.isEmpty(request.getId()))
            return badRequest("ID хоосон байна");

        Optional<PushNotification> pushNotificationOpt = pushNotificationRepository.findById(request.getId());
        if (pushNotificationOpt.isEmpty())
            return badRequest("Push notification олдсонгүй");

        PushNotification pushNotification = pushNotificationOpt.get();
        if (pushNotification.getSendResult() != null)
            return badRequest("Зөвхөн хараахан илгээгээгүй push notification засварлах боломжтой");

        try {
            User user = basePermissionService.user(principal, locale);

            pushNotification.setType(request.getType());
            pushNotification.setSendType(request.getSendType());
            pushNotification.setScheduledDate(request.getScheduledDate());
            if (pushNotification.getScheduledDate() == null)
                pushNotification.setScheduledDate(LocalDateTime.now());
            pushNotification.setReceiverType(request.getReceiverType());
            pushNotification.setReceiver(request.getReceiver());
            if (request.getReceiverType() == PushNotificationReceiverType.USERNAME)
                pushNotification.setReceiver(request.getReceiver());
            pushNotification.setPriority(request.getPriority());
            pushNotification.setTitle(request.getTitle());
            pushNotification.setBody(request.getBody());
            pushNotification.setData(request.getData());
            pushNotification.setModifiedDate(LocalDateTime.now());
            pushNotification.setModifiedBy(user.getId());
            pushNotificationRepository.save(pushNotification);

            return ResponseEntity.ok(pushNotification.getId());
        } catch (PermissionException e) {
            return errorInvalidRequest(locale);
        }
    }
}
