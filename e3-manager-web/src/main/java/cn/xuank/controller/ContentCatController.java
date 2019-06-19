package cn.xuank.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.xuank.common.pojo.EasyUIDataGridResult;
import cn.xuank.common.pojo.EasyUITreeNode;
import cn.xuank.common.utils.E3Result;
import cn.xuank.content.service.ContentCategoryService;
import cn.xuank.content.service.ContentService;

/**
 * 内容分类管理Controller
 */
@Controller
public class ContentCatController {

	@Autowired
	private ContentCategoryService contentCategoryService;
	private ContentService contentService;
	
	@RequestMapping("/content/category/list")
	@ResponseBody
	public List<EasyUITreeNode> getContentCatList(
			@RequestParam(name="id", defaultValue="0")Long parentId) {
		List<EasyUITreeNode> list = contentCategoryService.getContentCatList(parentId);
		return list;
	}
	
	/**
	 * 添加分类节点
	 */
	@RequestMapping(value="/content/category/create",method=RequestMethod.POST)
	@ResponseBody
	public E3Result createContentCategory(Long parentId, String name) {
		//调用服务添加节点
		E3Result e3Result = contentCategoryService.addContentCategory(parentId, name);
		return e3Result;
	}
	/**
	 * 更新分类节点
	 */
	@RequestMapping(value="/content/category/update",method=RequestMethod.POST)
	@ResponseBody
	public E3Result updataContentCategory(Long id, String name) {
		//调用服务添加节点
		E3Result e3Result = contentCategoryService.updataContentCategory(id, name);
		return e3Result;
	}
	
	@RequestMapping(value="/content/category/delete/",method=RequestMethod.POST)
	@ResponseBody
	public E3Result deleteContentCategory(Long id) {
		//调用服务添加节点
		E3Result e3Result = contentCategoryService.deleteContentCategory(id);
		return e3Result;
	}
	
}
