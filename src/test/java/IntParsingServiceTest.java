import com.bwxor.portix.service.parser.IntParsingService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class IntParsingServiceTest {
    private IntParsingService intParsingService;
    @BeforeEach
    public void setup() {
        intParsingService = new IntParsingService();
    }

    @Test
    public void testTryParseTrue() {
        boolean result = intParsingService.tryParse("5");
        Assertions.assertTrue(result);
    }

    @Test
    public void testTryParseFalse() {
        boolean result = intParsingService.tryParse("B");
        Assertions.assertFalse(result);
    }
}
