package cn.xuank.content.service;

import java.util.List;

import cn.xuank.common.pojo.EasyUIDataGridResult;
import cn.xuank.common.utils.E3Result;
import cn.xuank.pojo.TbContent;
import cn.xuank.pojo.TbItem;

public interface ContentService {
	EasyUIDataGridResult getContentList(long categoryId,int page, int rows);
	E3Result addContent(TbContent content);
	E3Result deleteContent(TbContent content);
	E3Result updateContent(TbContent content);
	List<TbContent> getContentListByCid(long cid);
}
