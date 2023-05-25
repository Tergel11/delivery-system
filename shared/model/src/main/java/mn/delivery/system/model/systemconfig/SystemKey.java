package mn.delivery.system.model.systemconfig;

import java.util.Arrays;
import java.util.List;

/**
 * @author Tergel
 */
public class SystemKey {

    // app version
    public final static String APP_VERSION_ANDROID = "APP_VERSION_ANDROID"; // ios app-н version
    public final static String APP_VERSION_IOS = "APP_VERSION_IOS"; // android app-н version

    public static List<String> values() {
        return Arrays.asList(APP_VERSION_ANDROID, APP_VERSION_IOS);
    }
}
