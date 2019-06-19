package cn.xuank.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import cn.xuank.common.utils.IDUtils;
import cn.xuank.common.utils.JsonUtils;
import cn.xuank.common.jedis.JedisClient;
import cn.xuank.common.pojo.EasyUIDataGridResult;
import cn.xuank.common.utils.E3Result;
import cn.xuank.mapper.TbItemDescMapper;
import cn.xuank.mapper.TbItemMapper;
import cn.xuank.pojo.TbItem;
import cn.xuank.pojo.TbItemDesc;
import cn.xuank.pojo.TbItemDescExample;
import cn.xuank.pojo.TbItemExample;
import cn.xuank.pojo.TbItemExample.Criteria;
import cn.xuank.ItemService;

/**
 * 商品管理Service
 
 */
@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private TbItemMapper itemMapper;
	@Autowired
	private TbItemDescMapper itemDescMapper;
	@Autowired
	private JmsTemplate jmsTemplate;
	@Resource
	private Destination topicDestination;
	@Resource
	private Destination queueDestination;
	@Resource
	private Destination queueUDestination;
	@Autowired
	private JedisClient jedisClient;
	@Value("${REDIS_ITEM_PRE}")
	private String REDIS_ITEM_PRE;
	@Value("${ITEM_CACHE_EXPIRE}")
	private Integer ITEM_CACHE_EXPIRE;
	/**
	 * 商品显示
	 */
	public EasyUIDataGridResult getItemList(int page, int rows) {
		//设置分页信息
		PageHelper.startPage(page, rows);
		//执行查询
		TbItemExample example = new TbItemExample();
		List<TbItem> list = itemMapper.selectByExample(example);
		//创建一个返回值对象
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setRows(list);
		//取分页结果
		PageInfo<TbItem> pageInfo = new PageInfo<>(list);
		//取总记录数
		long total = pageInfo.getTotal();
		result.setTotal(total);
		return result;
	}
	/**
	 * 商品添加
	 */
	@Override
	public E3Result addItem(TbItem item, String desc) {
		//生成商品id
		long itemId = IDUtils.genItemId();
		//补全item的属性
		item.setId(itemId);
		//1-正常，2-下架，
		item.setStatus((byte) 1);
		item.setCreated(new Date());
		item.setUpdated(new Date());
		//向商品表插入数据
		itemMapper.insert(item);
		//创建一个商品描述表对应的pojo对象。
		TbItemDesc itemDesc = new TbItemDesc();
		//补全属性
		itemDesc.setItemId(itemId);
		itemDesc.setItemDesc(desc);
		itemDesc.setCreated(new Date());
		itemDesc.setUpdated(new Date());
		
		//向商品描述表插入数据
		itemDescMapper.insert(itemDesc);
		
		jmsTemplate.send(topicDestination, new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage textMessage = session.createTextMessage(itemId + "");
				return textMessage;
			}
		});
		//返回成功
		return E3Result.ok();
	}

	/**
	 * 商品删除
	 */
	@Override
	public E3Result deleteItem(TbItem item) {
		String items="";
		for(int i=0;i<item.getIds().size();i++){
			Long id =item.getIds().get(i);
			itemMapper.deleteByPrimaryKey(id);
			itemDescMapper.deleteByPrimaryKey(id);
			if(i!=item.getIds().size()-1){
				items=items+id+",";
			}
			else if(i==item.getIds().size()-1){
				items=items+id;
			}
		}
		final String itemss=items;
		jmsTemplate.send(queueDestination, new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage textMessage = session.createTextMessage(itemss);
				return textMessage;
			}
		});
		return E3Result.ok();
	}


	@Override
	public E3Result editbyIdd(long itemId) {
		TbItemDesc tbItemdesc = itemDescMapper.selectByPrimaryKey(itemId);
		
		return E3Result.ok(tbItemdesc);
	}


	@Override
	public E3Result updateItem(TbItem item, String desc) {
		Long id = item.getId();	
		// 创建查询条件，根据id更新商品表
		TbItemExample tbItemExample = new TbItemExample();	
		Criteria criteria = tbItemExample.createCriteria();	
		criteria.andIdEqualTo(id);	itemMapper.updateByExampleSelective(item, tbItemExample);			
		// 2.根据商品id更新商品描述表	
		TbItemDesc itemDesc = new TbItemDesc();	
		itemDesc.setItemDesc(desc);	TbItemDescExample tbItemDescExample = new TbItemDescExample();	
		TbItemDescExample.Criteria createCriteria = tbItemDescExample.createCriteria();	
		createCriteria.andItemIdEqualTo(id);	
		itemDescMapper.updateByExampleSelective(itemDesc, tbItemDescExample);
		jmsTemplate.send(queueUDestination, new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage textMessage = session.createTextMessage(id+"");
				return textMessage;
			}
		});
		return E3Result.ok();
	}


	@Override
	public E3Result updateItemStatus(List<Long> ids, String method) {
		TbItem item = new TbItem();	
		if (method.equals("reshelf")) {			
			// 正常，更新status=3即可			
			item.setStatus((byte) 1);		
			} else if (method.equals("instock")) {			
				// 下架，更新status=3即可			
				item.setStatus((byte) 2);		
				} 				
		for (Long id : ids) {			
			// 创建查询条件，根据id更新			
			TbItemExample tbItemExample = new TbItemExample();			
			Criteria criteria = tbItemExample.createCriteria();			
			criteria.andIdEqualTo(id);						
			itemMapper.updateByExampleSelective(item, tbItemExample);	
			return E3Result.ok();
		}

			 
		return null;
	}
	@Override
	public TbItemDesc getItemDescById(long itemId) {
		
		//查询缓存
				try {
					String json = jedisClient.get(REDIS_ITEM_PRE + ":" + itemId + ":DESC");
					if(StringUtils.isNotBlank(json)) {
						TbItemDesc tbItemDesc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
						return tbItemDesc;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				TbItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(itemId);
				//把结果添加到缓存
				try {
					jedisClient.set(REDIS_ITEM_PRE + ":" + itemId + ":DESC", JsonUtils.objectToJson(itemDesc));
					//设置过期时间
					jedisClient.expire(REDIS_ITEM_PRE + ":" + itemId + ":DESC", ITEM_CACHE_EXPIRE);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return itemDesc;
			}
	@Override
	public TbItem getItemById(long itemId) {
		//查询缓存
				try {
					String json = jedisClient.get(REDIS_ITEM_PRE + ":" + itemId + ":BASE");
					if(StringUtils.isNotBlank(json)) {
						TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
						return tbItem;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				//缓存中没有，查询数据库
				//根据主键查询
				//TbItem tbItem = itemMapper.selectByPrimaryKey(itemId);
				TbItemExample example = new TbItemExample();
				Criteria criteria = example.createCriteria();
				//设置查询条件
				criteria.andIdEqualTo(itemId);
				//执行查询
				List<TbItem> list = itemMapper.selectByExample(example);
				if (list != null && list.size() > 0) {
					//把结果添加到缓存
					try {
						jedisClient.set(REDIS_ITEM_PRE + ":" + itemId + ":BASE", JsonUtils.objectToJson(list.get(0)));
						//设置过期时间
						jedisClient.expire(REDIS_ITEM_PRE + ":" + itemId + ":BASE", ITEM_CACHE_EXPIRE);
					} catch (Exception e) {
						e.printStackTrace();
					}
					return list.get(0);
				}
		return null;
	}


	

}
