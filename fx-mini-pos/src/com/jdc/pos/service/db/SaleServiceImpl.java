package com.jdc.pos.service.db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.jdc.pos.entities.Category;
import com.jdc.pos.entities.Item;
import com.jdc.pos.entities.OrderDetails;
import com.jdc.pos.entities.Voucher;
import com.jdc.pos.service.SaleService;
import com.jdc.pos.util.ConnectionManager;

public class SaleServiceImpl implements SaleService {
	
	private static final SaleService INSTANCE = new SaleServiceImpl();
	
	private static final String INSERT_VOUCHER = "INSERT INTO voucher (saleDate,saleTime) VALUES (?,?)";
	private static final String INSERT_ORDER = "INSERT INTO orderdetail (item,voucher,count,subTotal,tax,total) VALUES "
			+ "(?,?,?,?,?,?)";
	private static final String SELECT ="select od.id detailId, v.id voucherID, i.id itemId, v.saleDate, v.saleTime,"
			+ "i.category, i.name, i.price, od.count, od.tax, od.subTotal, od.total from orderdetail od "
			+ "join voucher v on od.voucher = v.id "
			+ "join item i on od.item = i.id where 1=1";
	
	private SaleServiceImpl() {}
	
	public static SaleService getInstance() {
		return INSTANCE;
	}
	
	@Override
	public void paid(Voucher voucher) {
		try {
			Connection con = ConnectionManager.getConnection();
			PreparedStatement prepVoucher = con.prepareStatement(INSERT_VOUCHER, Statement.RETURN_GENERATED_KEYS);
			PreparedStatement prepDetail = con.prepareStatement(INSERT_ORDER);
			
			prepVoucher.setDate(1, Date.valueOf(voucher.getSaleDate()));
			prepVoucher.setTime(2,Time.valueOf(voucher.getSaleTime()));
			
			prepVoucher.executeUpdate();
			
			ResultSet rs = prepVoucher.getGeneratedKeys();
			
		//	System.out.println(rs.next());
			
			while(rs.next()) {
				
				int voucherID = rs.getInt(1);
				
				for(OrderDetails detail : voucher.getList()) {					
					
					prepDetail.setInt(1, detail.getItem().getId());
					prepDetail.setInt(2, voucherID);
					prepDetail.setInt(3, detail.getCount());
					prepDetail.setInt(4, detail.getSubTotal());
					prepDetail.setInt(5, detail.getTax());
					prepDetail.setInt(6, detail.getTotal());
					
					prepDetail.executeUpdate();
					
				}				
				
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public List<OrderDetails> search(Category c, Item item, LocalDate dateFrom, LocalDate dateTo) {
		
			List<OrderDetails> list = new ArrayList<>();
				
			StringBuilder query = new StringBuilder(SELECT);
			List<Object> param = new ArrayList<>();
			
			if(null!=c) {
				query.append(" and i.category=?");
				param.add(c.toString());
			}
			if(null!=item) {
				query.append(" and i.name like ?");
				param.add(item.getName().concat("%"));
			}
			if(null!=dateFrom) {
				query.append(" and v.saleDate >= ?");
				param.add(Date.valueOf(dateFrom));
			}
			if(null!=dateTo) {
				query.append(" and v.saleTime <= ?");
				param.add(Date.valueOf(dateTo));
			}
			try {
				Connection conn = ConnectionManager.getConnection();
				PreparedStatement prep = conn.prepareStatement(query.toString());
				
				for(int i = 0 ; i < param.size() ; i++) {
					prep.setObject(i+1, param.get(i));
				}
				ResultSet rs = prep.executeQuery();				
				
				while(rs.next()) {
					
					// item athit saut pee shr loz ya daz data htez
					
					Item i = new Item();
					i.setId(rs.getInt("itemId"));
					i.setName(rs.getString("name"));
					i.setPrice(rs.getInt("price"));
					i.setCategory(Category.valueOf(rs.getString("category")));
					
					Voucher vo = new Voucher();
					vo.setId(rs.getInt("voucherId"));
					vo.setSaleTime(rs.getTime("saleTime").toLocalTime());
					vo.setSaleDate(rs.getDate("saleDate").toLocalDate());
										
					
					// item nae voucher ya b so orderdetail ko sat htez
					OrderDetails detail = new OrderDetails();
					
					detail.setId(rs.getInt("detailId"));
					detail.setCount(rs.getInt("count"));
					detail.setSubTotal(rs.getInt("subTotal"));
					detail.setTax(rs.getInt("tax"));
					detail.setTotal(rs.getInt("total"));
					
					detail.setItem(i);
					detail.setVoucher(vo);
					
					list.add(detail);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
					
		return list;
	}

}




