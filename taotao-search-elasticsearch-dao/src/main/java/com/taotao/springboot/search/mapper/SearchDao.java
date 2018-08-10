package com.taotao.springboot.search.mapper;

import com.taotao.springboot.search.domain.request.SearchItem;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>Title: ItemDao</p>
 * <p>Description: </p>
 * <p>Company: bupt.edu.cn</p>
 * <p>Created: 2018-06-03 19:54</p>
 * @author ChengTengfei
 * @version 1.0
 */
@Component
public interface SearchDao extends CrudRepository<SearchItem, String> {

    /**
     * 根据ID查询商品信息
     */
    List<SearchItem> getByTitleLike(String title, Pageable pageable);


}
