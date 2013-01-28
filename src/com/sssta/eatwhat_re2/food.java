package com.sssta.eatwhat_re2;

import java.text.DecimalFormat;

import android.R.integer;
import android.R.string;

public class food {
	private Integer id;
	private String name;
	private String place1;
	private String place2;
	private Integer rate;
	private Double price;
	private int num_random;
	public int getNum_random() {
		return num_random;
	}

	public void setNum_random(int num_random) {
		this.num_random = num_random;
	}
	DecimalFormat df = new DecimalFormat("#.00");
	public food()
	{
		
	}
	
	public food(Integer id, String name,String place1,String place2,Integer rate,Double price)
	{
		df.format(price);
		this.id=id;
		this.name=name;
		this.place1=place1;
		this.place2=place2;
		this.rate=rate;
		this.price=price;
		
	}
	public food(String name,String place1,String place2,Integer rate,Double price)
	{
		df.format(price);
		
		this.name=name;
		this.place1=place1;
		this.place2=place2;
		this.rate=rate;
		this.price=price;
		
	}
	public String getName() {
		return name;
	}
	public void setName(String string) {
		this.name = string;
	}
	public String getPlace1() {
		return place1;
	}
	public void setPlace1(String string) {
		this.place1 = string;
	}
	public String getPlace2() {
		return place2;
	}
	public Integer getRate() {
		return rate;
	}
	public void setRate(Integer rate) {
		this.rate = rate;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		df.format(price);
		this.price = price;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public void setPlace2(String place2) {
		// TODO Auto-generated method stub
		this.place2 = place2;
	}
	 public String toString() {  
	        // TODO Auto-generated method stub  
		 return name+" "+place1+"-"+place2+" "+rate+" "+price +"\n";    		
	 } 
	 public String toString2()
	 {
		 return place1+"-"+place2+" "+"评分："+rate+" "+ "价格："+price ;
	 }
	 public String toRenren()
	 {
		 return "我在"+place1+place2+"吃" + name + "———来自 吃什么";
	 }
}
