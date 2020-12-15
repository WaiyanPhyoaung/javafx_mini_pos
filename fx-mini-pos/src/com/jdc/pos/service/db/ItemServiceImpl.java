package com.jdc.pos.service.db;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.jdc.pos.entities.Category;
import com.jdc.pos.entities.Item;
import com.jdc.pos.service.ItemService;
import com.jdc.pos.util.ConnectionManager;
import com.jdc.pos.util.MessageHandler;

public class ItemServiceImpl implements ItemService {
	
	private static final String Query= "select id, name, category, price from item where 1=1";
	private static final String Insert= "insert into item (id, name, category, price) values (?,?,?,?)";
	
	private static final ItemService INSTANCE = new ItemServiceImpl();
	
	private ItemServiceImpl() {
		
	}
	
	public static ItemService getItemService() {
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
		
		//MessageHandler.showAlert("Query : " + query.toString());
		
		try {
			Connection con = ConnectionManager.getConnection();
			PreparedStatement prep = con.prepareStatement(query.toString());
			
			//set paramaters
			for(int i=0; i < param.size(); i++) {
				prep.setObject(i+1, param.get(i));
			}
			
			ResultSet resultSet = prep.executeQuery();
			
			while(resultSet.next()) {				
				Item item = getItemFormDB(resultSet);				
				items.add(item);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return items;
	}

	private Item getItemFormDB(ResultSet resultSet) throws SQLException {
		Item item = new Item();
		item.setCategory(Category.valueOf(resultSet.getString("category")));
		item.setId(resultSet.getInt("id"));
		item.setName(resultSet.getString("name"));
		item.setPrice(resultSet.getInt("price"));
		return item;
	}

	@Override
	public void add(Item item) {
		try {
			Connection con = ConnectionManager.getConnection();
			PreparedStatement prep = con.prepareStatement(Insert);
			
			prep.setInt(1, item.getId());
			prep.setString(2, item.getName());
			prep.setString(3, String.valueOf(item.getCategory()));
			prep.setInt(4, item.getPrice());
			
			prep.executeUpdate();
					
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		

	}

	@Override
	public void add(String path) {
		try {
			Files.lines(Paths.get(path)).skip(1)
			.map(line -> line.split("\t"))
			.map(arr -> new Item(arr))
			.forEach(item-> add(item));			
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
