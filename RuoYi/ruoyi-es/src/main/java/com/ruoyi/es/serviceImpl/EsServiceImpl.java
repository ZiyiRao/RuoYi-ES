package com.ruoyi.es.serviceImpl;

import com.ruoyi.es.bean.es.EsUserBean;
import com.ruoyi.es.service.EsService;
import org.springframework.data.elasticsearch.repository.support.AbstractElasticsearchRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 刘玖玖
 * @version 1.0
 */
@Service
public class EsServiceImpl extends AbstractElasticsearchRepository<EsUserBean, Integer> implements EsService {

//    @Autowired
//    private EsMapper esMapper;
    @Override
    protected String stringIdRepresentation(Integer integer) {
        return null;
    }
//    @Override
//    public List<EsUserBean> findAll(EsUserBean es) {
//
////        return esService.findByIP("10.156.17.22");
//        return null;
//    }
    @Override
    public EsUserBean queryEmployeeById(String id) {
        return null;
    }

}