package com.jdc.pos.views;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.ToIntFunction;

import com.jdc.pos.entities.Category;
import com.jdc.pos.entities.Item;
import com.jdc.pos.entities.OrderDetails;
import com.jdc.pos.entities.Voucher;
import com.jdc.pos.service.ItemService;
import com.jdc.pos.service.SaleService;
import com.jdc.pos.util.MessageHandler;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;

public class Pos implements Initializable {

    @FXML
    private ComboBox<Category> category;
    @FXML
    private TextField idName;
    @FXML
    private Label headTotal;
    @FXML
    private TableView<Item> itemTable;
    @FXML
    private TableView<OrderDetails> cartTable;
    @FXML
    private Label subTotal;
    @FXML
    private Label tax;
    @FXML
    private Label total;
    @FXML
    private TableColumn<OrderDetails, Integer> count;
    
    private ItemService itemService;
    private SaleService saleService;
    
    @FXML
    void clearCart() {
    	cartTable.getItems().clear();
    	calculate();
    }
    
    private void calculate() {    	
    	
    	subTotal.setText(getData(od -> od.getSubTotal()).toString());   	
    	tax.setText(getData(od -> od.getTax()).toString());
    	total.setText(getData(od -> od.getTotal()).toString());
	}

	private Integer getData(ToIntFunction<OrderDetails>value) {
		
		return cartTable.getItems().stream().mapToInt(value).sum();
	}


	@FXML
    void clearSearch() {
    	category.setValue(null);
    	idName.clear();
    }

    @FXML
    void delete() {
    	OrderDetails od = cartTable.getSelectionModel().getSelectedItem();
    	cartTable.getItems().remove(od);
    	calculate();
    	cartTable.getSelectionModel().clearSelection();
    }

    @FXML
    void paid() {
    	try {
    		Voucher voucher = new Voucher();
    		voucher.setSaleDate(LocalDate.now());
    		voucher.setSaleTime(LocalTime.now());
    		voucher.setId(voucher.getSaleTime().toSecondOfDay());
    		voucher.getList().addAll(cartTable.getItems());
    		
    		saleService.paid(voucher);
    		clearCart();
			
		} catch (Exception e) {
			MessageHandler.showAlert(e);
		}
    }

    public void search() {
    	try {      		
    		
			List<Item> items = itemService.search(category.getValue(), idName.getText());
			itemTable.getItems().clear();
			itemTable.getItems().addAll(items);
			
		} catch (Exception e) {
			e.printStackTrace();
		//	MessageHandler.showAlert(e);
		}
    }
    
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		itemService = ItemService.getInstance();
		saleService = SaleService.getInstance();
		
		category.getItems().addAll(Category.values());
		
		headTotal.textProperty().bind(total.textProperty());	
		
		
		category.valueProperty().addListener( (a,b,c) -> search());
		idName.textProperty().addListener( (a,b,c) -> search());
		
		itemTable.setOnMouseClicked(event -> {
			
			if(event.getClickCount() == 2) {
				Item item = itemTable.getSelectionModel().getSelectedItem();
				
				OrderDetails detail = cartTable.getItems().stream()
						.filter( order -> order.getItem().getId() == item.getId()).findAny().orElse(null);
				
				if(detail == null) {
					detail = new OrderDetails();
					detail.setItem(item);
					cartTable.getItems().add(detail);
				}
					detail.setCount(detail.getCount() + 1);
					detail.calculate();
										
					calculate();
					cartTable.refresh();				
			}
		});
		search();
		
		//context menu apply			
			
		MenuItem delete = new MenuItem("Delete");
		delete.setOnAction( a -> delete());
			
		ContextMenu contextMenu = new ContextMenu(delete);			
		cartTable.setContextMenu(contextMenu);
		
		//set column editable
		
		cartTable.setEditable(true);
		
		//converter for column table
		
		count.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Integer>() {

			@Override
			public Integer fromString(String string) {
				try {
					if(null != string && !string.isEmpty()) {
						return Integer.parseInt(string);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			public String toString(Integer integer) {
				if(null!= integer) {
					return integer.toString();
				}
				return null;
			}
		}));
		
		//event after commit in table_column
		count.setOnEditCommit(event -> {
			OrderDetails detail = event.getRowValue();
			detail.setCount(event.getNewValue());
			
			detail.calculate();
			cartTable.refresh();
			calculate();
		});
	}	


}


















