package com.example.mes.modules.masterdata.controller;

import com.example.mes.common.response.PageResult;
import com.example.mes.common.response.Result;
import com.example.mes.modules.masterdata.entity.MdProduct;
import com.example.mes.modules.masterdata.service.MdProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/masterdata/products")
@RequiredArgsConstructor
public class MdProductController {

    private final MdProductService mdProductService;

    @PostMapping
    public Result<MdProduct> create(@RequestBody MdProduct product) {
        return Result.ok(mdProductService.createProduct(product));
    }

    @PutMapping("/{id}")
    public Result<MdProduct> update(@PathVariable Long id, @RequestBody MdProduct product) {
        product.setId(id);
        return Result.ok(mdProductService.updateProduct(product));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        mdProductService.deleteProduct(id);
        return Result.ok();
    }

    @GetMapping("/{id}")
    public Result<MdProduct> getById(@PathVariable Long id) {
        return Result.ok(mdProductService.getProductById(id));
    }

    @GetMapping
    public Result<PageResult<MdProduct>> list(@RequestParam(defaultValue = "") String keyword,
                                               @RequestParam(required = false) Integer status,
                                               @RequestParam(defaultValue = "1") int page,
                                               @RequestParam(defaultValue = "10") int pageSize) {
        return Result.ok(mdProductService.listProducts(keyword, status, page, pageSize));
    }
}
