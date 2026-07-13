package com.example.mes.modules.trace.controller;

import com.example.mes.common.response.Result;
import com.example.mes.modules.trace.entity.TraceMaterialBatch;
import com.example.mes.modules.trace.entity.TraceProduct;
import com.example.mes.modules.trace.entity.TraceProductMaterial;
import com.example.mes.modules.trace.service.TraceMaterialBatchService;
import com.example.mes.modules.trace.service.TraceProductMaterialService;
import com.example.mes.modules.trace.service.TraceProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/trace")
@RequiredArgsConstructor
public class TraceController {

    private final TraceProductService traceProductService;
    private final TraceMaterialBatchService traceMaterialBatchService;
    private final TraceProductMaterialService traceProductMaterialService;

    /**
     * 正向追溯：根据产品条码查询完整追溯链
     * 返回产品信息及其关联的所有物料批次信息
     */
    @GetMapping("/product/{barcode}")
    public Result<Map<String, Object>> traceByProduct(@PathVariable String barcode) {
        TraceProduct product = traceProductService.getProductByBarcodeNo(barcode);
        if (product == null) {
            return Result.fail("产品不存在");
        }

        List<TraceProductMaterial> materials =
                traceProductMaterialService.getProductMaterialsByBarcodeNo(barcode);

        List<Map<String, Object>> materialDetails = materials.stream().map(m -> {
            TraceMaterialBatch batch =
                    traceMaterialBatchService.getMaterialBatchByBatchNo(m.getMaterialBatchNo());
            Map<String, Object> detail = new LinkedHashMap<>();
            detail.put("productMaterial", m);
            detail.put("materialBatch", batch);
            return detail;
        }).collect(Collectors.toList());

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("product", product);
        result.put("materials", materialDetails);
        return Result.ok(result);
    }

    /**
     * 反向追溯：根据物料批次号查询该批次用于哪些产品
     * 返回物料批次信息及其关联的所有产品信息
     */
    @GetMapping("/material/{batchNo}")
    public Result<Map<String, Object>> traceByMaterial(@PathVariable String batchNo) {
        TraceMaterialBatch materialBatch =
                traceMaterialBatchService.getMaterialBatchByBatchNo(batchNo);
        if (materialBatch == null) {
            return Result.fail("物料批次不存在");
        }

        List<TraceProductMaterial> productMaterials =
                traceProductMaterialService.getProductMaterialsByMaterialBatchNo(batchNo);

        List<Map<String, Object>> productDetails = productMaterials.stream().map(pm -> {
            TraceProduct product =
                    traceProductService.getProductByBarcodeNo(pm.getBarcodeNo());
            Map<String, Object> detail = new LinkedHashMap<>();
            detail.put("productMaterial", pm);
            detail.put("product", product);
            return detail;
        }).collect(Collectors.toList());

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("materialBatch", materialBatch);
        result.put("products", productDetails);
        return Result.ok(result);
    }

    @PostMapping("/products")
    public Result<TraceProduct> createProduct(@RequestBody TraceProduct product) {
        return Result.ok(traceProductService.createProduct(product));
    }

    @PostMapping("/material-batches")
    public Result<TraceMaterialBatch> createMaterialBatch(@RequestBody TraceMaterialBatch materialBatch) {
        return Result.ok(traceMaterialBatchService.createMaterialBatch(materialBatch));
    }

    @PostMapping("/product-materials")
    public Result<TraceProductMaterial> createProductMaterial(@RequestBody TraceProductMaterial productMaterial) {
        return Result.ok(traceProductMaterialService.createProductMaterial(productMaterial));
    }
}
