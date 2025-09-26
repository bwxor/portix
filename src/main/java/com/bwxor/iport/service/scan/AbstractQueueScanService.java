package com.bwxor.iport.service.scan;

import com.bwxor.iport.entity.IPAddress;
import com.bwxor.iport.entity.Port;
import com.bwxor.iport.entity.ScanResult;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

public abstract class AbstractQueueScanService implements ScanService {
    protected ConcurrentLinkedQueue<ScanResult> scanQueue;

    @Override
    public synchronized void scan(IPAddress start, IPAddress end, List<Port> ports, int timeout) {
        scanQueue = new ConcurrentLinkedQueue<>();

        IPAddress currentIp = start;

        while (!currentIp.after(end)) {
            for (var p : ports) {
                ScanResult scanResult = doScan(new IPAddress(currentIp.toString()), p, timeout);
                scanQueue.add(scanResult);
            }

            currentIp.increment();
        }
    }

    public ConcurrentLinkedQueue<ScanResult> getScanQueue() {
        return scanQueue;
    }

    protected abstract ScanResult doScan(IPAddress currentIp, Port port, int timeout);
}
