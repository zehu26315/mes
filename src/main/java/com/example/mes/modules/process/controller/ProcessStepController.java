package com.example.mes.modules.process.controller;

import com.example.mes.common.response.Result;
import com.example.mes.modules.process.entity.ProcessStep;
import com.example.mes.modules.process.service.ProcessStepService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/process")
@RequiredArgsConstructor
public class ProcessStepController {

    private final ProcessStepService processStepService;

    @GetMapping("/routes/{routeId}/steps")
    public Result<List<ProcessStep>> listByRoute(@PathVariable Long routeId) {
        return Result.ok(processStepService.listStepsByRouteId(routeId));
    }

    @PostMapping("/routes/{routeId}/steps")
    public Result<ProcessStep> create(@PathVariable Long routeId, @RequestBody ProcessStep step) {
        return Result.ok(processStepService.createStep(routeId, step));
    }

    @PutMapping("/steps/{id}")
    public Result<ProcessStep> update(@PathVariable Long id, @RequestBody ProcessStep step) {
        step.setId(id);
        return Result.ok(processStepService.updateStep(step));
    }

    @DeleteMapping("/steps/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        processStepService.deleteStep(id);
        return Result.ok();
    }

    @GetMapping("/steps/{id}")
    public Result<ProcessStep> getById(@PathVariable Long id) {
        return Result.ok(processStepService.getStepById(id));
    }

    @PutMapping("/steps/{id}/sort")
    public Result<ProcessStep> updateSortOrder(@PathVariable Long id, @RequestBody Map<String, Integer> body) {
        return Result.ok(processStepService.updateSortOrder(id, body.get("sortOrder")));
    }
}
