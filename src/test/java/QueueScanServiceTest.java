import com.bwxor.portix.entity.IPAddress;
import com.bwxor.portix.entity.Port;
import com.bwxor.portix.entity.ScanResult;
import com.bwxor.portix.service.scan.QueueScanService;
import com.bwxor.portix.service.scan.QueueScanner;
import com.bwxor.portix.type.ScanStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

@ExtendWith(MockitoExtension.class)
public class QueueScanServiceTest {
    private QueueScanService queueScanService;
    @Mock
    QueueScanner queueScanner;

    @BeforeEach
    public void setup() {
        queueScanService = new QueueScanService();
        queueScanService.setQueueScanner(queueScanner);
    }

    @Test
    public void testSuccess() {
        Mockito.when(queueScanner.doScan(new IPAddress("192.168.1.1"), new Port(80), 500)).thenReturn(
                new ScanResult(ScanStatus.ACCESSIBLE, new IPAddress("192.168.1.1"), new Port(80))
        );

        Mockito.when(queueScanner.doScan(new IPAddress("192.168.1.1"), new Port(443), 500)).thenReturn(
                new ScanResult(ScanStatus.NOT_ACCESSIBLE, new IPAddress("192.168.1.1"), new Port(443))
        );

        Mockito.when(queueScanner.doScan(new IPAddress("192.168.1.2"), new Port(80), 500)).thenReturn(
                new ScanResult(ScanStatus.NOT_ACCESSIBLE, new IPAddress("192.168.1.2"), new Port(80))
        );

        Mockito.when(queueScanner.doScan(new IPAddress("192.168.1.2"), new Port(443), 500)).thenReturn(
                new ScanResult(ScanStatus.NOT_ACCESSIBLE, new IPAddress("192.168.1.2"), new Port(443))
        );

        Mockito.when(queueScanner.doScan(new IPAddress("192.168.1.3"), new Port(80), 500)).thenReturn(
                new ScanResult(ScanStatus.ACCESSIBLE, new IPAddress("192.168.1.3"), new Port(80))
        );

        Mockito.when(queueScanner.doScan(new IPAddress("192.168.1.3"), new Port(443), 500)).thenReturn(
                new ScanResult(ScanStatus.ACCESSIBLE, new IPAddress("192.168.1.3"), new Port(443))
        );

        queueScanService.scan(new IPAddress("192.168.1.1"), new IPAddress("192.168.1.3"), List.of(new Port(80), new Port(443)), 500);
        ConcurrentLinkedQueue<ScanResult> results = queueScanService.getScanQueue();

        Assertions.assertNotNull(results);
        Assertions.assertEquals(6, results.size());

        ScanResult res;

        res = results.poll();
        Assertions.assertNotNull(res);
        Assertions.assertEquals(new IPAddress("192.168.1.1"), res.getIpAddress());
        Assertions.assertEquals(new Port(80), res.getPort());
        Assertions.assertEquals(ScanStatus.ACCESSIBLE, res.getStatus());

        res = results.poll();
        Assertions.assertNotNull(res);
        Assertions.assertEquals(new IPAddress("192.168.1.1"), res.getIpAddress());
        Assertions.assertEquals(new Port(443), res.getPort());
        Assertions.assertEquals(ScanStatus.NOT_ACCESSIBLE, res.getStatus());

        res = results.poll();
        Assertions.assertNotNull(res);
        Assertions.assertEquals(new IPAddress("192.168.1.2"), res.getIpAddress());
        Assertions.assertEquals(new Port(80), res.getPort());
        Assertions.assertEquals(ScanStatus.NOT_ACCESSIBLE, res.getStatus());

        res = results.poll();
        Assertions.assertNotNull(res);
        Assertions.assertEquals(new IPAddress("192.168.1.2"), res.getIpAddress());
        Assertions.assertEquals(new Port(443), res.getPort());
        Assertions.assertEquals(ScanStatus.NOT_ACCESSIBLE, res.getStatus());

        res = results.poll();
        Assertions.assertNotNull(res);
        Assertions.assertEquals(new IPAddress("192.168.1.3"), res.getIpAddress());
        Assertions.assertEquals(new Port(80), res.getPort());
        Assertions.assertEquals(ScanStatus.ACCESSIBLE, res.getStatus());

        res = results.poll();
        Assertions.assertNotNull(res);
        Assertions.assertEquals(new IPAddress("192.168.1.3"), res.getIpAddress());
        Assertions.assertEquals(new Port(443), res.getPort());
        Assertions.assertEquals(ScanStatus.ACCESSIBLE, res.getStatus());

    }
}
