package com.ruoyi.es.mapper;

import com.ruoyi.es.bean.mysql.TsBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TsMapper {
    List<TsBean> selectUserBeanList(TsBean ub);

    List<TsBean> selectLikeUserBeanList(TsBean ub);

}
