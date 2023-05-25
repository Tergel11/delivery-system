package mn.delivery.system.config;

import lombok.extern.slf4j.Slf4j;
import mn.delivery.system.model.auth.enums.ApplicationRole;
import mn.delivery.system.model.enums.SharedStatus;
import mn.delivery.system.model.systemconfig.enums.SystemCronType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

/**
 * @author Tergel
 */
@Slf4j
public class EnumsConverter {

    @ReadingConverter
    public static class ApplicationRoleConverter implements Converter<String, ApplicationRole> {
        @Override
        public ApplicationRole convert(final String source) {
            return ApplicationRole.fromString(source);
        }
    }

    @ReadingConverter
    public static class SharedStatusConverter implements Converter<String, SharedStatus> {
        @Override
        public SharedStatus convert(final String source) {
            return SharedStatus.fromString(source);
        }
    }

    @ReadingConverter
    public static class SystemCronTypeConverter implements Converter<String, SystemCronType> {
        @Override
        public SystemCronType convert(final String source) {
            return SystemCronType.fromString(source);
        }
    }
}
