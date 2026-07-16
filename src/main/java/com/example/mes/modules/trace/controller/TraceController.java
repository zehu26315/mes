package com.example.mes.modules.trace.controller;

import com.example.mes.common.response.PageResult;
import com.example.mes.common.response.Result;
import com.example.mes.modules.trace.entity.MaterialTrace;
import com.example.mes.modules.trace.entity.ProductTrace;
import com.example.mes.modules.trace.service.MaterialTraceService;
import com.example.mes.modules.trace.service.ProductTraceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trace")
@RequiredArgsConstructor
public class TraceController {

    private final MaterialTraceService materialService;
    private final ProductTraceService productService;

    // ==================== Material Trace ====================

    @GetMapping("/materials")
    public Result<PageResult<MaterialTrace>> listMaterials(@RequestParam(required = false) Long orderId,
                                                            @RequestParam(defaultValue = "1") int page,
                                                            @RequestParam(defaultValue = "10") int pageSize) {
        return Result.ok(materialService.list(orderId, page, pageSize));
    }

    @GetMapping("/materials/{id}")
    public Result<MaterialTrace> getMaterial(@PathVariable Long id) {
        return Result.ok(materialService.getById(id));
    }

    @PostMapping("/materials")
    public Result<MaterialTrace> createMaterial(@RequestBody MaterialTrace entity) {
        return Result.ok(materialService.create(entity));
    }

    @PutMapping("/materials/{id}")
    public Result<MaterialTrace> updateMaterial(@PathVariable Long id, @RequestBody MaterialTrace entity) {
        entity.setId(id);
        return Result.ok(materialService.update(entity));
    }

    @DeleteMapping("/materials/{id}")
    public Result<Void> deleteMaterial(@PathVariable Long id) {
        materialService.delete(id);
        return Result.ok();
    }

    // ==================== Product Trace ====================

    @GetMapping("/products")
    public Result<PageResult<ProductTrace>> listProducts(@RequestParam(required = false) String keyword,
                                                          @RequestParam(defaultValue = "1") int page,
                                                          @RequestParam(defaultValue = "10") int pageSize) {
        return Result.ok(productService.list(keyword, page, pageSize));
    }

    @GetMapping("/products/{id}")
    public Result<ProductTrace> getProduct(@PathVariable Long id) {
        return Result.ok(productService.getById(id));
    }

    @PostMapping("/products")
    public Result<ProductTrace> createProduct(@RequestBody ProductTrace entity) {
        return Result.ok(productService.create(entity));
    }

    @PutMapping("/products/{id}")
    public Result<ProductTrace> updateProduct(@PathVariable Long id, @RequestBody ProductTrace entity) {
        entity.setId(id);
        return Result.ok(productService.update(entity));
    }

    @DeleteMapping("/products/{id}")
    public Result<Void> deleteProduct(@PathVariable Long id) {
        productService.delete(id);
        return Result.ok();
    }
}
