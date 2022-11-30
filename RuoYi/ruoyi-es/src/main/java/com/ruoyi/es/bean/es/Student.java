package com.ruoyi.es.bean.es;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student {
    private Long id;
    private String name;
    private Long age;
    private Double height;
    private String remark;
    private boolean isHealth;
    private String IP;
    private String SrcIP;
}

