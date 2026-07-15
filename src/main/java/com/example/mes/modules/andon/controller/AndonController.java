package com.example.mes.modules.andon.controller;

import com.example.mes.common.response.PageResult;
import com.example.mes.common.response.Result;
import com.example.mes.modules.andon.entity.AndonConfig;
import com.example.mes.modules.andon.entity.AndonRecord;
import com.example.mes.modules.andon.service.AndonConfigService;
import com.example.mes.modules.andon.service.AndonRecordService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/andon")
@RequiredArgsConstructor
public class AndonController {

    private static final Logger log = LoggerFactory.getLogger(AndonController.class);

    private final AndonConfigService andonConfigService;
    private final AndonRecordService andonRecordService;
    private final SimpMessagingTemplate messagingTemplate;

    // ==================== AndonConfig ====================

    @PostMapping("/configs")
    public Result<AndonConfig> createConfig(@RequestBody AndonConfig andonConfig) {
        AndonConfig result = andonConfigService.create(andonConfig);
        broadcastChange("config", "create", result.getId());
        return Result.ok(result);
    }

    @PutMapping("/configs/{id}")
    public Result<AndonConfig> updateConfig(@PathVariable Long id, @RequestBody AndonConfig andonConfig) {
        andonConfig.setId(id);
        AndonConfig result = andonConfigService.update(andonConfig);
        broadcastChange("config", "update", id);
        return Result.ok(result);
    }

    @DeleteMapping("/configs/{id}")
    public Result<Void> deleteConfig(@PathVariable Long id) {
        andonConfigService.delete(id);
        broadcastChange("config", "delete", id);
        return Result.ok();
    }

    @GetMapping("/configs/{id}")
    public Result<AndonConfig> getConfigById(@PathVariable Long id) {
        return Result.ok(andonConfigService.getById(id));
    }

    @GetMapping("/configs")
    public Result<PageResult<AndonConfig>> listConfigs(@RequestParam(defaultValue = "") String keyword,
                                                        @RequestParam(required = false) String andonType,
                                                        @RequestParam(required = false) Integer status,
                                                        @RequestParam(defaultValue = "1") int page,
                                                        @RequestParam(defaultValue = "10") int pageSize) {
        return Result.ok(andonConfigService.list(keyword, andonType, status, page, pageSize));
    }

    // ==================== AndonRecord ====================

    @PostMapping("/records")
    public Result<AndonRecord> createRecord(@RequestBody AndonRecord andonRecord) {
        AndonRecord result = andonRecordService.create(andonRecord);
        broadcastChange("record", "create", result.getId());
        return Result.ok(result);
    }

    @GetMapping("/records/{id}")
    public Result<AndonRecord> getRecordById(@PathVariable Long id) {
        return Result.ok(andonRecordService.getById(id));
    }

    @GetMapping("/records")
    public Result<PageResult<AndonRecord>> listRecords(@RequestParam(defaultValue = "") String keyword,
                                                        @RequestParam(required = false) Long andonConfigId,
                                                        @RequestParam(required = false) Integer status,
                                                        @RequestParam(required = false) String productionLine,
                                                        @RequestParam(required = false) String startTime,
                                                        @RequestParam(required = false) String endTime,
                                                        @RequestParam(defaultValue = "1") int page,
                                                        @RequestParam(defaultValue = "10") int pageSize) {
        return Result.ok(andonRecordService.list(keyword, andonConfigId, status, productionLine,
                startTime, endTime, page, pageSize));
    }

    @PutMapping("/records/{id}")
    public Result<AndonRecord> updateRecord(@PathVariable Long id, @RequestBody AndonRecord andonRecord) {
        andonRecord.setId(id);
        AndonRecord result = andonRecordService.update(andonRecord);
        broadcastChange("record", "update", id);
        return Result.ok(result);
    }

    @GetMapping("/records/active")
    public Result<List<AndonRecord>> getActiveRecords() {
        return Result.ok(andonRecordService.getActive());
    }

    @DeleteMapping("/records/{id}")
    public Result<Void> deleteRecord(@PathVariable Long id) {
        andonRecordService.delete(id);
        broadcastChange("record", "delete", id);
        return Result.ok();
    }

    // ==================== WebSocket broadcast ====================

    private void broadcastChange(String type, String action, Long id) {
        try {
            Map<String, Object> msg = new HashMap<>();
            msg.put("type", type);
            msg.put("action", action);
            msg.put("id", id);
            msg.put("time", LocalDateTime.now().toString());
            messagingTemplate.convertAndSend("/topic/andon", (Object) msg);
        } catch (Exception e) {
            log.warn("Failed to broadcast andon change: {}", e.getMessage());
        }
    }
}
