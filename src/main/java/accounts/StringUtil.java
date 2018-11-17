package accounts;

import java.util.*;

public class StringUtil {


    private StringUtil() {

    }

    public static Map<Character, Integer> getCharAmountsFromText(String text) {

        if (text == null || text.length() == 0) {
            return new HashMap<>();
        }
        Map<Character, Integer> result = new HashMap<>();
        for (char c: text.toCharArray()) {
            if (result.containsKey(c)) {
                int amount = result.get(c) + 1;
                result.put(c, amount);
            } else {
                result.put(c, 1);
            }
        }
        return result;
    }

}
