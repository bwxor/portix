package com.bwxor.iport.service.scan;

import com.bwxor.iport.entity.IPAddress;
import com.bwxor.iport.entity.Port;
import com.bwxor.iport.entity.ScanResult;
import com.bwxor.iport.type.ScanStatus;

import java.util.Random;

public class MockQueueScanService extends AbstractQueueScanService {
    private final Random random;

    public MockQueueScanService() {
        random = new Random();
    }

    @Override
    protected ScanResult doScan(IPAddress currentIp, Port port) {
        ScanStatus randomStatus = random.nextBoolean() ? ScanStatus.ACCESSIBLE : ScanStatus.NOT_ACCESSIBLE;
        return new ScanResult(randomStatus, currentIp, port);
    }
}
