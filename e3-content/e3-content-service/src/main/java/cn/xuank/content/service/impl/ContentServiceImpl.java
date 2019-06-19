package cn.xuank.content.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.xuank.common.jedis.JedisClient;
import cn.xuank.common.pojo.EasyUIDataGridResult;
import cn.xuank.common.utils.E3Result;
import cn.xuank.common.utils.JsonUtils;
import cn.xuank.content.service.ContentService;
import cn.xuank.mapper.TbContentMapper;
import cn.xuank.pojo.TbContent;
import cn.xuank.pojo.TbContentExample;
import cn.xuank.pojo.TbItemDesc;
import cn.xuank.pojo.TbItemDescExample;
import cn.xuank.pojo.TbItemExample;
import cn.xuank.pojo.TbContentExample.Criteria;

@Service
public class ContentServiceImpl implements ContentService{
	@Autowired
	private TbContentMapper contentMapper;
	@Autowired
	private JedisClient jedisClient;
	
	@Value("${CONTENT_LIST}")
	private String CONTENT_LIST;
	@Override
	public EasyUIDataGridResult getContentList(long categoryId, int page, int rows) {
		TbContentExample example=new TbContentExample();
		Criteria criteria=example.createCriteria();
        criteria.andCategoryIdEqualTo(categoryId);
        //分页管理
        PageHelper.startPage(page, rows);
        List<TbContent> list=contentMapper.selectByExample(example);
        EasyUIDataGridResult result = new EasyUIDataGridResult();
        result.setRows(list);
        PageInfo<TbContent> pageInfo = new PageInfo<>(list);
        result.setTotal(pageInfo.getTotal());
        return result;
	}
	@Override
	public E3Result addContent(TbContent content) {
				content.setCreated(new Date());
				content.setUpdated(new Date());
				//向内容表插入数据
				contentMapper.insert(content);
				//返回成功
				try {
					jedisClient.hdel(CONTENT_LIST, content.getCategoryId().toString());
				} catch (Exception e) {
					e.printStackTrace();
				}
				return E3Result.ok();
	}
	@Override
	public E3Result deleteContent(TbContent content) {
		for(int i=0;i<content.getIds().size();i++){
			Long id =content.getIds().get(i);
			contentMapper.deleteByPrimaryKey(id);
		}
		try {
			jedisClient.expire(CONTENT_LIST, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return E3Result.ok();
	}
	
	@Override
	public E3Result updateContent(TbContent content) {
		Long id = content.getId();
		TbContentExample tbContentExample = new TbContentExample();	
		Criteria criteria = tbContentExample.createCriteria();
		criteria.andIdEqualTo(id);
		contentMapper.updateByExampleSelective(content, tbContentExample);
		try {
			jedisClient.hdel(CONTENT_LIST, content.getCategoryId().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return E3Result.ok();
	}
	@Override
	public List<TbContent> getContentListByCid(long cid) {
		//查询缓存
		try {
			//如果缓存中有直接响应结果
			String json = jedisClient.hget(CONTENT_LIST, cid+"");
			//System.out.println(json);
			if (StringUtils.isNotBlank(json)) {
				List<TbContent> list = JsonUtils.jsonToList(json, TbContent.class);
				return list;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		//设置查询条件
		criteria.andCategoryIdEqualTo(cid);
		//执行查询
		List<TbContent> list = contentMapper.selectByExampleWithBLOBs(example);
		//把结果添加到缓存
				try {
					jedisClient.hset(CONTENT_LIST, cid+"", JsonUtils.objectToJson(list));
				} catch (Exception e) {
					e.printStackTrace();
				}
		return list;
	}
}
