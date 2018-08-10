package com.taotao.springboot.search.export;

import com.taotao.springboot.search.domain.result.SearchRes;
import com.taotao.springboot.search.domain.result.TaotaoResult;

/**
 * <p>Title: SearchResource</p>
 * <p>Description: </p>
 * <p>Company: bupt.edu.cn</p>
 * <p>Created: 2018-05-06 00:36</p>
 * @author ChengTengfei
 * @version 1.0
 */
public interface SearchResource {

    /**
     * 将商品数据导入到索引库中
     */
    TaotaoResult importItemsToIndex();

    /**
     * 商品搜索
     */
    SearchRes search(String queryString, int page, int rows) throws Exception;

}
