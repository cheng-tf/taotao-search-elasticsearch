package com.taotao.springboot.search.domain.request;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>Title: SearchReq</p>
 * <p>Description: </p>
 * <p>Company: bupt.edu.cn</p>
 * <p>Created: 2018-05-05 21:47</p>
 * @author ChengTengfei
 * @version 1.0
 */
//@Document(indexName = "taotao", type = "item", shards = 1, replicas = 0, refreshInterval = "-1")
@Document(indexName = "taotao", type = "item")
public class SearchItem implements Serializable {

    private static final long serialVersionUID = 7552433518656382518L;

    // 商品ID
    @Id
    private String id;

    // 标题
    @Field(
            type = FieldType.Text,
            analyzer = "ik_max_word",
            searchAnalyzer = "ik_smart",
            index = true,
            store = true
    )
    private String title;

    // 卖点
    @Field(
            type = FieldType.Text,
            analyzer = "ik_max_word",
            searchAnalyzer = "ik_smart",
            index = true,
            store = true
    )
    private String sellPoint;

    // 价格
    @Field(type = FieldType.Long, index = true, store = true)
    private long price;

    // 图片链接
    @Field(type = FieldType.Text, index = false, store = true)
    private String image;

    // 商品更新时间
    @Field(type = FieldType.Date, index = true, store = true)
    private Date updated;

    // 商品类目名称
    @Field(type = FieldType.Text, index = true, store = true)
    private String categoryName;

    // 商品描述
    @Field(
            type = FieldType.Text,
            analyzer = "ik_max_word",
            searchAnalyzer = "ik_smart",
            index = true,
            store = false
    )
    private String itemDesc;

    public SearchItem() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSellPoint() {
        return sellPoint;
    }

    public void setSellPoint(String sellPoint) {
        this.sellPoint = sellPoint;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public String[] getImages() {
        if (image != null && !"".equals(image)) {
            String[] images = image.split(",");
            return images;
        }
        return null;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }
}
