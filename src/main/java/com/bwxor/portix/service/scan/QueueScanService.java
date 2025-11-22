package com.bwxor.portix.service.scan;

import com.bwxor.portix.entity.IPAddress;
import com.bwxor.portix.entity.Port;
import com.bwxor.portix.entity.ScanResult;
import com.bwxor.portix.service.scan.exception.UninitializedQueueScannerException;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueScanService implements ScanService {
    protected QueueScanner queueScanner;
    protected ConcurrentLinkedQueue<ScanResult> scanQueue;
    protected boolean stopped;

    @Override
    public synchronized void scan(IPAddress start, IPAddress end, List<Port> ports, int timeout) {
        if (queueScanner == null) {
            throw new UninitializedQueueScannerException();
        }

        scanQueue = new ConcurrentLinkedQueue<>();

        IPAddress currentIp = start;

        while (!currentIp.after(end) && !isStopped()) {
            for (var p : ports) {
                if (isStopped()) {
                    return;
                }

                ScanResult scanResult = queueScanner.doScan(new IPAddress(currentIp.toString()), p, timeout);
                scanQueue.add(scanResult);
            }

            currentIp.increment();
        }
    }

    public void setQueueScanner(QueueScanner queueScanner) {
        this.queueScanner = queueScanner;
    }

    public ConcurrentLinkedQueue<ScanResult> getScanQueue() {
        return scanQueue;
    }

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
