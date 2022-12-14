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

    //???????????????
    @Before
    public void init(){
        client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("192.168.3.10", 9200, "http")));
    }
    //???????????????
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

    //??????
    @Test
    public void add(){
        Student student = new Student(1L,"??????",11L,1.50,"????????????",false);
        String str = JSON.toJSONString(student);
        IndexRequest indexRequest = new IndexRequest("studentinfo", "student", "1");
        indexRequest.source(str, XContentType.JSON);
        try {
            IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
            System.out.println("????????????: "+JSON.toJSONString(indexResponse));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //????????????
    @Test
    public void addList(){
        BulkRequest bulkRequest = new BulkRequest();
        for (long i = 2; i <=10 ; i++) {
            Student student = new Student(i,"??????"+i,11+i,1.50,"????????????"+i,false);
            String str = JSON.toJSONString(student);
            IndexRequest indexRequest = new IndexRequest("studentinfo", "student", i + "");
            indexRequest.source(str,XContentType.JSON);
            bulkRequest.add(indexRequest);
            try {
                BulkResponse bulk = client.bulk(bulkRequest, RequestOptions.DEFAULT);
                System.out.println("????????????: "+bulk);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //??????
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

    //??????
    @Test
    public  void get(){
        String id = "1";
        GetRequest getRequest = new GetRequest("studentinfo", "student", id);
        try {
            GetResponse documentFields = client.get(getRequest,RequestOptions.DEFAULT);
            System.out.println("????????????: "+documentFields);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //?????? match
    @Test
    public void getMatch(){
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("title", "???");
        commonSearch(matchQueryBuilder);
    }

    //?????? match_all
    @Test
    public void getMatchAll(){
        MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();
        commonSearch(matchAllQueryBuilder);
    }

    //????????????
    @Test
    public void hightSearch(){
        MatchQueryBuilder queryBuilder = QueryBuilders.matchQuery("name", "???");
        //??????searchRequest??????????????????????????????
        SearchRequest searchRequest = new SearchRequest("studentinfo");
        //??????searchSourceBuilder????????????
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //???queryBuilder ???????????????searchSourceBuilder???
        searchSourceBuilder.query(queryBuilder);

        //??????????????????
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<b color='red'>");
        highlightBuilder.postTags("</b>");
        highlightBuilder.field("title");

        //???searchSourceBuilder????????????????????????????????? SearchRequest???
        searchRequest.source(searchSourceBuilder);
        try {
            //??????????????????????????????
            SearchResponse search = client.search(searchRequest, RequestOptions.DEFAULT);
            //????????????
            SearchHit[] hits = search.getHits().getHits();
            for (SearchHit hit:hits) {
                String sourceAsString = hit.getSourceAsString();
                System.out.println("????????????: "+sourceAsString);

                //????????????????????????
                Map<String, HighlightField> highlightFields = hit.getHighlightFields();
                HighlightField title = highlightFields.get("title");
                Text[] fragments = title.getFragments();
                for (Text fragment : fragments){
                    System.out.println("?????????????????????"+fragment);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //?????????????????????
    public void commonSearch(QueryBuilder queryBuilder){
        //??????searchRequest??????????????????????????????
        SearchRequest searchRequest = new SearchRequest("studentinfo");
        //??????searchSourceBuilder????????????
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //???queryBuilder ???????????????searchSourceBuilder???
        searchSourceBuilder.query(queryBuilder);

        /*??????*/
        searchSourceBuilder.sort("height", SortOrder.DESC);
        searchSourceBuilder.sort("age", SortOrder.ASC);
//        searchSourceBuilder.sort("name", SortOrder.ASC);//????????????String??????????????? ??????????????????fielddata : true ?????????false

        /*?????? ??????????????? int page = (pageNum-1)*pageSize */
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(2);

        //???searchSourceBuilder????????????????????????????????? SearchRequest???
        searchRequest.source(searchSourceBuilder);
        try {
            //??????????????????????????????
            SearchResponse search = client.search(searchRequest, RequestOptions.DEFAULT);
            //????????????
            SearchHit[] hits = search.getHits().getHits();
            for (SearchHit hit:hits) {
                String sourceAsString = hit.getSourceAsString();
                System.out.println("????????????: "+sourceAsString);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //??????
    @Test
    public void update() {
        UpdateRequest updateRequest = new UpdateRequest("studentinfo", "student", "1");
        Student student = new Student(1L,"??????",22L,1.75,"????????????",false);
        String str = JSON.toJSONString(student);
        UpdateRequest doc = updateRequest.doc(str, XContentType.JSON);
        try {
            UpdateResponse update = client.update(doc, RequestOptions.DEFAULT);
            System.out.println("????????????: "+update);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /**
     * ?????????????????????sql???
     */
     @Test
     public void test1(){
         System.out.println("??????????????????");
         final String url = "jdbc:mysql://127.0.0.1:3306/aa?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8";
         final String name = "com.mysql.cj.jdbc.driver";
         final String user = "root";
         final String password = "123456";
         Connection conn = null;
         try {
            Class.forName(name);//??????????????????
         } catch (ClassNotFoundException e) {
            e.printStackTrace();
         }
         try {
            conn = DriverManager.getConnection(url, user, password);//????????????
         } catch (SQLException e) {
            e.printStackTrace();
         }
         if (conn!=null) {
            System.out.println("??????????????????");
            insert(conn);
         }else {
            System.out.println("??????????????????");
         }
     }
     public void insert(Connection conn) {
         // ????????????
         Long begin = new Date().getTime();
         // sql??????
         String prefix = "INSERT INTO testTable (accountName,testImage,testRead,createTime) VALUES ";
         try {
             // ??????sql??????
             StringBuffer suffix = new StringBuffer();
             // ??????????????????????????????
             conn.setAutoCommit(false);
             // ??????st???pst????????????
             PreparedStatement pst = (PreparedStatement) conn.prepareStatement(" ");//??????????????????

             // ????????????????????????????????????
             for (int i = 1; i <= 100; i++) {
             suffix = new StringBuffer();
             // ???j???????????????
             for (int j = 1; j <= 10000; j++) {
             // ??????SQL??????
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
             // ????????????SQL
             String sql = prefix + suffix.substring(0, suffix.length() - 1);
             System.out.println("sql==="+sql);
             // ????????????SQL
             pst.addBatch(sql);
             // ????????????
             pst.executeBatch();
             // ????????????
             conn.commit();
             // ??????????????????????????????
             suffix = new StringBuffer();
         }
             // ????????????
             pst.close();
             conn.close();
         } catch (SQLException e) {
            e.printStackTrace();
         }
         // ????????????
         Long end = new Date().getTime();
         // ??????
         System.out.println("100?????????????????????????????? : " + (end - begin) / 1000 + " s");
         System.out.println("????????????");
     }
}
