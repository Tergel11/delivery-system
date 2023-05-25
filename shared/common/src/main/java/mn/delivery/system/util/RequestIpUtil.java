package mn.delivery.system.util;

import jakarta.servlet.http.HttpServletRequest;

public class RequestIpUtil {

    public static String getClientIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}
