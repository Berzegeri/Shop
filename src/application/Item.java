package application;

import java.io.Serializable;

public class Item implements Serializable {
	private String name;
	private String category;
	private int price;
	public int db;
	private String PhotoPath;
	
	public Item(String n, int prc) {
		name=n;		
		price=prc;
		db=0;
		PhotoPath="photos/no_photo.png";
	}
		
	public String defCat(String in) {
		category=in;
		return "You succesfully categorized "+name;
	}

	public String[] getStr() {
		String[] out= {this.name,this.category,PhotoPath};
		return out;
	}
	
	public int[] getNumb() {
		int[] out= {this.price,this.db};
		return out;
	}
	public void setPath(String in) {
		PhotoPath=in;
	}
}
