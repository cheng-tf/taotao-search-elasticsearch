package com.taotao.springboot.search.service.impl;

import com.taotao.springboot.search.domain.request.SearchItem;
import com.taotao.springboot.search.domain.result.SearchRes;
import com.taotao.springboot.search.domain.result.TaotaoResult;
import com.taotao.springboot.search.mapper.SearchDao;
import com.taotao.springboot.search.mapper.SearchItemMapper;
import com.taotao.springboot.search.service.SearchService;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>Title: SearchServiceImpl</p>
 * <p>Description: </p>
 * <p>Company: bupt.edu.cn</p>
 * <p>Created: 2018-05-06 00:28</p>
 * @author ChengTengfei
 * @version 1.0
 */
@Service
public class SearchServiceImpl implements SearchService {

    private static final Logger log = LoggerFactory.getLogger(SearchServiceImpl.class);

    @Autowired
    private SearchItemMapper searchItemMapper;

    @Autowired
    private SearchDao searchDao;

    @Autowired
    private TransportClient transportClient;

    // 方法1：使用Spring Data Elastic API 添加文档
    @Override
    public TaotaoResult importItemsToIndex() {
        try {
            // 1、先查询所有商品数据
            List<SearchItem> itemList = searchItemMapper.getItemList();
            // 2、遍历商品数据添加到索引库
            for (SearchItem searchItem : itemList) {
                // 把文档写入索引库
                searchDao.save(searchItem);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return TaotaoResult.build(500, "数据导入失败");
        }
        // 3、返回添加成功
        return TaotaoResult.ok();
    }

    // 方法2：使用ElasticSearch Java API 添加文档
    @Override
    public TaotaoResult importItemsToIndex2() {
        try {
            // 1、先查询所有商品数据
            List<SearchItem> itemList = searchItemMapper.getItemList();
            // 2、遍历商品数据添加到索引库
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            XContentBuilder sourceBuilder;
            for (SearchItem searchItem : itemList) {
                try {
                    sourceBuilder = XContentFactory.jsonBuilder()
                            .startObject()
                            .field("item_id", searchItem.getId())
                            .field("item_title", searchItem.getTitle())
                            .field("item_sell_point", searchItem.getSellPoint())
                            .field("item_price", searchItem.getPrice())
                            .field("item_image", searchItem.getImage())
                            .field("item_date", sdf.format(searchItem.getUpdated()))
                            .field("item_category_name", searchItem.getCategoryName())
                            //.field("item_desc",searchItem.getItemDesc())
                            .endObject();
                } catch (IOException e1) {
                    e1.printStackTrace();
                    return TaotaoResult.build(500, "数据导入失败");
                }
                IndexResponse response = transportClient.prepareIndex("taotao", "item", searchItem.getId())
                        .setSource(sourceBuilder)
                        .get();
                log.info(response.status().toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return TaotaoResult.build(500, "数据导入失败");
        }
        // 3、返回添加成功
        return TaotaoResult.ok();
    }

    // 方法1：使用Spring Data Elastic API 进行搜索
    @Override
    public SearchRes search(String queryString, Integer page, Integer rows) {
        // 设置分页
        if (page < 1){
            page = 1;
        }
        if (rows < 1) {
            rows = 10;
        }
        Pageable pageable = new QPageRequest(page, rows);
        List<SearchItem> itemLists = searchDao.getByTitleLike(queryString, pageable);
        // 封装查询结果
        SearchRes result = new SearchRes();
        long recordCount = itemLists.size();
        long pages =  recordCount / rows;
        if (recordCount % rows > 0) {
            pages++;
        }
        result.setItemList(itemLists);
        result.setRecordCount(recordCount);
        result.setTotalPages(pages);
        return result;
    }

    // 方法2：使用ElasticSearch Java API 进行搜索
    @Override
    public SearchRes search2(String queryString, Integer page, Integer rows) {
        // #0 设置分页信息
        if (page == null || page < 1){
            page = 1;
        }
        if (rows == null || rows < 1) {
            rows = 10;
        }
        int from = (page - 1) * rows;
        // #2 构造查询
        QueryBuilder qeryBuilder = QueryBuilders.termQuery("item_title", queryString);
        // #3 创建SearchRequestBuilder
        SearchRequestBuilder searchRequestBuilder = transportClient.prepareSearch("taotao")
                //.setTypes("item")
                //.setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(qeryBuilder)
                .setFrom(from)
                .setSize(rows);
        // #4 设置高亮显示
        HighlightBuilder highlightBuilder = new HighlightBuilder().field("item_title").requireFieldMatch(false);
        highlightBuilder.preTags("<font color='red'>");
        highlightBuilder.postTags("</font>");
        searchRequestBuilder.highlighter(highlightBuilder);
        // #5 执行查询
        SearchResponse response = searchRequestBuilder.get();
        // #6 处理查询结果
        List<SearchItem> itemLists = new ArrayList<>();
        SearchHits searchHits = response.getHits();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(SearchHit hit : searchHits){
            // 处理普通字段
            // 方法1：读取文档的字段
            System.out.println(hit.getSourceAsString());
            Map<String, Object> map = hit.getSourceAsMap();
            SearchItem searchItem = new SearchItem();
            searchItem.setId((String) map.get("item_id"));
            searchItem.setSellPoint((String) map.get("item_sell_point"));
            // 价格
            Integer item_price = (Integer) map.get("item_price");
            searchItem.setPrice(item_price.longValue());
            // 图片链接
            String image = (String) map.get("item_image");
            if (StringUtils.isNotBlank(image)) {
                image = image.split(",")[0];
            }
            searchItem.setImage(image);
            // 日期
            String item_date = (String) map.get("item_date");
            try {
                searchItem.setUpdated(sdf.parse(item_date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            searchItem.setCategoryName((String) map.get("item_category_name"));
            // 处理普通字段
            // 方法1：读取文档的字段，但必须先使用addFields()声明需要返回的字段
            //SearchItem searchItem = new SearchItem();
            //Map<String, SearchHitField> filds = hit.getFields();
            //searchItem.setId(filds.get("id").getValue());
            //searchItem.setSellPoint(filds.get("sellPoint").getValue());
            //searchItem.setPrice(filds.get("price").getValue());
            //searchItem.setImage(filds.get("image").getValue());
            //searchItem.setCategoryName(filds.get("categoryName").getValue());
            //searchItem.setItemDesc(filds.get("itemDesc").getValue());
            // 方法2：读取文档的_source，注意：当没有声明返回的字段，则默认返回_source
            // SearchItem searchItem = JacksonUtils.jsonToPojo(JacksonUtils.objectToJson(hit.getSource()), SearchItem.class);
            // 处理高亮字段
            HighlightField titleField = hit.getHighlightFields().get("item_title");
            if(titleField != null) {
                StringBuilder nameTmp = new StringBuilder();
                Text[] fragments = titleField.fragments();
                for (Text text : fragments) {
                    nameTmp.append(text.toString());
                }
                searchItem.setTitle(nameTmp.toString());
            }
            itemLists.add(searchItem);
        }
        // #7 封装返回
        SearchRes result = new SearchRes();
        long recordCount = itemLists.size();
        long pages =  recordCount / rows;
        if (recordCount % rows > 0) {
            pages++;
        }
        result.setItemList(itemLists);
        result.setRecordCount(recordCount);
        result.setTotalPages(pages);
        return result;
    }

}
