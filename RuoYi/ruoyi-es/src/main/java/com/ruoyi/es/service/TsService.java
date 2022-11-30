package com.ruoyi.es.service;


import com.ruoyi.es.bean.mysql.TsBean;

import java.util.List;

/**
 * @author japhet_jiu
 * @version 1.0
 */
public interface TsService {
    List<TsBean> selectUserBeanList(TsBean ub);

    List<TsBean> selectLikeUserBeanList(TsBean ub);


}
