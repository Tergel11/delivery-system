package mn.delivery.system.service.systemconfig;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mn.delivery.system.model.systemconfig.SystemKeyValue;
import mn.delivery.system.repository.systemconfig.SystemKeyValueRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * @author Tergel
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SystemKeyValueService {
    private final SystemKeyValueRepository systemKeyValueRepository;

    public SystemKeyValue get(String key) {
        if (ObjectUtils.isEmpty(key))
            return null;

        Optional<SystemKeyValue> valueOpt = systemKeyValueRepository.findById(key);
        return valueOpt.orElse(null);
    }

    public String getStringValue(String key) {
        if (ObjectUtils.isEmpty(key))
            return null;

        Optional<SystemKeyValue> valueOpt = systemKeyValueRepository.findById(key);
        if (valueOpt.isPresent() && valueOpt.get().getValue() != null) {
            try {
                return String.valueOf(valueOpt.get().getValue());
            } catch (Exception e) {
                log.error("get String value convert error: key->" + key + ", value->" + valueOpt.get().getValue());
            }
        }
        return null;
    }

    public Integer getIntegerValue(String key) {
        if (ObjectUtils.isEmpty(key))
            return null;

        Optional<SystemKeyValue> valueOpt = systemKeyValueRepository.findById(key);
        if (valueOpt.isPresent() && valueOpt.get().getValue() != null) {
            try {
                return Integer.valueOf(valueOpt.get().getValue().toString());
            } catch (Exception e) {
                log.error("get Integer value convert error: key->" + key + ", value->" + valueOpt.get().getValue());
            }
        }
        return null;
    }

    public BigDecimal getBigDecimalValue(String key) {
        if (ObjectUtils.isEmpty(key))
            return null;

        Optional<SystemKeyValue> valueOpt = systemKeyValueRepository.findById(key);
        if (valueOpt.isPresent() && valueOpt.get().getValue() != null) {
            try {
                return new BigDecimal(valueOpt.get().getValue().toString());
            } catch (Exception e) {
                log.error("get Integer value convert error: key->" + key + ", value->" + valueOpt.get().getValue());
            }
        }
        return null;
    }
}
