package com.ruoyi.es.bean.es;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;


/**
 * @author japhet_jiu
 * @version 1.0
 *
 * indexName = "userInfo"          // es 的索引名称
 * type = "doc"                    //默认的
 *
 * 当我们springboot 启动的时候 ，引入es的依赖之后，会去看一下document的实体，如果没有配置  useServerConfiguration  则会 同步到es里面去
 * useServerConfiguration = true   //使用线上的
 * createIndex = false             // 如果没有配置，回去es删除掉这个索引名称，配置了 项目启动的时候，不会去es里面创建新的index
 *
 */
@Data
@Document(indexName = "net-ips23-2020.08.13" , type = "doc",
        useServerConfiguration = true,createIndex = false)
public class EsUserBean {
    @Id
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String Action;
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String DMAC;
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String DstIP;
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String DstIPVer;
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String DstPort;
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String EventID;
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String EventLevel;
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String EventName;
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String EventsetName;
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String EvtCount;
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String FwPolicyID;
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String GenTime;
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String HOSTNAME;
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String IP;
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String InInterface;
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String Intranet;
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String LogType;
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String MON;
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String OutInterface;
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String PACKAGE;
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String Protocol;
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String ProtocolID;
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String ProtocolType;
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String SERVERNAME;
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String SMAC;
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String SecurityID;
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String SecurityType;
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String SerialNum;
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String SrcIP;
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String SrcIPVer;
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String SrcPort;
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String Vsysid;
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String pid;



}

