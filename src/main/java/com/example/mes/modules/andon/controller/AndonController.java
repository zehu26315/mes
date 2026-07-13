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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/andon")
@RequiredArgsConstructor
public class AndonController {

    private final AndonTypeService andonTypeService;
    private final AndonConfigService andonConfigService;
    private final AndonReasonService andonReasonService;
    private final AndonRecordService andonRecordService;

    // ==================== AndonType ====================

    @PostMapping("/types")
    public Result<AndonType> createType(@RequestBody AndonType andonType) {
        return Result.ok(andonTypeService.create(andonType));
    }

    @PutMapping("/types/{id}")
    public Result<AndonType> updateType(@PathVariable Long id, @RequestBody AndonType andonType) {
        andonType.setId(id);
        return Result.ok(andonTypeService.update(andonType));
    }

    @DeleteMapping("/types/{id}")
    public Result<Void> deleteType(@PathVariable Long id) {
        andonTypeService.delete(id);
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
        return Result.ok(andonConfigService.create(andonConfig));
    }

    @PutMapping("/configs/{id}")
    public Result<AndonConfig> updateConfig(@PathVariable Long id, @RequestBody AndonConfig andonConfig) {
        andonConfig.setId(id);
        return Result.ok(andonConfigService.update(andonConfig));
    }

    @DeleteMapping("/configs/{id}")
    public Result<Void> deleteConfig(@PathVariable Long id) {
        andonConfigService.delete(id);
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
        return Result.ok(andonReasonService.create(andonReason));
    }

    @PutMapping("/reasons/{id}")
    public Result<AndonReason> updateReason(@PathVariable Long id, @RequestBody AndonReason andonReason) {
        andonReason.setId(id);
        return Result.ok(andonReasonService.update(andonReason));
    }

    @DeleteMapping("/reasons/{id}")
    public Result<Void> deleteReason(@PathVariable Long id) {
        andonReasonService.delete(id);
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
        return Result.ok(andonRecordService.create(andonRecord));
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
        return Result.ok(andonRecordService.updateStatus(id, status, handler, resolveDesc));
    }

    @GetMapping("/records/active")
    public Result<List<AndonRecord>> getActiveRecords() {
        return Result.ok(andonRecordService.getActive());
    }

    @DeleteMapping("/records/{id}")
    public Result<Void> deleteRecord(@PathVariable Long id) {
        andonRecordService.delete(id);
        return Result.ok();
    }
}
