import com.bwxor.portix.entity.IPAddress;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IPAddressTest {
    @Test
    public void testIncrement() {
        IPAddress ipAddress = new IPAddress("192.168.1.1");
        ipAddress.increment();
        Assertions.assertNotNull(ipAddress);
        Assertions.assertEquals("192.168.1.2", ipAddress.toString());
    }

    @Test
    public void testIncrementByteOver255() {
        IPAddress ipAddress = new IPAddress("192.168.1.255");
        ipAddress.increment();
        Assertions.assertNotNull(ipAddress);
        Assertions.assertEquals("192.168.2.0", ipAddress.toString());
    }

    @Test
    public void testDiff() {
        IPAddress ip1 = new IPAddress("192.169.0.5");
        IPAddress ip2 = new IPAddress("192.168.255.254");
        long diff = ip1.diff(ip2);
        Assertions.assertEquals(7, diff);
    }
}
