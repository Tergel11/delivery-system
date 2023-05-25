package mn.delivery.system.util.logger;

import static java.time.temporal.ChronoUnit.SECONDS;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggerAspect {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
        "yyyy-MM-dd HH:mm:ss");

    @Before("@annotation(mn.delivery.system.util.logger.LogExecutionDuration)")
    public void beforeMethodStart(final JoinPoint point) {
        String message = String.format("Method { %s' } Started at %s",
            point.getSignature().getName(),
            LocalDateTime.now().format(formatter));

        log.debug(message);
    }

    @After("@annotation(mn.delivery.system.util.logger.LogExecutionDuration)")
    public void afterMethodStart(final JoinPoint point) {
        String message = String.format("Method { %s } Ended at %s",
            point.getSignature().getName(),
            LocalDateTime.now().format(formatter));

        log.debug(message);
    }

    @Around("@annotation(mn.delivery.system.util.logger.LogExecutionDuration)")
    public Object calculate(final ProceedingJoinPoint point) {
        final var startTime = LocalTime.now();
        Object response = null;
        try {
            response = point.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            final var endTime = LocalTime.now();
            String message = String.format(
                "Processing time of Method { %s } -> %d-seconds",
                point.getSignature().getName(),
                SECONDS.between(startTime, endTime));
            log.debug(message);
        }
        return response;
    }
}
