package com.cmg.cco.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @version 1.0
 * @Author zgz
 * @Date 2024/12/2 11:03
 * @Description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResult<T> {
    //状态码
    private Integer code;

    private String  message;

    private T       data;

    public CommonResult(Integer code, String message) {
        this(code, message, null); // 正确的写法是将 null 传给 data
    }
}
