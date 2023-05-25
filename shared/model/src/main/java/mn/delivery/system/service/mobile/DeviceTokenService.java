package mn.delivery.system.service.mobile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mn.delivery.system.model.mobile.DeviceToken;
import mn.delivery.system.repository.mobile.DeviceTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class DeviceTokenService {

    private final DeviceTokenRepository deviceTokenRepository;

    public DeviceToken createOrUpdate(String deviceId, String token, String os, String email, String ip) {
        DeviceToken deviceToken;
        if (!ObjectUtils.isEmpty(deviceId)) {
            deviceTokenRepository.deleteByDeviceId(deviceId);
            deviceToken = null;
        } else {
            deviceToken = deviceTokenRepository.findByTokenAndOs(token, os);
        }

        if (deviceToken == null) {
            deviceToken = new DeviceToken();
            deviceToken.setCreatedDate(LocalDateTime.now());
        }

        deviceToken.setDeviceId(deviceId);
        deviceToken.setToken(token);
        deviceToken.setOs(os);

        if (email != null) {
            deviceToken.setEmail(email);
        }
        deviceToken.setIp(ip);
        deviceToken.setModifiedDate(LocalDateTime.now());
        return deviceTokenRepository.save(deviceToken);
    }

    public void deleteOldToken(String email) {
        List<DeviceToken> deviceTokens = deviceTokenRepository.findByEmail(email);
        for (DeviceToken deviceToken : deviceTokens) {
            deviceTokenRepository.delete(deviceToken);
        }
    }
}
