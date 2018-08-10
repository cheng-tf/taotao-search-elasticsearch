package com.taotao.springboot.search.mapper;

import com.taotao.springboot.search.domain.request.SearchItem;

import java.util.List;

/**
 * <p>Title: SearchItemMapper</p>
 * <p>Description: </p>
 * <p>Company: bupt.edu.cn</p>
 * <p>Created: 2018-05-06 00:19</p>
 * @author ChengTengfei
 * @version 1.0
 */
public interface SearchItemMapper {

    /**
     * 获取Item列表，用于Solr数据源
     */
    List<SearchItem> getItemList();

    /**
     * 根据商品ID查询Item
     */
    SearchItem getItemById(long itemId);


}
