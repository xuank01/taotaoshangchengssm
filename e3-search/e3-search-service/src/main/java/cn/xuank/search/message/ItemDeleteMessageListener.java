package cn.xuank.search.message;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import cn.xuank.common.pojo.SearchItem;
import cn.xuank.search.mapper.ItemMapper;


/**
 * 监听商品添加消息，接收消息后，将对应的商品信息同步到索引库
 */
public class ItemDeleteMessageListener implements MessageListener {
	
	@Autowired
	private ItemMapper itemMapper;
	@Autowired
	private SolrClient solrClient;

	@Override
	public void onMessage(Message message) {
		try {
			//从消息中取商品id
			TextMessage textMessage = (TextMessage) message;
			String text = textMessage.getText();
			/*List items=Arrays.stream(text.split(","))
            .map(s -> Long.parseLong(s.trim()))
            .collect(Collectors.toList());*/
			//Long itemId = new Long(text);
			//等待事务提交
			//System.out.println(itemId);
			String [] str=text.split(",");
			Thread.sleep(1000);
			for(int i=0;i<str.length;i++){
			//SearchItem searchItem = itemMapper.getItemById(itemId);
			solrClient.deleteById(str[i].trim());
			//向文档对象中添加域
			/*for(int i=0;i<items.size();i++){
				solrClient.deleteById(items.get(i)+"");
				//提交
				solrClient.commit();
				}*/
			solrClient.commit();
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
