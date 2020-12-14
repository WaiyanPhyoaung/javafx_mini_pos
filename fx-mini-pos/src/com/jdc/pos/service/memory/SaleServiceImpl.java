package com.jdc.pos.service.memory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import com.jdc.pos.entities.Category;
import com.jdc.pos.entities.Item;
import com.jdc.pos.entities.OrderDetails;
import com.jdc.pos.entities.Voucher;
import com.jdc.pos.service.SaleService;


public class SaleServiceImpl implements SaleService {

	private static SaleService saleService = new SaleServiceImpl();
	public static final String SALE_LIST = "vouchers.obj";
	private List<Voucher> vouchers;
	
	
	@SuppressWarnings("unchecked")
	SaleServiceImpl() {
		if(vouchers == null) {
			vouchers = new ArrayList<Voucher>();
		}
		
		try {
				FileInputStream input = new FileInputStream(SALE_LIST);
				@SuppressWarnings("resource")
				ObjectInputStream objInput = new ObjectInputStream(input);
				
				vouchers = (List<Voucher>) objInput.readObject();
				
			} catch (Exception e) {
				e.printStackTrace();
			
			}
	}
	
	public static SaleService getInstance() {
		return saleService;
	}
		
	@Override
	public void paid(Voucher voucher) {
		voucher.getList().forEach(od -> od.setVoucher(voucher));
		vouchers.add(voucher);
		System.out.println(vouchers.size());
		saveFile(vouchers);
		
	}

	private void saveFile(List<Voucher> vouchers) {
		try {FileOutputStream output = new FileOutputStream(SALE_LIST);
				@SuppressWarnings("resource")
				ObjectOutputStream objOutput = new ObjectOutputStream(output);
			
			objOutput.writeObject(vouchers);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public List<OrderDetails> search(Category c, Item item, LocalDate dateFrom, LocalDate dateTo) {
		
		Predicate<OrderDetails> cond = a -> true;
		
		if(null != c) {
			cond = cond.and(detail -> detail.getItem().getCategory() == c);
		}
		
		if(null != item) {
			cond = cond.and(detail -> detail.getItem().getId()==item.getId());
		}
		
		if(null != dateFrom) {
			cond = cond.and(detail -> detail.getVoucher()
					.getSaleDate().compareTo(dateFrom) >= 0);
		}
		
		if(null != dateTo) {
			cond = cond.and(detail -> detail.getVoucher()
					.getSaleDate().compareTo(dateTo) <= 0 );
		}
		
		return vouchers.stream()
				.flatMap(voucher -> voucher.getList().stream())
				.filter(cond)
				.collect(Collectors.toList());
	}

}









