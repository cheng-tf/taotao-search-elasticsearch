package com.taotao.springboot.search.web.service;

import com.taotao.springboot.search.common.utils.JacksonUtils;
import com.taotao.springboot.search.domain.result.SearchRes;
import com.taotao.springboot.search.service.SearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>Title: SearchServiceImplTest</p>
 * <p>Description: SearchService测试 </p>
 * <p>Company: bupt.edu.cn</p>
 * <p>Created: 2018-09-01 14:24</p>
 * @author ChengTengfei
 * @version 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class SearchServiceImplTest {

    @Autowired
    private SearchService searchService;

    private static Logger log = LoggerFactory.getLogger(SearchServiceImplTest.class);

    // 商品搜索
    @Test
    public void testSearch2() {
        SearchRes result = searchService.search2("笔记本", 0, 30);
        log.info(JacksonUtils.objectToJson(result));
    }

}
