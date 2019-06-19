package cn.xuank.content.service;

import java.util.List;

import cn.xuank.common.pojo.EasyUIDataGridResult;
import cn.xuank.common.pojo.EasyUITreeNode;
import cn.xuank.common.utils.E3Result;

public interface ContentCategoryService {

	List<EasyUITreeNode> getContentCatList(long parentId);
	E3Result addContentCategory(long parentId, String name);
	E3Result updataContentCategory(long id, String name);
	E3Result deleteContentCategory(long id);
	
}
