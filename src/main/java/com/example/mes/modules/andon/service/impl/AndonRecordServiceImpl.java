package com.example.mes.modules.andon.service.impl;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.andon.entity.AndonRecord;
import com.example.mes.modules.andon.mapper.AndonRecordMapper;
import com.example.mes.modules.andon.service.AndonRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AndonRecordServiceImpl implements AndonRecordService {

    private final AndonRecordMapper andonRecordMapper;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    @Transactional
    public AndonRecord create(AndonRecord andonRecord) {
        andonRecord.setStatus("REPORTED");
        andonRecord.setReportTime(LocalDateTime.now());
        if (andonRecord.getSeverity() == null) {
            andonRecord.setSeverity("NORMAL");
        }
        andonRecordMapper.insert(andonRecord);
        return andonRecord;
    }

    @Override
    public AndonRecord getById(Long id) {
        return andonRecordMapper.selectById(id);
    }

    @Override
    public PageResult<AndonRecord> list(String keyword, Long andonTypeId, String status, String workCenter,
                                        String startTime, String endTime, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        LocalDateTime start = null;
        LocalDateTime end = null;
        if (startTime != null && !startTime.isEmpty()) {
            start = LocalDateTime.parse(startTime, FORMATTER);
        }
        if (endTime != null && !endTime.isEmpty()) {
            end = LocalDateTime.parse(endTime, FORMATTER);
        }
        List<AndonRecord> rows = andonRecordMapper.selectList(keyword, andonTypeId, status, workCenter,
                start, end, offset, pageSize);
        long total = andonRecordMapper.countList(keyword, andonTypeId, status, workCenter, start, end);
        return PageResult.of(total, page, pageSize, rows);
    }

    @Override
    @Transactional
    public AndonRecord updateStatus(Long id, String status, String handler, String resolveDesc) {
        AndonRecord record = andonRecordMapper.selectById(id);
        if (record == null) {
            throw new RuntimeException("Andon record not found: " + id);
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime confirmTime = null;
        LocalDateTime resolveTime = null;
        Long duration = null;

        switch (status) {
            case "CONFIRMED":
                if (!"REPORTED".equals(record.getStatus())) {
                    throw new RuntimeException("Only REPORTED records can be confirmed, current status: " + record.getStatus());
                }
                confirmTime = now;
                break;
            case "RESOLVED":
                if (!"CONFIRMED".equals(record.getStatus())) {
                    throw new RuntimeException("Only CONFIRMED records can be resolved, current status: " + record.getStatus());
                }
                resolveTime = now;
                if (record.getConfirmTime() != null) {
                    duration = Duration.between(record.getConfirmTime(), resolveTime).toMinutes();
                } else if (record.getReportTime() != null) {
                    duration = Duration.between(record.getReportTime(), resolveTime).toMinutes();
                }
                break;
            default:
                throw new RuntimeException("Invalid status transition to: " + status);
        }

        andonRecordMapper.updateStatus(id, status, handler, confirmTime, resolveTime, resolveDesc, duration);
        return andonRecordMapper.selectById(id);
    }

    @Override
    public List<AndonRecord> getActive() {
        return andonRecordMapper.selectActive();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        andonRecordMapper.deleteById(id);
    }
}
