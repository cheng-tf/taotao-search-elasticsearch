package com.taotao.springboot.search.domain.result;

import com.taotao.springboot.search.domain.request.SearchItem;

import java.io.Serializable;
import java.util.List;

/**
 * <p>Title: SearchRes</p>
 * <p>Description: </p>
 * <p>Company: bupt.edu.cn</p>
 * <p>Created: 2018-05-05 21:47</p>
 * @author ChengTengfei
 * @version 1.0
 */
public class SearchRes implements Serializable {

    private static final long serialVersionUID = 6479606470816977248L;

    private long totalPages;

    private long recordCount;

    private List<SearchItem> itemList;

    public SearchRes() {
    }

    public long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(long totalPages) {
        this.totalPages = totalPages;
    }

    public long getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(long recordCount) {
        this.recordCount = recordCount;
    }

    public List<SearchItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<SearchItem> itemList) {
        this.itemList = itemList;
    }

}
