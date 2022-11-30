package com.ruoyi.es.serviceImpl;

import com.ruoyi.es.bean.mysql.TsBean;
import com.ruoyi.es.mapper.TsMapper;
import com.ruoyi.es.service.TsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author japhet_jiu
 * @version 1.0
 */
@Service
public class TsServiceImpl implements TsService {

    //将DAO注入Service层
    @Autowired
    private TsMapper tsMapper;

    @Override
    public List<TsBean> selectUserBeanList(TsBean ub) {
        return tsMapper.selectUserBeanList(ub);
    }

    @Override
    public List<TsBean> selectLikeUserBeanList(TsBean ub) {
        return tsMapper.selectLikeUserBeanList(ub);
    }


}
