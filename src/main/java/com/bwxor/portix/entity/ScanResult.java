package com.bwxor.portix.entity;

import com.bwxor.portix.type.ScanStatus;

public class ScanResult {
    private final ScanStatus status;
    private final IPAddress ipAddress;
    private final Port port;

    public ScanResult(ScanStatus status, IPAddress ipAddress, Port port) {
        this.status = status;
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public ScanStatus getStatus() {
        return status;
    }

    public IPAddress getIpAddress() {
        return ipAddress;
    }

    public Port getPort() {
        return port;
    }
}
