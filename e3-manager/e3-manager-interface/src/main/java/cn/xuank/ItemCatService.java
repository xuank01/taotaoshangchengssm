package cn.xuank;

import java.util.List;

import cn.xuank.common.pojo.EasyUITreeNode;

public interface ItemCatService {

	List<EasyUITreeNode> getItemCatlist(long parentId);
}
