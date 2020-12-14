package com.jdc.pos.service.memory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.jdc.pos.entities.Category;
import com.jdc.pos.entities.Item;
import com.jdc.pos.service.ItemService;
import com.jdc.pos.util.MessageHandler;

public class ItemServiceImpl implements ItemService{

	private static ItemService itemService = new ItemServiceImpl();
	
	private List<Item> items;
	
	ItemServiceImpl() {
		if(null == items)
			items = new ArrayList<Item>();
	}
	
	public static ItemService getItemService() {
		return itemService;
	}
	
	@Override
	public List<Item> search(Category c, String idName) {
		
		Predicate<Item> cond = a -> true;
		
		if(null != c) {
			cond = cond.and(item -> item.getCategory() == c);
		}
		
		if(null != idName && !idName.isEmpty()) {
			
			Predicate<Item> id  = item -> String.valueOf(item.getId())
					.equals(idName);
			
			Predicate<Item> name = item -> item.getName().toLowerCase()
					.startsWith(idName.toLowerCase());
			
			cond = cond.and(id.or(name));
		}
		
		return items.stream().filter(cond).collect(Collectors.toList());
	}

	@Override
	public void add(Item item) {
		
	}

	@Override
	public void add(String path) {
		try {
			items = Files.lines(Paths.get(path)).skip(1)
						.map(line -> line.split("\t"))
						.map(arr -> new Item(arr))
						.collect(Collectors.toList());
			
			MessageHandler.showAlert("Size: " + items.size());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
}
