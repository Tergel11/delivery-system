package mn.delivery.system.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mn.delivery.system.model.auth.User;
import mn.delivery.system.model.email.EmailConfirmation;
import mn.delivery.system.model.email.enums.ConfirmationType;
import mn.delivery.system.util.ListUtils;
import mn.delivery.system.database.util.MongoUtil;
import mn.delivery.system.exception.auth.UserException;
import mn.delivery.system.repository.auth.UserRepository;
import mn.delivery.system.repository.email.EmailConfirmationRepository;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final EmailConfirmationRepository emailConfirmationRepository;

    private final MongoUtil mongoUtil;

    public String getEmailById(String id) {
        if (ObjectUtils.isEmpty(id))
            return null;

        Optional<User> userOpt = userRepository.findById(id);
        return userOpt.map(User::getEmail).orElse(null);
    }

    public User getByEmail(String email) {
        if (ObjectUtils.isEmpty(email))
            return null;

        return userRepository.findByEmailAndDeletedFalse(email.toLowerCase());
    }

    public List<String> findByEmail(String email) {
        List<String> userIds = new ArrayList<>();
        if (!ObjectUtils.isEmpty(email))
            userIds = ListUtils.getIds(userRepository.findAllByEmail(email));
        return userIds;
    }

    @Transactional
    public boolean confirmRemoveMobile(String token) throws UserException {
        EmailConfirmation confirmation = emailConfirmationRepository.findByTokenAndTypeAndDeletedFalse(token,
                ConfirmationType.REMOVE_MOBILE);
        if (confirmation == null) {
            throw new UserException("Буруу токен байна");
        }

        if (confirmation.getExpiredDate().isBefore(LocalDateTime.now())) {
            throw new UserException("Токены хугацаа дууссан байна");
        }

        User user = userRepository.findByEmailAndDeletedFalse(confirmation.getEmail());
        if (user == null) {
            throw new UserException("Хэрэглэгч олдсонгүй");
        }

        mongoUtil.findAndModify(
                new Query()
                        .addCriteria(Criteria.where("id").is(user.getId())),
                new Update()
                        .unset("mobile")
                        .set("mobileVerified", false)
                        .set("modifiedDate", LocalDateTime.now()),
                MongoUtil.FM_OPTIONS,
                User.class);

        mongoUtil.findAndModify(
                new Query()
                        .addCriteria(Criteria.where("id").is(confirmation.getId())),
                new Update()
                        .set("confirmedDate", LocalDateTime.now())
                        .set("modifiedDate", LocalDateTime.now()),
                MongoUtil.FM_OPTIONS,
                EmailConfirmation.class);

        return true;
    }
}
