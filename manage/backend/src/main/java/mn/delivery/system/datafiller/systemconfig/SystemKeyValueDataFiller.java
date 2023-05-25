package mn.delivery.system.datafiller.systemconfig;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mn.delivery.system.model.systemconfig.SystemKey;
import mn.delivery.system.model.systemconfig.SystemKeyValue;
import mn.delivery.system.repository.systemconfig.SystemKeyValueRepository;
import org.springframework.stereotype.Component;

/**
 * @author digz6666
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SystemKeyValueDataFiller {

    private final SystemKeyValueRepository systemKeyValueRepository;

    @PostConstruct
    private void fill() {
        for (String key : SystemKey.values())
            if (!systemKeyValueRepository.existsById(key))
                systemKeyValueRepository.save(new SystemKeyValue(key));
    }
}
