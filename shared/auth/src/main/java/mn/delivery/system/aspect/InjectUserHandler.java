package mn.delivery.system.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mn.delivery.system.exception.InjectUserException;
import mn.delivery.system.repository.auth.UserRepository;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class InjectUserHandler {

    private final UserRepository userRepository;

    @Around("@annotation(mn.delivery.system.annotations.InjectUser)")
    public Object validateAspect(ProceedingJoinPoint joinPoint) throws InjectUserException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (User) auth.getPrincipal();
//        log.info("userDetails: " + userDetails.getUsername());

        mn.delivery.system.model.auth.User user = userRepository.findByEmail(userDetails.getUsername());
        if (user == null)
            throw new InjectUserException("User not found: " + userDetails.getUsername());

        try {
            List<Object> newArgs = new ArrayList<>(joinPoint.getArgs().length);
            for (Object arg : joinPoint.getArgs()) {
                if (arg instanceof mn.delivery.system.model.auth.User)
                    newArgs.add(user);
                else
                    newArgs.add(arg);
            }

            return joinPoint.proceed(newArgs.toArray());
        } catch (Throwable t) {
            log.error(t.getMessage(), t);
            throw new InjectUserException(t.getMessage());
        }
    }
}
