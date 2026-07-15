package com.example.mes.common.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PageResult 分页响应")
class PageResultTest {

    @Test
    @DisplayName("of() → 字段正确映射")
    void of() {
        List<String> rows = List.of("a", "b", "c");

        PageResult<String> result = PageResult.of(100L, 2, 10, rows);

        assertEquals(100L, result.getTotal());
        assertEquals(2, result.getPage());
        assertEquals(10, result.getPageSize());
        assertSame(rows, result.getRows());
    }
}
