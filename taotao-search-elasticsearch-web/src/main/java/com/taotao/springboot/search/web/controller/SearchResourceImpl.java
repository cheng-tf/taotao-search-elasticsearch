package com.taotao.springboot.search.web.controller;

import com.alibaba.dubbo.config.annotation.Service;
import com.taotao.springboot.search.domain.result.SearchRes;
import com.taotao.springboot.search.domain.result.TaotaoResult;
import com.taotao.springboot.search.export.SearchResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.taotao.springboot.search.service.SearchService;
import com.taotao.springboot.search.common.utils.JacksonUtils;

/**
 * <p>Title: SearchResourceImpl</p>
 * <p>Description: </p>
 * <p>Company: bupt.edu.cn</p>
 * <p>Created: 2018-05-06 00:53</p>
 * @author ChengTengfei
 * @version 1.0
 */
@Service(interfaceClass = SearchResource.class)
@Controller
public class SearchResourceImpl implements SearchResource {

    private static final Logger log = LoggerFactory.getLogger(SearchResourceImpl.class);

    @Autowired
    private SearchService searchService;

    @Override
    public TaotaoResult importItemsToIndex() {
        TaotaoResult res = null;
        try {
            log.info("将商品数据导入到索引库中 importItemsToIndex ");
            res = searchService.importItemsToIndex2();
            log.info("将商品数据导入到索引库中 importItemsToIndex res = {}", JacksonUtils.objectToJson(res));
        } catch (Exception e){
            log.error("### Call SearchItemResourceImpl.importItemsToIndex error = {}", e);
        }
        return res;
    }

    @Override
    public SearchRes search(String queryString, int page, int rows) throws Exception {
        SearchRes res = null;
        try {
            log.info("商品搜索 queryString = {}, page/rows = {}", queryString, String.valueOf(page) + "/" + String.valueOf(rows));
            res = searchService.search2(queryString, page, rows);
            log.info("商品搜索 search res = {}", JacksonUtils.objectToJson(res));
        } catch (Exception e){
            log.error("### Call SearchResourceImpl.search error = {}", e);
        }
        return res;
    }

}
