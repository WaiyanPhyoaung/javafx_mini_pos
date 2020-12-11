package com.jdc.pos.views;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.jdc.pos.entities.OrderDetails;
import com.jdc.pos.service.SaleService;
import com.jdc.pos.util.MessageHandler;

import javafx.fxml.Initializable;

public class Report implements Initializable {
	
	private static SaleService saleService;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		saleService = SaleService.getInstance();
		List<OrderDetails> list = saleService.search(null, null, null, null);
		MessageHandler.showAlert("Size : " + list.size());
	}

}
