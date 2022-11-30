package com.ruoyi;

import com.alibaba.fastjson.JSON;
import com.ruoyi.es.bean.es.Student;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EsTest {

    private RestHighLevelClient client;

    //初始化开启
    @Before
    public void init(){
        client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("192.168.3.10", 9200, "http")));
    }
    //初始化关闭
    @After
    public void close(){
        if(client!=null){
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //新增
    @Test
    public void add(){
        Student student = new Student(1L,"张三",11L,1.50,"张三备注",false);
        String str = JSON.toJSONString(student);
        IndexRequest indexRequest = new IndexRequest("studentinfo", "student", "1");
        indexRequest.source(str, XContentType.JSON);
        try {
            IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
            System.out.println("响应结果: "+JSON.toJSONString(indexResponse));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //批量新增
    @Test
    public void addList(){
        BulkRequest bulkRequest = new BulkRequest();
        for (long i = 2; i <=10 ; i++) {
            Student student = new Student(i,"张三"+i,11+i,1.50,"张三备注"+i,false);
            String str = JSON.toJSONString(student);
            IndexRequest indexRequest = new IndexRequest("studentinfo", "student", i + "");
            indexRequest.source(str,XContentType.JSON);
            bulkRequest.add(indexRequest);
            try {
                BulkResponse bulk = client.bulk(bulkRequest, RequestOptions.DEFAULT);
                System.out.println("响应结果: "+bulk);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //删除
    @Test
    public  void delete(){
        String id = "1";
        DeleteRequest deleteRequest = new DeleteRequest("studentinfo", "student",id);
        try {
            DeleteResponse delete = client.delete(deleteRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //查询
    @Test
    public  void get(){
        String id = "1";
        GetRequest getRequest = new GetRequest("studentinfo", "student", id);
        try {
            GetResponse documentFields = client.get(getRequest,RequestOptions.DEFAULT);
            System.out.println("响应结果: "+documentFields);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //查询 match
    @Test
    public void getMatch(){
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("title", "张");
        commonSearch(matchQueryBuilder);
    }

    //查询 match_all
    @Test
    public void getMatchAll(){
        MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();
        commonSearch(matchAllQueryBuilder);
    }

    //高亮查询
    @Test
    public void hightSearch(){
        MatchQueryBuilder queryBuilder = QueryBuilders.matchQuery("name", "张");
        //构建searchRequest请求对象，指定索引库
        SearchRequest searchRequest = new SearchRequest("studentinfo");
        //构建searchSourceBuilder查询对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //将queryBuilder 对象设置到searchSourceBuilder中
        searchSourceBuilder.query(queryBuilder);

        //高亮查询条件
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<b color='red'>");
        highlightBuilder.postTags("</b>");
        highlightBuilder.field("title");

        //将searchSourceBuilder查询对象封装到请求对象 SearchRequest中
        searchRequest.source(searchSourceBuilder);
        try {
            //调用方法进行数据通信
            SearchResponse search = client.search(searchRequest, RequestOptions.DEFAULT);
            //解析结果
            SearchHit[] hits = search.getHits().getHits();
            for (SearchHit hit:hits) {
                String sourceAsString = hit.getSourceAsString();
                System.out.println("响应结果: "+sourceAsString);

                //获取高亮查询结果
                Map<String, HighlightField> highlightFields = hit.getHighlightFields();
                HighlightField title = highlightFields.get("title");
                Text[] fragments = title.getFragments();
                for (Text fragment : fragments){
                    System.out.println("高亮查询结果："+fragment);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //查询的公共方法
    public void commonSearch(QueryBuilder queryBuilder){
        //构建searchRequest请求对象，指定索引库
        SearchRequest searchRequest = new SearchRequest("studentinfo");
        //构建searchSourceBuilder查询对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //将queryBuilder 对象设置到searchSourceBuilder中
        searchSourceBuilder.query(queryBuilder);

        /*排序*/
        searchSourceBuilder.sort("height", SortOrder.DESC);
        searchSourceBuilder.sort("age", SortOrder.ASC);
//        searchSourceBuilder.sort("name", SortOrder.ASC);//如果需要String类型排序， 字段需要加入fielddata : true ，默认false

        /*分页 分页公式： int page = (pageNum-1)*pageSize */
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(2);

        //将searchSourceBuilder查询对象封装到请求对象 SearchRequest中
        searchRequest.source(searchSourceBuilder);
        try {
            //调用方法进行数据通信
            SearchResponse search = client.search(searchRequest, RequestOptions.DEFAULT);
            //解析结果
            SearchHit[] hits = search.getHits().getHits();
            for (SearchHit hit:hits) {
                String sourceAsString = hit.getSourceAsString();
                System.out.println("响应结果: "+sourceAsString);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //修改
    @Test
    public void update() {
        UpdateRequest updateRequest = new UpdateRequest("studentinfo", "student", "1");
        Student student = new Student(1L,"李四",22L,1.75,"李四备注",false);
        String str = JSON.toJSONString(student);
        UpdateRequest doc = updateRequest.doc(str, XContentType.JSON);
        try {
            UpdateResponse update = client.update(doc, RequestOptions.DEFAULT);
            System.out.println("响应结果: "+update);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /**
     * 百万数据插入到sql中
     */
     @Test
     public void test1(){
         System.out.println("测试。。。。");
         final String url = "jdbc:mysql://127.0.0.1:3306/aa?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8";
         final String name = "com.mysql.cj.jdbc.driver";
         final String user = "root";
         final String password = "123456";
         Connection conn = null;
         try {
            Class.forName(name);//指定连接类型
         } catch (ClassNotFoundException e) {
            e.printStackTrace();
         }
         try {
            conn = DriverManager.getConnection(url, user, password);//获取连接
         } catch (SQLException e) {
            e.printStackTrace();
         }
         if (conn!=null) {
            System.out.println("获取连接成功");
            insert(conn);
         }else {
            System.out.println("获取连接失败");
         }
     }
     public void insert(Connection conn) {
         // 开始时间
         Long begin = new Date().getTime();
         // sql前缀
         String prefix = "INSERT INTO testTable (accountName,testImage,testRead,createTime) VALUES ";
         try {
             // 保存sql后缀
             StringBuffer suffix = new StringBuffer();
             // 设置事务为非自动提交
             conn.setAutoCommit(false);
             // 比起st，pst会更好些
             PreparedStatement pst = (PreparedStatement) conn.prepareStatement(" ");//准备执行语句

             // 外层循环，总提交事务次数
             for (int i = 1; i <= 100; i++) {
             suffix = new StringBuffer();
             // 第j次提交步长
             for (int j = 1; j <= 10000; j++) {
             // 构建SQL后缀
             String string = "";
             for (int k = 0; k < 10; k++) {
             char c = (char) ((Math.random() * 26) + 97);
             string += (c + "");
         }
             String name = string;
             String testImage = string;
             String testRead = string;
             SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
             suffix.append("('" + name+"','"+testImage+"','"+testRead+"','"+sdf.format(new Date())+"'),");
         }
             // 构建完整SQL
             String sql = prefix + suffix.substring(0, suffix.length() - 1);
             System.out.println("sql==="+sql);
             // 添加执行SQL
             pst.addBatch(sql);
             // 执行操作
             pst.executeBatch();
             // 提交事务
             conn.commit();
             // 清空上一次添加的数据
             suffix = new StringBuffer();
         }
             // 头等连接
             pst.close();
             conn.close();
         } catch (SQLException e) {
            e.printStackTrace();
         }
         // 结束时间
         Long end = new Date().getTime();
         // 耗时
         System.out.println("100万条数据插入花费时间 : " + (end - begin) / 1000 + " s");
         System.out.println("插入完成");
     }
}
