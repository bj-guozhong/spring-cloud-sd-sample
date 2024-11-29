package com.cmg.spcl.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
