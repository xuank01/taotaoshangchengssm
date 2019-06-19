package cn.xuank.search.mapper;

import java.util.List;

import cn.xuank.common.pojo.SearchItem;



public interface ItemMapper {

	List<SearchItem> getItemList();
	SearchItem getItemById(long itemId);
}
