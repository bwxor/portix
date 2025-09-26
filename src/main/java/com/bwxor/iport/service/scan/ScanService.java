package com.bwxor.iport.service.scan;

import com.bwxor.iport.entity.IPAddress;
import com.bwxor.iport.entity.Port;

import java.util.List;

public interface ScanService {
    void scan(IPAddress start, IPAddress end, List<Port> ports);
}
