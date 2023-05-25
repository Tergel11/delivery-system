package mn.delivery.system.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * @author Tergel
 */
@Slf4j
@Configuration
@EnableAsync
public class EmailAsyncConfiguration {

//    @Bean("emailExecutor")
//    public Executor emailExecutor() {
//        log.info("Creating Email Async Task Executor");
//
//        final var executor = new ThreadPoolTaskExecutor();
//        executor.setCorePoolSize(10);
//        executor.setMaxPoolSize(10);
//        executor.setQueueCapacity(1000);
//        executor.setThreadNamePrefix("Email Sender Thread-");
//        executor.initialize();
//        executor.setWaitForTasksToCompleteOnShutdown(true);
//        return executor;
//    }
}
