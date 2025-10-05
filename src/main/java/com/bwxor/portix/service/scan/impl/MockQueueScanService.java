package com.bwxor.portix.service.scan.impl;

import com.bwxor.portix.entity.IPAddress;
import com.bwxor.portix.entity.Port;
import com.bwxor.portix.entity.ScanResult;
import com.bwxor.portix.service.scan.AbstractQueueScanService;
import com.bwxor.portix.type.ScanStatus;

import java.util.Random;

public class MockQueueScanService extends AbstractQueueScanService {
    private final Random random;

    public MockQueueScanService() {
        random = new Random();
    }

    @Override
    protected ScanResult doScan(IPAddress currentIp, Port port, int timeout) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        ScanStatus randomStatus = random.nextBoolean() ? ScanStatus.ACCESSIBLE : ScanStatus.NOT_ACCESSIBLE;
        return new ScanResult(randomStatus, currentIp, port);
    }
}
