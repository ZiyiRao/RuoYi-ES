package com.ruoyi.es.controller;

import com.alibaba.fastjson.JSON;

import net.sf.json.JSONArray;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.es.bean.es.EsUserBean;
import com.ruoyi.es.bean.es.EsNginxBean;
import com.ruoyi.es.bean.mysql.UserBean;
import com.ruoyi.es.service.EsService;
import com.ruoyi.es.service.TsService;
import com.ruoyi.es.service.UserService;
import com.ruoyi.common.core.controller.BaseController;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

//import com.ruoyi.es.bean.es.Page;
import java.util.*;

/**
 * @author japhet_jiu
 * @version 1.0
 */
@Controller
@RequestMapping("/es")
public class EsController extends BaseController {

    @Autowired
    UserService userService;

    @Autowired
    EsService esService;

    @Autowired
    TsService tsService;




    @GetMapping("/ips")
    public String Ips(){
        return "/es/ips";
    }
    @GetMapping("/nginx")
    public String Nginx(){
        return "/es/nginx";
    }
    @RequestMapping("/add")
    public String add(Model view){
        return "/es/add";
    }
    @RequestMapping("/update")
    public String update(Model view, String id){
        if(null == id){
            return "index";
        }
        view.addAttribute("id",id);
        return "/es/update";
    }

    @ResponseBody
    @PostMapping("/selectAllIps")
    public TableDataInfo selectAllIps(){
        List<EsUserBean> ips=esService.selectEsBean("net-ips22-2020.08.13","EsUserBean");
        JSONArray object = JSONArray.fromObject(ips);
        return getDataTable(object);
    }

    @ResponseBody
    @PostMapping("/selectAllNginx")
    public TableDataInfo selectAllNginx(){
        List<EsNginxBean> nginx=esService.selectEsBean("nginx-client-2022.10","EsNginxBean");
        JSONArray object = JSONArray.fromObject(nginx);
        return getDataTable(object);
    }
    /**
     * 修改用户
     */
    @GetMapping("/edit/{userId}")
    public String edit(@PathVariable("userId") Long userId, ModelMap mmap)
    {
        UserBean ub = new UserBean();
        ub.setId(Integer.parseInt(userId.toString()));
        mmap.put("user", userService.selectUserBeanList(ub).get(0));
        return "es/update";
    }

    /**
     * @PathVariable用于告诉Spring URI路径的一部分是您希望传递给方法的值。这是你想要的，还是应该将形式数据的变量发布到URI？
     *
     * 如果您想要表单数据，请使用@RequestParam而不是@PathVariable。
     *
//     * @param type
     * @param keyword
     * @return
     */
    @GetMapping("/searchIpsField/{field}/{keyword}")
    public String searchIpsField( @PathVariable("field")String field,@PathVariable("keyword")String keyword,ModelMap mmap){
        List<String> resultList = new LinkedList();
        resultList.add(field);
        resultList.add(keyword);
        JSONArray object = JSONArray.fromObject(resultList);
        System.out.println(object);
        mmap.put("list", object);
        return "es/search_ips";

    }

    @GetMapping("/searchNginxField/{field}/{keyword}")
    public String searchNginxField( @PathVariable("field")String field,@PathVariable("keyword")String keyword,ModelMap mmap){
        List<String> resultList = new LinkedList();
        resultList.add(field);
        resultList.add(keyword);
        JSONArray object = JSONArray.fromObject(resultList);
        System.out.println(object);
        mmap.put("list1", object);
        return "es/search_nginx";

    }

    @ResponseBody
    @PostMapping("/searchEsIps/{field}/{keyword}")
    public TableDataInfo searchEsIps(@PathVariable("field")String field,@PathVariable("keyword")String keyword) {
        List<EsUserBean> ips=esService.selectField("net-ips22-2020.08.13","EsUserBean",field,keyword);
        JSONArray object = JSONArray.fromObject(ips);
        return getDataTable(object);
    }

    @ResponseBody
    @PostMapping("/searchEsNginx/{field}/{keyword}")
    public TableDataInfo searchEsNginx(@PathVariable("field")String field,@PathVariable("keyword")String keyword) {
        List<EsNginxBean> nginx=esService.selectField("nginx-client-2022.10","EsNginxBean",field,keyword);
        JSONArray object = JSONArray.fromObject(nginx);
        return getDataTable(object);
    }


    @ResponseBody
    @GetMapping("/searchAllIpsField")
    public List<EsUserBean> searchAllIpsField(){
        List<EsUserBean> ips=esService.selectEsBean("net-ips22-2020.08.13","EsUserBean");
        JSONArray object = JSONArray.fromObject(ips);
        return object;
    }

    @ResponseBody
    @GetMapping("/searchAllNginxField")
    public List<EsNginxBean> searchAllNginxField(){
        List<EsNginxBean> nginx=esService.selectEsBean("nginx-client-2022.10","EsNginxBean");
        JSONArray object = JSONArray.fromObject(nginx);
        return object;
    }



//    @GetMapping("/getMess/{id}")
//    @ResponseBody
//    public List<UserBean> getMess(@PathVariable("id") Integer id){
//        UserBean ub = new UserBean();
//        ub.setId(id);
//        List<UserBean> userBeans = userService.selectUserBeanList(ub);
//        return userBeans;
//    }
//
//    @PostMapping("/updateSave")
//    @ResponseBody
//    @Transactional(rollbackFor = Exception.class)
//    public AjaxResult updateSave(@RequestParam("id")String id, @RequestParam("name")String name, @RequestParam("age")String age, @RequestParam("phone")String phone, @RequestParam("remark")String remark){
//        //修改mysql数据库
//        UserBean ub = new UserBean();
//        ub.setId(Integer.parseInt(id));
//        ub.setName(name);
//        ub.setAge(Integer.parseInt(age));
//        ub.setPhone(phone);
//        ub.setRemark(remark);
//        int row = userService.updateById(ub);
//
//        //修改es
//        EsUserBean esUserBean = esService.queryEmployeeById(id);
//        esUserBean.setId(Integer.parseInt(id));
//        esUserBean.setName(name);
//        esUserBean.setAge(Integer.parseInt(age));
//        esUserBean.setPhone(phone);
//        esUserBean.setRemark(remark);
//        EsUserBean save = esService.save(esUserBean);
//        System.out.println(save);
//
//        /*BoolQueryBuilder builder = QueryBuilders.boolQuery();
//        builder.should(QueryBuilders.matchPhraseQuery("id",id));
//        Page<EsUserBean> search = (Page<EsUserBean>)esService.search(builder);
//        List<EsUserBean> content = search.getContent();
//        content.get(0).setId(Integer.parseInt(id));
//        content.get(0).setName(name);
//        content.get(0).setAge(Integer.parseInt(age));
//        content.get(0).setPhone(phone);
//        content.get(0).setRemark(remark);
//        esService.save(content.get(0));*/
//
//
//       /* Optional<EsUserBean> byId = esService.findById(Integer.parseInt(id));
//        byId.get().setId(Integer.parseInt(id));
//        byId.get().setName(name);
//        byId.get().setAge(Integer.parseInt(age));
//        byId.get().setPhone(phone);
//        byId.get().setRemark(remark);
//        esService.save(byId.get());*/
//
//
//        /*Iterable<EsUserBean> id1 = esService.search(new MatchQueryBuilder("id", id));
//        Iterator<EsUserBean> iterator = id1.iterator();
//        while (iterator.hasNext()){
//            EsUserBean next = iterator.next();
//            System.out.println(next);
//        }*/
//        if(row == 1 && save!=null){
//            return toAjax(row);
//        }else{
//            return toAjax(0);
//        }
//    }
//
//    @PostMapping("/addSave")
//    @ResponseBody
//    @Transactional(rollbackFor = Exception.class)
//    public AjaxResult addSave(UserBean ub){
//        //向mysql添加数据
//        Date date = new Date();
//        Date date2 = new Date();
//        ub.setCreateTime(date);
//        ub.setUpdateTime(date2);
//        int row = userService.save(ub);
//
//
//        //向es添加数据
//        EsUserBean eub = new EsUserBean();
//        eub.setId(ub.getId()); //确保es mysql 的id一样
//        eub.setName(ub.getName());
//        eub.setPassword(ub.getName());
//        eub.setAge(ub.getAge());
//        eub.setPhone(ub.getPhone());
//        eub.setRemark(ub.getRemark());
//        eub.setCreateTime(date);
//        eub.setUpdateTime(date2);
//        EsUserBean save = esService.save(eub);
//        if(row==1 && save!=null){
//            return toAjax(row);
//        }else{
//            return toAjax(0);
//        }
//    }
//
//
//    @PostMapping("/remove")
//    @ResponseBody
//    @Transactional(rollbackFor = Exception.class)
//    public AjaxResult deleteById(@RequestParam("ids") String id){
//        try {
//            //删除mysql 数据库
//            int row = userService.deleteById(id);
//            //删除es
//            EsUserBean esUserBean = esService.queryEmployeeById(id);
//            esService.delete(esUserBean);
//            if(row==1){
//                return toAjax(row);
//            }else{
//                return toAjax(0);
//            }
//        }catch (Exception e) {
//            return error(e.getMessage());
//        }
//
//    }

}