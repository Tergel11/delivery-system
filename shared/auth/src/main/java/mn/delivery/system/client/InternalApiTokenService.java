package mn.delivery.system.client;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mn.delivery.system.util.JwtTokenUtil;
import mn.delivery.system.dao.auth.UserDao;
import mn.delivery.system.model.auth.User;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Date;

/**
 * @author digz6666
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class InternalApiTokenService {

    private final UserDao userDAO;
    private final JwtTokenUtil tokenUtil;

    @Getter
    private String token; // cron-оор generate хийнэ

    public boolean isCurrentTokenValid() {
        if (ObjectUtils.isEmpty(token)) {
            return false;
        }

        Date expirationDate = tokenUtil.getExpirationDateFromToken(token);
//        log.info("expirationDate ->" + expirationDate);
        // try to refresh token 10 minutes before expiration date
        return expirationDate != null && expirationDate.after(DateUtils.addMinutes(new Date(), 10));
    }

    public void generateToken() {
        User user = userDAO.getInternalApi();
        if (user != null) {
            token = tokenUtil.generateToken(user.getEmail());
            log.debug("Generated new token with user -> " + user.getEmail() + " -> " + token);
        } else {
            token = null;
            log.debug("Failed to generate token");
        }
    }
}
