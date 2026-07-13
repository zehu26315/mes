package com.example.mes.modules.process.service;

import com.example.mes.modules.process.entity.ProcessStep;

import java.util.List;

public interface ProcessStepService {

    ProcessStep createStep(Long routeId, ProcessStep step);

    ProcessStep updateStep(ProcessStep step);

    void deleteStep(Long id);

    ProcessStep getStepById(Long id);

    List<ProcessStep> listStepsByRouteId(Long routeId);

    ProcessStep updateSortOrder(Long id, Integer sortOrder);
}
