package linhlang.commons.utils;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.nio.file.Paths;

@UtilityClass
public class UrlUtils {

    public static String appendDomain(String url, String domain) {
        return isFullUrl(url) ? url : domain + "/" + Paths.get(url);
    }

    public static boolean isFullUrl(String url) {
        return StringUtils.startsWithAny(url, "http://", "https://");
    }

    public static String joinPath(String... paths) {
        return Paths.get("", paths).toString();
    }
}
