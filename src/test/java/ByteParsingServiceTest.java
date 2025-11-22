import com.bwxor.portix.service.parser.ByteParsingService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ByteParsingServiceTest {
    private ByteParsingService byteParsingService;
    @BeforeEach
    public void setup() {
        byteParsingService = new ByteParsingService();
    }

    @Test
    public void testTryParseTrue() {
        boolean result = byteParsingService.tryParse("5");
        Assertions.assertTrue(result);
    }

    @Test
    public void testTryParseFalse() {
        boolean result = byteParsingService.tryParse("B");
        Assertions.assertFalse(result);
    }
}
