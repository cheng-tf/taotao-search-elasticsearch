package com.taotao.springboot.search.service;

import com.taotao.springboot.search.domain.result.SearchRes;
import com.taotao.springboot.search.domain.result.TaotaoResult;

/**
 * <p>Title: SearchService</p>
 * <p>Description: 商品搜索Service</p>
 * <p>Company: bupt.edu.cn</p>
 * <p>Created: 2018-05-06 00:26</p>
 * @author ChengTengfei
 * @version 1.0
 */
public interface SearchService {

    /**
     * 将商品数据导入到索引库中
     */
    // 方法1：使用Spring Data Elastic API
    TaotaoResult importItemsToIndex();
    // 方法2：使用ElasticSearch Java API
    TaotaoResult importItemsToIndex2();

    /**
     * 商品搜索
     */
    // 方法1：使用Spring Data Elastic API
    SearchRes search(String queryString, Integer page, Integer rows);
    // 方法2：使用ElasticSearch Java API
    SearchRes search2(String queryString, Integer page, Integer rows);

}
