package com.jdc.pos.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Voucher implements Serializable{

	
	private static final long serialVersionUID = 1L;
	
	private int id;
	private LocalDate saleDate;
	private LocalTime saleTime;
	private List<OrderDetails> list;
	
	public Voucher() {
		list = new ArrayList<OrderDetails>();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LocalDate getSaleDate() {
		return saleDate;
	}

	public void setSaleDate(LocalDate saleDate) {
		this.saleDate = saleDate;
	}

	public LocalTime getSaleTime() {
		return saleTime;
	}

	public void setSaleTime(LocalTime saleTime) {
		this.saleTime = saleTime;
	}

	public List<OrderDetails> getList() {
		return list;
	}

	public void setList(List<OrderDetails> list) {
		this.list = list;
	}
	
	
}
