package view.kz.security;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Класс осуществляет различные проверки, для исключения уязвимостей, обнаруженных при сканированиях.
 * <p>
 * Vladimir
 * 03.11.2016.
 */
public class VulnerabilitiesDefender {

    /**
     * Подделка запроса сервера, https://cwe.mitre.org/data/definitions/918.html
     * @param query    передавать только параметры URL, но не URL целиком
     */
    public static String checkServerSideRequestForgery(String query) {
        if (query == null) {
            return null;
        }
        if (query.contains("http://") || query.contains("https://")) {
            throw new SecurityException(restrictLogForging("Suspicious url query: " + query));
        }

        return query;
    }

    /**
     * Внедрение внешних сущностей XML, https://cwe.mitre.org/data/definitions/611.html,
     * http://stackoverflow.com/q/26488319/1341148
     */
    public static void restrictXmlXxe(DocumentBuilderFactory dbf) throws ParserConfigurationException {
        String FEATURE = "http://apache.org/xml/features/disallow-doctype-decl";
        dbf.setFeature(FEATURE, true);
        dbf.setExpandEntityReferences(false);
        dbf.setXIncludeAware(false);
    }

    /**
     * Создание/Удаление/Чтение произвольного файла, https://cwe.mitre.org/data/definitions/73.html
     */
    public static void checkExternalControlOfFileNameOrPath(String path) {
        if (path != null && path.contains("..")) {
            throw new SecurityException(restrictLogForging("Suspicious path: " + path));
        }
    }

    public static void checkExternalControlOfFileNameOrPath(String path, List<String> allowedDirs, String fileNamePattern) {
        if(path != null) {
            String fileName = path.substring(path.lastIndexOf(File.separator) + 1);
            String dir = path.substring(0, path.lastIndexOf(File.separator) + 1);
            if(allowedDirs != null) {
                if(allowedDirs.isEmpty()) {
                    if(!dir.isEmpty()) {
                        throw new SecurityException(restrictLogForging("Suspicious path: " + path));
                    }
                }
                else {
                    boolean testComplete = false;
                    for(String str : allowedDirs) {
                        if(dir.toLowerCase().startsWith(str.toLowerCase())) {
                            testComplete = true;
                            break;
                        }
                    }
                    if(!testComplete) {
                        throw new SecurityException(restrictLogForging("Suspicious path: " + path));
                    }
                }
            }
            if(fileNamePattern != null) {
                Pattern p = Pattern.compile(fileNamePattern);
                Matcher m = p.matcher(fileName);
                if(!m.matches()) {
                    throw new SecurityException(restrictLogForging("Suspicious path: " + path));
                }
            }
        }
    }

    public static String restrictLogForging(String str) {
        return str.replaceAll("\n", "_").replaceAll("\r", "_");
    }

    public static String restrictHttpResponseSplitting(String str) {
        return str.replaceAll("\n", "_").replaceAll("\r", "_");
    }
}
