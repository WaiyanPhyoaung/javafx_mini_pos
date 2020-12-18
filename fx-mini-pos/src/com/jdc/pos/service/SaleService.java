package com.jdc.pos.service;

import java.time.LocalDate;
import java.util.List;

import com.jdc.pos.entities.Category;
import com.jdc.pos.entities.Item;
import com.jdc.pos.entities.OrderDetails;
import com.jdc.pos.entities.Voucher;
import com.jdc.pos.service.db.SaleServiceImpl;

public interface SaleService{

	public static SaleService getInstance() {
		return SaleServiceImpl.getInstance();
	}
	
	void paid(Voucher voucher);
	
	List<OrderDetails> search(Category c, Item item, 
			LocalDate dateFrom, LocalDate dateTo);
	
}
























