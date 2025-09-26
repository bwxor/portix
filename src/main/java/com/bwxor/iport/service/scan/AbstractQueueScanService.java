package com.bwxor.iport.service.scan;

import com.bwxor.iport.entity.IPAddress;
import com.bwxor.iport.entity.Port;
import com.bwxor.iport.entity.ScanResult;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

public abstract class AbstractQueueScanService implements ScanService {
    protected AtomicLong noProcessed;
    protected ConcurrentLinkedQueue<ScanResult> scanQueue;

    @Override
    public synchronized void scan(IPAddress start, IPAddress end, List<Port> ports) {
        noProcessed = new AtomicLong(0);
        scanQueue = new ConcurrentLinkedQueue<>();

        IPAddress currentIp = start;

        while (!currentIp.after(end)) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            for (var p : ports) {
                ScanResult scanResult = doScan(new IPAddress(currentIp.toString()), p);
                scanQueue.add(scanResult);
            }

            currentIp.increment();
            noProcessed.incrementAndGet();
        }
    }

    public AtomicLong getNoProcessed() {
        return noProcessed;
    }

    public ConcurrentLinkedQueue<ScanResult> getScanQueue() {
        return scanQueue;
    }

    protected abstract ScanResult doScan(IPAddress currentIp, Port port);
}
