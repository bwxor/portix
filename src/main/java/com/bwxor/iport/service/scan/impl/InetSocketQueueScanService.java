package com.bwxor.iport.service.scan.impl;

import com.bwxor.iport.entity.IPAddress;
import com.bwxor.iport.entity.Port;
import com.bwxor.iport.entity.ScanResult;
import com.bwxor.iport.service.scan.AbstractQueueScanService;
import com.bwxor.iport.type.ScanStatus;

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
