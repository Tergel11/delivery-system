package mn.delivery.system.datafiller.systemconfig;

import lombok.RequiredArgsConstructor;
import mn.delivery.system.model.systemconfig.SystemCron;
import mn.delivery.system.model.systemconfig.enums.SystemCronType;
import mn.delivery.system.repository.systemconfig.SystemCronRepository;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

/**
 * @author digz6666
 */
@Component
@RequiredArgsConstructor
public class SystemCronDataFiller {

    private final SystemCronRepository systemCronRepository;

    @PostConstruct
    private void fill() {
        for (SystemCronType type : SystemCronType.values())
            if (!systemCronRepository.existsById(type))
                systemCronRepository.save(new SystemCron(type, false, null));
    }
}
