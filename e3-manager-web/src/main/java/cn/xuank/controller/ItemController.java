package cn.xuank.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.xuank.common.pojo.EasyUIDataGridResult;
import cn.xuank.common.utils.E3Result;
import cn.xuank.pojo.TbItem;
import cn.xuank.pojo.TbItemDesc;
import cn.xuank.ItemService;

/**
 * 商品管理Controller

 */
@Controller
public class ItemController {

	@Autowired
	private ItemService itemService;
	
	@RequestMapping("/item/list")
	@ResponseBody
	public EasyUIDataGridResult getItemList(Integer page, Integer rows) {
		//调用服务查询商品列表
		EasyUIDataGridResult result = itemService.getItemList(page, rows);
		return result;
	}
	/**
	 * 商品添加功能
	 */
	@RequestMapping(value="/item/save", method=RequestMethod.POST)
	@ResponseBody
	public E3Result addItem(TbItem item, String desc) {
		E3Result result = itemService.addItem(item, desc);
		return result;
	}
	/**
	 * 商品删除功能
	 */
	@RequestMapping(value="/rest/item/delete",method=RequestMethod.POST)
	@ResponseBody
	public E3Result delete(TbItem item) {
		//System.out.println(item.getIds().get(0));
		E3Result result = itemService.deleteItem(item);
		return result;
	}
	/**
	 * 商品修改功能
	 */
	@RequestMapping(value="/rest/item/query/item/desc/{itemId}")
	@ResponseBody
	public E3Result editbyIdd(@PathVariable Long itemId) {
		E3Result tbItemdesc = itemService.editbyIdd(itemId);
		return tbItemdesc;
	}
	/**
	 * 商品更新功能
	 */
	@RequestMapping(value="/rest/item/update", method=RequestMethod.POST)
	@ResponseBody
	public E3Result updateItem(TbItem item, String desc) {
		E3Result result = itemService.updateItem(item, desc);
		return result;
	}
	/**
	 * 商品状态功能
	 */
	@RequestMapping("/rest/item/{method}")
	@ResponseBody	
	public E3Result updateItemStatus(@RequestParam(value="ids")List<Long> ids,@PathVariable String method) {		
		E3Result result = itemService.updateItemStatus(ids,method);		
		return result;
	}

	
	
}
