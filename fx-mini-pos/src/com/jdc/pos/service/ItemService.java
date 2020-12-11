package com.jdc.pos.service;

import java.util.List;

import com.jdc.pos.entities.Category;
import com.jdc.pos.entities.Item;
import com.jdc.pos.service.memory.ItemServiceImpl;

public interface ItemService {
	
	public static ItemService getInstance() {
		return ItemServiceImpl.getItemService();
	}
	
	List<Item> search(Category c, String name);
	
	void add(Item item);

	void add(String path);
}
