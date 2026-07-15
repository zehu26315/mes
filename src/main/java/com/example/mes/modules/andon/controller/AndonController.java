package com.example.mes.modules.andon.controller;

import com.example.mes.common.response.PageResult;
import com.example.mes.common.response.Result;
import com.example.mes.common.response.TreeNode;
import com.example.mes.modules.andon.entity.AndonConfig;
import com.example.mes.modules.andon.entity.AndonReason;
import com.example.mes.modules.andon.entity.AndonRecord;
import com.example.mes.modules.andon.entity.AndonType;
import com.example.mes.modules.andon.service.AndonConfigService;
import com.example.mes.modules.andon.service.AndonReasonService;
import com.example.mes.modules.andon.service.AndonRecordService;
import com.example.mes.modules.andon.service.AndonTypeService;
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

    private final AndonTypeService andonTypeService;
    private final AndonConfigService andonConfigService;
    private final AndonReasonService andonReasonService;
    private final AndonRecordService andonRecordService;
    private final SimpMessagingTemplate messagingTemplate;

    // ==================== AndonType ====================

    @PostMapping("/types")
    public Result<AndonType> createType(@RequestBody AndonType andonType) {
        AndonType result = andonTypeService.create(andonType);
        broadcastChange("type", "create", result.getId());
        return Result.ok(result);
    }

    @PutMapping("/types/{id}")
    public Result<AndonType> updateType(@PathVariable Long id, @RequestBody AndonType andonType) {
        andonType.setId(id);
        AndonType result = andonTypeService.update(andonType);
        broadcastChange("type", "update", id);
        return Result.ok(result);
    }

    @DeleteMapping("/types/{id}")
    public Result<Void> deleteType(@PathVariable Long id) {
        andonTypeService.delete(id);
        broadcastChange("type", "delete", id);
        return Result.ok();
    }

    @GetMapping("/types/{id}")
    public Result<AndonType> getTypeById(@PathVariable Long id) {
        return Result.ok(andonTypeService.getById(id));
    }

    @GetMapping("/types")
    public Result<PageResult<AndonType>> listTypes(@RequestParam(defaultValue = "") String keyword,
                                                    @RequestParam(defaultValue = "1") int page,
                                                    @RequestParam(defaultValue = "10") int pageSize) {
        return Result.ok(andonTypeService.list(keyword, page, pageSize));
    }

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
                                                        @RequestParam(required = false) Long andonTypeId,
                                                        @RequestParam(required = false) Integer status,
                                                        @RequestParam(defaultValue = "1") int page,
                                                        @RequestParam(defaultValue = "10") int pageSize) {
        return Result.ok(andonConfigService.list(keyword, andonTypeId, status, page, pageSize));
    }

    // ==================== AndonReason ====================

    @PostMapping("/reasons")
    public Result<AndonReason> createReason(@RequestBody AndonReason andonReason) {
        AndonReason result = andonReasonService.create(andonReason);
        broadcastChange("reason", "create", result.getId());
        return Result.ok(result);
    }

    @PutMapping("/reasons/{id}")
    public Result<AndonReason> updateReason(@PathVariable Long id, @RequestBody AndonReason andonReason) {
        andonReason.setId(id);
        AndonReason result = andonReasonService.update(andonReason);
        broadcastChange("reason", "update", id);
        return Result.ok(result);
    }

    @DeleteMapping("/reasons/{id}")
    public Result<Void> deleteReason(@PathVariable Long id) {
        andonReasonService.delete(id);
        broadcastChange("reason", "delete", id);
        return Result.ok();
    }

    @GetMapping("/reasons/{id}")
    public Result<AndonReason> getReasonById(@PathVariable Long id) {
        return Result.ok(andonReasonService.getById(id));
    }

    @GetMapping("/reasons")
    public Result<PageResult<AndonReason>> listReasons(@RequestParam(defaultValue = "") String keyword,
                                                        @RequestParam(required = false) Long andonTypeId,
                                                        @RequestParam(required = false) Integer status,
                                                        @RequestParam(defaultValue = "1") int page,
                                                        @RequestParam(defaultValue = "10") int pageSize) {
        return Result.ok(andonReasonService.list(keyword, andonTypeId, status, page, pageSize));
    }

    @GetMapping("/reasons/tree")
    public Result<List<TreeNode<AndonReason>>> getReasonTree() {
        return Result.ok(andonReasonService.getTree());
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
                                                        @RequestParam(required = false) Long andonTypeId,
                                                        @RequestParam(required = false) String status,
                                                        @RequestParam(required = false) String workCenter,
                                                        @RequestParam(required = false) String startTime,
                                                        @RequestParam(required = false) String endTime,
                                                        @RequestParam(defaultValue = "1") int page,
                                                        @RequestParam(defaultValue = "10") int pageSize) {
        return Result.ok(andonRecordService.list(keyword, andonTypeId, status, workCenter,
                startTime, endTime, page, pageSize));
    }

    @PutMapping("/records/{id}/status")
    public Result<AndonRecord> updateRecordStatus(@PathVariable Long id,
                                                   @RequestBody Map<String, String> body) {
        String status = body.get("status");
        String handler = body.get("handler");
        String resolveDesc = body.get("resolveDesc");
        AndonRecord result = andonRecordService.updateStatus(id, status, handler, resolveDesc);
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
