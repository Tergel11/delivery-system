package mn.delivery.system.util.sse;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import mn.delivery.system.util.dto.SseRequest;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class SseService {

    private static final Long DAY_IN_MILLISECONDS = 86400000L;

    private final Map<String, SseEmitter> sseEmitters = new ConcurrentHashMap<>();

    public SseEmitter createChannel(String eventName, String channelName) throws IOException {
        final SseEmitter sseEmitter = new SseEmitter(DAY_IN_MILLISECONDS);
        try {
            sseEmitters.put(channelName, sseEmitter);

            sseEmitter.send(SseEmitter.event().name(eventName).data(channelName));
            sseEmitter.onCompletion(() -> sseEmitters.remove(channelName));
            sseEmitter.onTimeout(() -> sseEmitters.remove(channelName));

            log.info("created sseEmitter: {} on {}", sseEmitter, channelName);
            return sseEmitter;
        } catch (Exception e) {
            sseEmitter.completeWithError(e);
            throw new IOException("Could not store the file. Error: " + e.getMessage());
        }
    }

    public void removeEmitter(String channelName) {
        sseEmitters.remove(channelName);
    }

    public void sendEmitterSignal(final SseRequest<List<String>> sseRequest) throws IOException {
        final var channelName = sseRequest.getChannelName();
        final var totalPercentage = sseRequest.getTotalPercent();

        final var sseEmitter = sseEmitters.get(channelName);
        if (sseEmitter == null) {
            throw new IOException("Emitter not found");
        }

        try {
            for (int i = 0; i < totalPercentage; i++) {
                Thread.sleep(500);
                sseEmitter.send(
                    SseEmitter.event().name(channelName).data(sseRequest.getData()));
                log.info("Sent emitter to {} as {}", channelName, i);

                if (i == 100) {
                    sseEmitter.complete();
                }
                log.info("Emitter signal successfully sent");
            }
        } catch (InterruptedException ex) {
            log.error(ex.getMessage());
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            sseEmitter.completeWithError(e);
            throw new IOException("Could not store the file. Error: " + e.getMessage());
        } finally {
            log.info("Removing emitter...");
            this.removeEmitter(channelName);
        }
    }
}
