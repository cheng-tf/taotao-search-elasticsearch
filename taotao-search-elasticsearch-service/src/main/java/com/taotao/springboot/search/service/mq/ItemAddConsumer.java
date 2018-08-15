package com.taotao.springboot.search.service.mq;

import com.taotao.springboot.search.domain.request.SearchItem;
import com.taotao.springboot.search.mapper.SearchDao;
import com.taotao.springboot.search.mapper.SearchItemMapper;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * <p>Title: ItemAddConsumer</p>
 * <p>Description: 监听商品添加事件，同步索引库</p>
 * <p>Company: bupt.edu.cn</p>
 * <p>Created: 2018-05-06 00:39</p>
 * @author ChengTengfei
 * @version 1.0
 */
@Service
@RabbitListener(queues = "item-add.elasticsearch")//基于RabbitMQ：使用RabbitListener配置监听的队列
public class ItemAddConsumer {

    private static final Logger log = LoggerFactory.getLogger(ItemAddConsumer.class);

    @Autowired
    private SearchItemMapper searchItemMapper;

    @Autowired
    private TransportClient transportClient;

    // 基于ActiveMQ：使用JmsListener配置监听的队列
    /*@JmsListener(destination = "item-add-topic", containerFactory = "jmsListenerContainerTopic")
    public void consume(Message message) {
        try {
            // 从消息中获取商品ID
            TextMessage textMessage = (TextMessage) message;
            long itemId = Long.parseLong(textMessage.getText());
            // 等待事务提交
            Thread.sleep(1000);
            // 根据ID查询商品信息
            SearchItem searchItem = searchItemMapper.getItemById(itemId);
            // 添加索引
            searchDao.save(searchItem);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }*/

    @RabbitHandler
    public void consume2(String context) {
        try {
            log.info("添加商品，同步商品索引库ES, itemId={}", context);
            Thread.sleep(1000);//等待事务提交
            // #1 根据ID查询商品信息
            SearchItem searchItem = searchItemMapper.getItemById(Long.parseLong(context));
            // #2 同步商品索引库ES
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            XContentBuilder sourceBuilder = null;
            try {
                sourceBuilder = XContentFactory.jsonBuilder()
                        .startObject()
                        .field("item_id",searchItem.getId())
                        .field("item_title",searchItem.getTitle())
                        .field("item_sell_point",searchItem.getSellPoint())
                        .field("item_price",searchItem.getPrice())
                        .field("item_image",searchItem.getImage())
                        .field("item_date",sdf.format(searchItem.getUpdated()))
                        .field("item_category_name",searchItem.getCategoryName())
                        //.field("item_desc",searchItem.getItemDesc())
                        .endObject();
            } catch (IOException e) {
                log.error("添加商品，同步商品索引库ES, error={}", e);
            }
            IndexResponse response = transportClient.prepareIndex("taotao", "item", searchItem.getId())
                    .setSource(sourceBuilder)
                    .get();
            log.info("添加商品，同步商品索引库ES, status={}", response.status());
        } catch (InterruptedException e) {
            log.error("添加商品，同步商品索引库ES, error={}", e);
        }
    }

}
