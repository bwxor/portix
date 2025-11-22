package com.bwxor.portix.service.scan;

import com.bwxor.portix.entity.IPAddress;
import com.bwxor.portix.entity.Port;
import com.bwxor.portix.entity.ScanResult;

public interface QueueScanner {
    ScanResult doScan(IPAddress currentIp, Port port, int timeout);
}
