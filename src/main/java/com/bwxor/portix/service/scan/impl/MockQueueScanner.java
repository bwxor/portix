package com.bwxor.portix.service.scan.impl;

import com.bwxor.portix.entity.IPAddress;
import com.bwxor.portix.entity.Port;
import com.bwxor.portix.entity.ScanResult;
import com.bwxor.portix.service.scan.QueueScanner;
import com.bwxor.portix.type.ScanStatus;

import java.util.Random;

public class MockQueueScanner implements QueueScanner {
    private final Random random;

    public MockQueueScanner() {
        random = new Random();
    }

    @Override
    public ScanResult doScan(IPAddress currentIp, Port port, int timeout) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        ScanStatus randomStatus = random.nextBoolean() ? ScanStatus.ACCESSIBLE : ScanStatus.NOT_ACCESSIBLE;
        return new ScanResult(randomStatus, currentIp, port);
    }
}
