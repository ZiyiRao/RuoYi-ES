package com.ruoyi.es.bean.mysql;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author japhet_jiu
 * @version 1.0
 */
@Data
@Table(name = "ts_info")
@Entity
public class TsBean {
    @Id //标识主键
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 主键自增
    private Integer id;
    private String ts;

}
