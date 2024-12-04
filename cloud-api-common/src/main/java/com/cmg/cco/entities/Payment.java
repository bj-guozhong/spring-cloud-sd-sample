package com.cmg.cco.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

/**
 * @version 1.0
 * @Author zgz
 * @Date 2024/12/2 11:03
 * @Description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment implements Serializable {
    private Long id;
    private String sid;

}
