package com.bwxor.portix.service.scan;

import com.bwxor.portix.entity.IPAddress;
import com.bwxor.portix.entity.Port;

import java.util.List;

public interface ScanService {
    void scan(IPAddress start, IPAddress end, List<Port> ports, int timeout);
    void stop();
    boolean isStopped();
}
