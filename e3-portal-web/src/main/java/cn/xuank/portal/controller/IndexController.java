package cn.xuank.portal.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.xuank.common.pojo.Ad1Node;
import cn.xuank.common.utils.JsonUtils;
import cn.xuank.content.service.ContentService;
import cn.xuank.pojo.TbContent;


/**
 * 首页展示Controller
 */
@Controller
public class IndexController {
	
	
	@Autowired
	private ContentService contentService;

		@Value("${AD1_CONTENT_CID}")
	    private Long AD1_CONTENT_CID;

	    @Value("${AD1_WIDTH}")
	    private Integer AD1_WIDTH;

	    @Value("${AD1_WIDTH_B}")
	    private Integer AD1_WIDTH_B;

	    @Value("${AD1_HEIGHT}")
	    private Integer AD1_HEIGHT;

	    @Value("${AD1_HEIGHT_B}")
	    private Integer AD1_HEIGHT_B;

	    @RequestMapping("/index")
	    public String showIndex1(Model model) {
	        // 取内容分类id，需要从属性文件中取
	        // 根据内容分类id查询内容列表
	        List<TbContent> contentList = contentService.getContentListByCid(AD1_CONTENT_CID);
	        List<Ad1Node> ad1NodeList = new ArrayList<Ad1Node>();
	        for (TbContent tbContent : contentList) {
	            Ad1Node node = new Ad1Node();
	            node.setAlt(tbContent.getSubTitle());
	            node.setHref(tbContent.getUrl());
	            node.setSrc(tbContent.getPic());
	            node.setSrcB(tbContent.getPic2());
	            node.setHeight(AD1_HEIGHT);
	            node.setHeightB(AD1_HEIGHT_B);
	            node.setWidth(AD1_WIDTH);
	            node.setWidthB(AD1_WIDTH_B);

	            ad1NodeList.add(node);
	        }
	        // 将List集合转成json字符串
	        String json = JsonUtils.objectToJson(ad1NodeList);
	        model.addAttribute("ad1", json);
	        return "index";
	    }
}
