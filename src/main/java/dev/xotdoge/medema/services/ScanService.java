package dev.xotdoge.medema.services;

import dev.xotdoge.medema.entity.Module;
import dev.xotdoge.medema.entity.Scan;
import dev.xotdoge.medema.logic.Page;
import dev.xotdoge.medema.repositories.ScanRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@SuppressWarnings("unused")
public class ScanService {
    public static Scan createScan(String moduleId, byte metal, LocalDateTime dateTime) {
        Module module = ModuleService.getModule(moduleId);
        if (ScanRepository.findScanByMetalAndTime(metal, dateTime) == null) {
            return new Scan(module, metal, dateTime);
        }
        return null;
    }

    public static void saveScan(Scan scan) {
        ScanRepository.save(scan);
    }

    public static List<Scan> getScansForToday(Page page) {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.plusDays(1).atStartOfDay();
        return ScanRepository.findScansByTimeBetweenOrderByTimeDesc(startOfDay, endOfDay, page);
    }

    public static List<Scan> getScansForAllTime(Page page) {
        return ScanRepository.findAllByOrderByDateTimeDesc(page);
    }

    public static List<Scan> getScansInTime(LocalDateTime fromDateTime, LocalDateTime toDateTime) {
        return ScanRepository.findScansByTimeBetweenOrderByTimeDesc(fromDateTime, toDateTime);
    }

    public static List<Scan> getScansByString(String string) {
        List<Scan> scanList;
        try {
            int number = Integer.parseInt(string);
            scanList = ScanRepository.findScansByModuleIdOrModuleLocationOrMetal(string, string, number);
        } catch (NumberFormatException e) {
            scanList = ScanRepository.findScansByModuleIdOrModuleLocation(string, string);
        }
        return scanList;
    }

    public static List<Scan> getScansByParameters(LocalDateTime fromDateTime, LocalDateTime toDateTime, String string) {
        List<Scan> scanList;
        try {
            int number = Integer.parseInt(string);
            scanList = ScanRepository.findScansByModuleIdOrModuleLocationOrMetalAndTimeBetweenOrderByTimeDesc(string, string, number, fromDateTime, toDateTime);
        } catch (NumberFormatException e) {
            scanList = ScanRepository.findScansByModuleIdOrModuleLocationAndTimeBetween(string, string, fromDateTime, toDateTime);
        }
        return scanList;
    }

    public static Scan findScanByDateTime(LocalDateTime dateTime) {
        return ScanRepository.findScanByTime(dateTime);
    }
}