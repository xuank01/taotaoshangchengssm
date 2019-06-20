package cn.xuank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.xuank.common.pojo.EasyUIDataGridResult;
import cn.xuank.common.utils.E3Result;
import cn.xuank.content.service.ContentService;
import cn.xuank.pojo.TbContent;
import cn.xuank.pojo.TbItem;
@Controller
public class ContentController {
	@Autowired
	private ContentService contentService;
	/**
	 * 内容显示功能
	 */
	@RequestMapping(value="/content/query/list" ,method=RequestMethod.GET)
	@ResponseBody
	    public  EasyUIDataGridResult getContentList(@RequestParam(name="categoryId", defaultValue="0")Long categoryId, @RequestParam(name="page", defaultValue="1")Integer page, @RequestParam(name="rows", defaultValue="20")Integer rows) {
	    EasyUIDataGridResult content=contentService.getContentList(categoryId,page,rows);
	    return content;
	}
	/**
	 * 内容添加功能
	 */
	@RequestMapping(value="/content/save", method=RequestMethod.POST)
	@ResponseBody
	public E3Result addContent(TbContent content) {
		E3Result result = contentService.addContent(content);
		return result;
	}
	/**
	 * 内容删除功能
	 */
	@RequestMapping(value="/content/delete", method=RequestMethod.POST)
	@ResponseBody
	public E3Result deleteContent(TbContent content) {
		E3Result result = contentService.deleteContent(content);
		return result;
	}
	
	@RequestMapping(value="/rest/content/edit", method=RequestMethod.POST)
	@ResponseBody
	public E3Result updateItem(TbContent content) {
		E3Result result = contentService.updateContent(content);
		return result;
	}
}
