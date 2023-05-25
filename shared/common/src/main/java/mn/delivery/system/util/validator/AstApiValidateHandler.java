package mn.delivery.system.util.validator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mn.delivery.system.exception.auth.ModuleException;
import mn.delivery.system.repository.systemconfig.SystemApiConfRepository;
import mn.delivery.system.util.LocalizationUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AstApiValidateHandler {

    private final LocalizationUtil localizationUtil;
    private final SystemApiConfRepository systemApiConfRepository;

    @Around(" @annotation(mn.delivery.system.util.validator.AstApiValidate)")
    public Object validateAspect(ProceedingJoinPoint pjp)
        throws ModuleException {

        try {
            final var signature = (MethodSignature) pjp.getSignature();
            final var method = signature.getMethod();

            final var validateAction = method.getAnnotation(
                AstApiValidate.class);

            final var apiEnableOpt = systemApiConfRepository.findByModuleType(
                validateAction.moduleName());
            if (apiEnableOpt.isEmpty()) {
                throw new ModuleException(
                    localizationUtil.build("system.module.not-exist", ""));
            } else {
                final var apiEnable = apiEnableOpt.get();
                if (apiEnable.isEnabled()) {
                    return pjp.proceed();
                } else {
                    throw new ModuleException(
                        localizationUtil.build("system.module.not-enabled", ""));
                }
            }
        } catch (Throwable t) {
            throw new ModuleException(t.getMessage());
        }
    }
}
