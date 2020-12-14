package com.jdc.pos.service.db;

import java.util.ArrayList;
import java.util.List;

import com.jdc.pos.entities.Category;
import com.jdc.pos.entities.Item;
import com.jdc.pos.service.ItemService;

public class ItemServiceImpl implements ItemService {
	
	private static final String Query= "select id, name, category, price from item where 1=1";
	
	private static final ItemService INSTANCE = new ItemServiceImpl();
	
	private ItemServiceImpl() {
		
	}
	
	public static ItemService getItemServiceDB() {
		return INSTANCE;
	}

	@Override
	public List<Item> search(Category c, String name) {
		//query
		StringBuilder query = new StringBuilder(Query);
		//parameter size
		List<Object> param = new ArrayList<Object>();
		List<Item> items = new ArrayList<Item>();
		
		if(null != c) {
			query.append(" and category=?");
			param.add(c.toString());
		}
		if(null != name && !name.isEmpty()) {
			query.append(" and name like ?");
			param.add(name.concat("%"));
		}
		
		return items;
	}

	@Override
	public void add(Item item) {
		

	}

	@Override
	public void add(String path) {
		

	}

}
