package com.ruoyi.es.service;

import com.ruoyi.es.bean.es.EsNginxBean;
import com.ruoyi.es.bean.es.EsUserBean;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;


/**
 * @author japhet_jiu
 * @version 1.0
 */
public interface EsService extends ElasticsearchRepository<EsUserBean,Integer> {
    /**
     * 查询所有
     * @param
     * @return
     */
//    List<EsUserBean> findAll(EsUserBean es);
    EsUserBean queryEmployeeById(String id);


    List selectEsBean(String index,String bean);
    List selectField(String index,String bean,String field,String keyword);
//    List<EsNginxBean> selectEsNginxBean(String index);
}

