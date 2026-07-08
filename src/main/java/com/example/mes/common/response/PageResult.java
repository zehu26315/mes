package com.example.mes.common.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> {

    private long total;
    private int page;
    private int pageSize;
    private List<T> rows;

    public static <T> PageResult<T> of(long total, int page, int pageSize, List<T> rows) {
        return new PageResult<>(total, page, pageSize, rows);
    }
}
