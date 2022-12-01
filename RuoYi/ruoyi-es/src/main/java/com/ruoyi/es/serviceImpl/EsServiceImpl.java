package com.ruoyi.es.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.es.bean.es.EsUserBean;
import com.ruoyi.es.bean.es.EsNginxBean;
import com.ruoyi.es.service.EsService;
import net.sf.json.JSONArray;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.data.elasticsearch.repository.support.AbstractElasticsearchRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
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

    @Override
    public EsUserBean queryEmployeeById(String id) {
        return null;
    }

    @Override
    public List selectEsBean(String index,String bean){
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost", 9200, "http"),
                        new HttpHost("localhost", 9201, "http")));
        List resultList = new LinkedList();		//返回的结果list

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder()     //创建 查询 对象
                .size(100);                         //返结果size
        //                .sort(new FieldSortBuilder("@timestamp")      //排序
//                .order(SortOrder.DESC));                     //正序
        SearchRequest rq = new SearchRequest(index)  //请求 索引库
                .source(sourceBuilder);						 //组装查询条件
        try {
            sourceBuilder.query(QueryBuilders.matchAllQuery());
            rq.source(sourceBuilder);
            SearchResponse resp = client.search(rq, RequestOptions.DEFAULT);
            for (SearchHit hit : resp.getHits()) {
                com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(hit.toString());
                String r = jsonObject.getString("_source");
                if(bean=="EsNginxBean"){
                    EsNginxBean t = JSON.parseObject(r, EsNginxBean.class);
                    resultList.add(t);
                }else if(bean=="EsUserBean"){
                    EsUserBean t = JSON.parseObject(r, EsUserBean.class);
                    resultList.add(t);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        } finally {
            try {
                client.close();    //关闭连接
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resultList;
    }

    @Override
    public List selectField(String index,String bean,String field,String keyword){
    RestHighLevelClient client = new RestHighLevelClient(
            RestClient.builder(
                    new HttpHost("localhost", 9200, "http"),
                    new HttpHost("localhost", 9201, "http")));
    List resultList = new LinkedList();		//返回的结果list

    SearchSourceBuilder sourceBuilder = new SearchSourceBuilder()     //创建 查询 对象
            .size(20);                         //返结果size
    //                .sort(new FieldSortBuilder("@timestamp")      //排序
//                .order(SortOrder.DESC));                     //正序
    SearchRequest rq = new SearchRequest(index)  //请求 索引库
            .source(sourceBuilder);						 //组装查询条件
        try {
        sourceBuilder.query(QueryBuilders.matchQuery(field,keyword));
//            sourceBuilder.query(QueryBuilders.matchAllQuery());
        rq.source(sourceBuilder);
        SearchResponse resp = client.search(rq, RequestOptions.DEFAULT);
        for (SearchHit hit : resp.getHits()) {
            com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(hit.toString());
            String r = jsonObject.getString("_source");
            if(bean=="EsNginxBean"){
                EsNginxBean t = JSON.parseObject(r, EsNginxBean.class);
                resultList.add(t);
            }else if(bean=="EsUserBean"){
                EsUserBean t = JSON.parseObject(r, EsUserBean.class);
                resultList.add(t);
            }
        }
    }catch(Exception e){
        e.printStackTrace();
    } finally {
        try {
            client.close();    //关闭连接
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
        return resultList;
}
}