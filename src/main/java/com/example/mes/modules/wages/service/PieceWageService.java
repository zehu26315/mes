package com.example.mes.modules.wages.service;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.wages.entity.PieceWage;

public interface PieceWageService {
    PieceWage create(PieceWage entity);
    PieceWage update(PieceWage entity);
    void delete(Long id);
    PieceWage getById(Long id);
    PageResult<PieceWage> list(String keyword, Integer status, String wageDate, int page, int pageSize);
}
