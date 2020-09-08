package com.clay.elasticsearch;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName TestESREST
 * @Dscription TODO
 * @Author wk
 * @Date 2020-09-07 17:27
 * @Version 1.0
 **/
public class TestESREST {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private RestClient restClient;



    @Before
    public void init() {

        RestClientBuilder restClientBuilder = RestClient.builder(new HttpHost("127.0.0.1", 9200, "http"),             new HttpHost("127.0.0.1", 9201, "http"),
                new HttpHost("127.0.0.1", 9202, "http"));
        restClientBuilder.setFailureListener(new RestClient.FailureListener() {
            @Override
            public void onFailure(Node host) {
                System.out.println("出错了 -> " + host);
            }

        });


        this.restClient = restClientBuilder.build();
    }

    @After
    public void after() throws IOException {
        restClient.close();
    }

    // 查询集群状态
    @Test
    public void testGetInfo() throws IOException {
        Request request = new Request("GET", "/_cluster/state");
        request.addParameter("pretty", "true");
        Response response = this.restClient.performRequest(request);
        System.out.println(response.getStatusLine());
        System.out.println(EntityUtils.toString(response.getEntity()));

    }

    // 新增数据
    @Test
    public void testCreateData() throws IOException {
        Request request = new Request("POST", "/haoke/house");
        Map<String, Object> data = new HashMap<>();
        data.put("id", "2001");
        data.put("title", "张江高科");
        data.put("price", "3500");
        request.setJsonEntity(MAPPER.writeValueAsString(data));
        Response response = this.restClient.performRequest(request);
        System.out.println(response.getStatusLine());
        System.out.println(EntityUtils.toString(response.getEntity()));

    }

    // 根据id查询数据
    @Test
    public void testQueryData() throws IOException {
        Request request = new Request("GET", "/haoke/house/G0pfE2gBCKv8opxuRz1y");
        Response response = this.restClient.performRequest(request);
        System.out.println(response.getStatusLine());
        System.out.println(EntityUtils.toString(response.getEntity()));
    }

    // 搜索数据
    @Test
    public void testSearchData() throws IOException {
        Request request = new Request("POST", "/haoke/house/_search");
        String searchJson = "{\"query\": {\"match\": {\"title\": \"拎包入住\"}}}";
        request.setJsonEntity(searchJson);
        request.addParameter("pretty", "true");
        Response response = this.restClient.performRequest(request);
        System.out.println(response.getStatusLine());
        System.out.println(EntityUtils.toString(response.getEntity()));
    }
}


