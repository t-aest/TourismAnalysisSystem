package com.taest.tourismdatavisualization.utils;

import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;
public class SystemUtils {

    private static OperatingSystemMXBean osmxb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

    /**
     * 获取系统cpu占用情况
     * @return
     */
    private static int cpuload(){
        double cpuLoad = osmxb.getSystemCpuLoad();
        int percentCpuLoad = (int) (cpuLoad * 100);
        return percentCpuLoad;
    }

    /**
     * 获取系统内存占用情况
     * @return
     */
    public static int memoryLoad() {
        double totalvirtualMemory = osmxb.getTotalPhysicalMemorySize();
        double freePhysicalMemorySize = osmxb.getFreePhysicalMemorySize();

        double value = freePhysicalMemorySize/totalvirtualMemory;
        int percentMemoryLoad = (int) ((1-value)*100);
        return percentMemoryLoad;
    }

    public static void main(String[] args) {
        System.out.println(cpuload());
        System.out.println(memoryLoad());
    }

}
