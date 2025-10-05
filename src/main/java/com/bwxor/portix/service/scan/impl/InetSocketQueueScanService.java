package com.bwxor.portix.service.scan.impl;

import com.bwxor.portix.entity.IPAddress;
import com.bwxor.portix.entity.Port;
import com.bwxor.portix.entity.ScanResult;
import com.bwxor.portix.service.scan.AbstractQueueScanService;
import com.bwxor.portix.type.ScanStatus;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class InetSocketQueueScanService extends AbstractQueueScanService {
    @Override
    protected ScanResult doScan(IPAddress currentIp, Port port, int timeout) {
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(currentIp.toString(), port.getValue()), timeout);
            socket.close();
            return new ScanResult(ScanStatus.ACCESSIBLE, currentIp, port);
        } catch (IOException e) {
            return new ScanResult(ScanStatus.NOT_ACCESSIBLE, currentIp, port);
        }
    }
}
