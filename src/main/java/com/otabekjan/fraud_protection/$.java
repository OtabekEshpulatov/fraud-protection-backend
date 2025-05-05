package com.otabekjan.fraud_protection;

import com.otabekjan.fraud_protection.service.FileService;
import io.jmix.core.FileRef;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

import java.net.URL;
import java.util.*;
import java.util.regex.Pattern;

/**
 * User: abdul
 * Date: 11/15/2023 2:54 PM
 */

public class $ {
    public static final String EMAIL_REGEX = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";
    public static final String PHONE_REGEX = "\\+?(\\d{1,2})?[ .-]?\\(?(\\d{3})\\)?[ .-]?(\\d{3})[ .-]?(\\d{4})";
    public static final String ISO_DATE_TIME = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String ISO_DATE = "yyyy-MM-dd";
    public static final String SEPARATOR = ":=";
    private static final Logger log = LoggerFactory.getLogger($.class);
    private static final String ALGORITHM = "AES";
    static Map<Character, String> map = new LinkedHashMap<>();

    static {
        char[] abcCyr = {'а', 'б', 'в', 'г', 'д', 'е', 'ё', 'ж', 'з', 'и', 'й', 'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ъ', 'ы', 'ь', 'э', 'ю', 'я', 'ў', 'қ', 'ғ', 'ҳ', 'А', 'Б', 'В', 'Г', 'Д', 'Е', 'Ё', 'Ж', 'З', 'И', 'Й', 'К', 'Л', 'М', 'Н', 'О', 'П', 'Р', 'С', 'Т', 'У', 'Ф', 'Х', 'Ц', 'Ч', 'Ш', 'Щ', 'Ъ', 'Ы', 'Ь', 'Э', 'Ю', 'Я', 'Ў', 'Қ', 'Ғ', 'Ҳ'};
        String[] abcLat = {"a", "b", "v", "g", "d", "e", "yo", "j", "z", "i", "y", "k", "l", "m", "n", "o", "p", "r", "s", "t", "u", "f", "h", "ts", "ch", "sh", "sh", "'", "i", "", "e", "yu", "ya", "o'", "q", "g'", "h", "A", "B", "V", "G", "D", "E", "Yo", "J", "Z", "I", "Y", "K", "L", "M", "N", "O", "P", "R", "S", "T", "U", "F", "H", "Ts", "Ch", "Sh", "Sh", "'", "I", "", "E", "Yu", "Ya", "O'", "Q", "G'", "H"};
        for (int x = 0; x < abcCyr.length; x++) {
            map.put(abcCyr[x], abcLat[x]);
        }
    }

    public static String transliterate(String message) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < message.length(); i++) {
            if (map.containsKey(message.charAt(i))) {
                builder.append(map.get(message.charAt(i)));
            } else {
                builder.append(message.charAt(i));
            }
        }
        return builder.toString();
    }

    public static String clearHtml(String html) {
        if (html == null) return null;
        return Jsoup.parse(html).text();
    }

    public static String clearHtmlLineBreak(String html) {
        if (html == null) return null;
        return Jsoup.parse(html).wholeText();
    }

    public static boolean isEmpty(String value) {
        if (value == null) return true;
        return value.trim().isEmpty();
    }

    public static boolean isEmpty(Object value) {
        if (value == null) return true;
        if (value instanceof String)
            return ((String) value).trim().isEmpty();
        if (value instanceof Collection)
            return ((Collection<?>) value).isEmpty();
        if (value instanceof Map)
            return ((Map<?, ?>) value).isEmpty();
        if (value instanceof Object[]) {
            return ((Object[]) value).length == 0;
        }
        return false;
    }

    public static boolean isEmail(String email) {
        if (email == null) return false;
        Pattern pat = Pattern.compile(EMAIL_REGEX);
        return pat.matcher(email).matches();
    }

    public static boolean isPhone(String phone) {
        if (phone == null) return false;
        Pattern pat = Pattern.compile(PHONE_REGEX);
        return pat.matcher(phone).matches();
    }

    public static boolean isURL(String url) {
        try {
            new URL(url).toURI();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isUUID(String uuid) {
        try {
            UUID.fromString(uuid);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isNumeric(String strNum) {
        try {
            Double.parseDouble(strNum);
        } catch (NumberFormatException | NullPointerException nfe) {
            return false;
        }
        return true;
    }

    public static boolean or(Object object, Object... conditions) {
        for (Object cond : conditions) {
            if (Objects.equals(object, cond)) {
                return true;
            }
        }
        return false;
    }

    public static String defaultString(String str, String emptyDefault) {
        return isEmpty(str) ? emptyDefault : str;
    }

    public static boolean listContains(List<String> list, String... conditions) {
        for (String cond : conditions) {
            if (list.contains(cond)) {
                return true;
            }
        }
        return false;
    }

    public static <T> List<T> nonNull(List<T> ts) {
        if (ts != null) return ts;
        return new ArrayList<>();
    }

    public static <T> Set<T> nonNull(Set<T> ts) {
        if (ts != null) return ts;
        return new HashSet<>();
    }

    public static String makeFileUrl(FileRef fileRef) {
        String host = AppBeans.get(Environment.class).getProperty("app.url");
        return makeFileUrl(host, fileRef);
    }

    public static String makeFileUrl(String host, FileRef fileRef) {
        if (fileRef == null) return null;
        String id = AppBeans.get(FileService.class).encode(fileRef);
        return host + "/file/" + id;
    }

    // NullPointerException-free comparator
    public static <C extends Comparable<C>> int compare(C c1, C c2) {
        if (c1 == null && c2 == null) return 0;
        if (c1 == null) return -1;
        if (c2 == null) return 1;
        return Objects.compare(c1, c2, Comparator.naturalOrder());
    }

}
