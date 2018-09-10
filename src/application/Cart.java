package application;

public class Cart {
	private String name="";
	private int price=0;
	private int db = 1;
	private int max=0;

	public Cart(String n, int p, int m) {
		name=n;
		price=p;
		max=m;
	}
	
	public void inc() {
		db++;
	}
	public void dec() {
		db--;
	}
	public String getName() {
		return name;
	}

	public int getPr() {
		return price;
	}
	public int getDB() {
		return db;
	}
	public int getMax() {
		return max;
	}	
}
