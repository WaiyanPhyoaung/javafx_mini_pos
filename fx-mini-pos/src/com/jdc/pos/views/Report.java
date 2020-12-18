package com.jdc.pos.views;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import com.jdc.pos.entities.Category;
import com.jdc.pos.entities.Item;
import com.jdc.pos.entities.OrderDetails;
import com.jdc.pos.service.ItemService;
import com.jdc.pos.service.SaleService;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;

public class Report implements Initializable{
	
	private SaleService saleService;
	private ItemService itemService;
	

    @FXML
    private ComboBox<Category> category;

    @FXML
    private ComboBox<Item> item;

    @FXML
    private DatePicker dateFrom;

    @FXML
    private DatePicker dateTo;

    @FXML
    private TableView<OrderDetails> table;

    @FXML
    void clear() {
    	category.setValue(null);
    	item.setValue(null);
    	dateFrom.setValue(null);
    	dateTo.setValue(null);
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		saleService = SaleService.getInstance();
		itemService = ItemService.getInstance();
		
		item.getItems().clear();
		category.getItems().clear();
		category.getItems().addAll(Category.values());
		
		search();
		
		dateTo.setValue(LocalDate.now());
		
		category.valueProperty().addListener((a,b,c)->{
		List<Item> list = itemService.search(category.getValue(), null);
		item.getItems().clear();
		item.getItems().addAll(list);
		
		search();
		});
		
		item.valueProperty().addListener((a,b,c)->search());
		dateFrom.valueProperty().addListener((a,b,c)->search());
		dateTo.valueProperty().addListener((a,b,c)->search());
		
			
	}

	private void search() {
		table.getItems().clear();
		List<OrderDetails> list = saleService.search(category.getValue(), item.getValue(), dateFrom.getValue(), dateTo.getValue());
		
	//	System.out.println(list.toString());
		
		table.getItems().addAll(list);
	}


}
