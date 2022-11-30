package com.ruoyi.es.bean.es;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@Document(indexName = "nginx-client-2022.11" ,type = "doc",
        useServerConfiguration = true,createIndex = false)
public class EsNginxBean {
    @Id
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String remote_addr;
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String host;
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String rps_status;
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String req_uri;
}
