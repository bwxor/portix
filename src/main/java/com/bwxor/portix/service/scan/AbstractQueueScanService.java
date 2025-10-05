package com.bwxor.portix.service.scan;

import com.bwxor.portix.entity.IPAddress;
import com.bwxor.portix.entity.Port;
import com.bwxor.portix.entity.ScanResult;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

public abstract class AbstractQueueScanService implements ScanService {
    protected ConcurrentLinkedQueue<ScanResult> scanQueue;
    protected boolean stopped;

    @Override
    public synchronized void scan(IPAddress start, IPAddress end, List<Port> ports, int timeout) {
        scanQueue = new ConcurrentLinkedQueue<>();

        IPAddress currentIp = start;

        while (!currentIp.after(end) && !isStopped()) {
            for (var p : ports) {
                if (isStopped()) {
                    return;
                }

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

    public void setScanQueue(ConcurrentLinkedQueue<ScanResult> scanQueue) {
        this.scanQueue = scanQueue;
    }

    public void stop() {
        stopped = true;
    }

    public boolean isStopped() {
        return stopped;
    }
}
