package cn.xuank;

import java.util.List;

import cn.xuank.common.pojo.EasyUIDataGridResult;
import cn.xuank.common.utils.E3Result;
import cn.xuank.pojo.TbItem;
import cn.xuank.pojo.TbItemDesc;

public interface ItemService {

	EasyUIDataGridResult getItemList(int page, int rows);
	E3Result addItem(TbItem item, String desc);
	E3Result deleteItem(TbItem item);
	E3Result editbyIdd(long itemId);
	E3Result updateItem(TbItem item, String desc);
	E3Result updateItemStatus(List<Long> ids,String method);
	TbItemDesc getItemDescById(long itemId);
	TbItem getItemById(long itemId);
}
