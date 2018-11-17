package accounts.domain.control;

import accounts.StringUtil;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.Map;

public class StringUtilTest {

    private static final String AAAA = "aaaa";
    private static final String BBB = "bbb";
    private static final String CC = "cc";
    private static final String D = "d";

    @Test
    public void checkAPlusBAmounts() {
        Map<Character, Integer> result = StringUtil.getCharAmountsFromText(AAAA + BBB + CC + D);
        Assertions.assertThat(result).containsKeys('a', 'b', 'c', 'd');
        Assertions.assertThat(result.get('a')).isEqualTo(4);
        Assertions.assertThat(result.get('b')).isEqualTo(3);
        Assertions.assertThat(result.get('c')).isEqualTo(2);
        Assertions.assertThat(result.get('d')).isEqualTo(1);
    }

    @Test
    public void checkEmptyText() {
        Map<Character, Integer> result = StringUtil.getCharAmountsFromText("");
        Assertions.assertThat(result).isEmpty();
    }

    @Test
    public void checkNullText() {
        Map<Character, Integer> result = StringUtil.getCharAmountsFromText(null);
        Assertions.assertThat(result).isEmpty();
    }
}
