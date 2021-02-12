package com.cgs.esdao;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.cgs.esdao.po.BaseEsPo;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.ElasticsearchClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author caoguangshu
 * @date 2021/2/12
 * @time 9:04 下午
 */
@Slf4j
public class BaseEsDAO <T extends BaseEsPo> {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    protected String indexName;

    protected String typeName;

    public BaseEsDAO(String indexName,String typeName){
        this.indexName = indexName;
        this.typeName = typeName;
    }

    public void clear(){

    }

    /**
     * 不存在则插入，存在则更新
     * @param id
     * @param json
     * @return
     * @throws IOException
     */
    public boolean upsert(String id, String json) {
        if(StringUtils.isEmpty(id)){
            log.error("更新ES数据|id={}, data={}", id, json);
            throw new RuntimeException("id为空");
        }

        // 构建请求
        UpdateRequest request = new UpdateRequest(this.indexName, this.typeName, id);
        request.doc(json, XContentType.JSON).upsert(json, XContentType.JSON);

        // 执行
        boolean bool = false;
        try {
            log.info("更新ES数据|id={}, data={}", id, json);
            UpdateResponse response = this.restHighLevelClient.update(request);
            bool = true;
        }catch (Exception e) {
            log.error("更新Es异常|index={}, id={}, json={}", indexName, id, json);
        }

        return bool;
    }

    /**
     * 批量更新：id不存在会抛出异常
     * @param datas：key为id，value为待更新数据
     * @return
     * @throws IOException
     */
    public boolean updateBatch(Map<String, Map<String, Object>> datas) {
        if(datas == null || datas.isEmpty()){
            return true;
        }

        BulkRequest bulkRequest = new BulkRequest();
        for (Map.Entry<String, Map<String, Object>> entry : datas.entrySet()) {
            Map<String, Object> data = entry.getValue();
            UpdateRequest updateRequest = new UpdateRequest(this.indexName, this.typeName, entry.getKey());
            updateRequest.doc(entry.getValue());

            bulkRequest.add(updateRequest);
        }

        boolean bool = false;
        try {
            BulkResponse response = this.restHighLevelClient.bulk(bulkRequest);
            bool = true;
        }catch (Exception e) {
            log.error("批量更新Es异常|index={}", indexName);
        }

        return bool;
    }

    /**
     * 批量更新：id不存在会抛出异常
     * @throws IOException
     */
    public boolean updateBatch(List<T> ts) {
        if(ts == null || ts.isEmpty()){
            return true;
        }

        Map<String, Map<String, Object>> datas = new HashMap<>();
        for (T t : ts) {
            if(t == null){
                continue;
            }

            if(t.getId() == null){
                continue;
            }

            String json = JSON.toJSONString(t);
            Map<String, Object> data = JSON.parseObject(json, Map.class);
            datas.put(t.getId(), data);
        }

        return updateBatch(datas);
    }
}
