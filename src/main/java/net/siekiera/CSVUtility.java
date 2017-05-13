package net.siekiera;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Convert Objects to CSV format
 * Created by Eric on 13.05.2017.
 */
public class CSVUtility {
    //don't set null values! use "" instead!
    //if you set value to null - "null" will be included in CSV!
    private static final String FIELD_SEPARATOR = ",";
    private static final String FIELD_WRAPPER = "\"";
    private static final String CRLF = "\r\n";

    public static String convertListToCsvLine(List<String> values) {
        boolean isFirst = true;
        StringBuilder sb = new StringBuilder();
        if (values.isEmpty()) {
            return "";
        } else {
            for (String singleFieldValue : values) {
                if (!isFirst) {
                    sb.append(FIELD_SEPARATOR);
                } else {
                    isFirst = false;
                }
                sb.append(FIELD_WRAPPER);
                sb.append(singleFieldValue);
                sb.append(FIELD_WRAPPER);
            }
        }
        sb.append(CRLF);
        return sb.toString();
    }

    public static List<String> convertObjectsToStrings(Map<String, Object> objects) {
        List<String> stringList = new ArrayList<String>();
        for (Map.Entry<String, Object> entry : objects.entrySet()) {
            if (entry.getValue() == null) {
                stringList.add("");
            } else {
                stringList.add(entry.getValue().toString());
            }
        }
        return stringList;
    }

    public static String convertObjectsToCsvLine(Map<String, Object> objects) {
        List<String> strings = convertObjectsToStrings(objects);
        return convertListToCsvLine(strings);
    }

}
