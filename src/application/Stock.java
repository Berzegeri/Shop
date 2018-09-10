package application;

import java.util.List;
import java.io.Serializable;
import java.util.ArrayList;

public class Stock implements Serializable {
	private List<Item> supply = new ArrayList<Item>();
	private List<String> category = new ArrayList<String>();

	public Stock() {

	}

	public String addCat(String addable) { // Admin
		String correct = addable.toLowerCase();
		for (int i = 0; i < category.size(); i++) {
			if (category.get(i).equals(correct))
				return "Categroy can not be added, because it's already exists.";
		}
		category.add(correct);
		return (addable + " succesfully added.");
	}

	public String removeCat(String removable) { // Admin
		String correct = removable.toLowerCase();
		for (int i = 0; i < supply.size(); i++) {
			if (supply.get(i).getStr()[1].equals(correct))
				return "Categroy can not be removed.";
		}
		category.remove(correct);
		return (removable + " succesfully removed.");
	}

	public String addItem(Item in, int db, String cat, String path) { // Admin
		for (int i = 0; i < supply.size(); i++)
			if (supply.get(i).getStr()[0].equals(in.getStr()[0]))
				return in.getStr()[0] + " already exists.";

		in.db = db;
		in.defCat(cat);
		this.addCat(cat);
		in.setPath(path);
		supply.add(in);
		return "Done.";
	}

	public String removeItem(String in) { // Admin
		for (int i = 0; i < supply.size(); i++)
			if (supply.get(i).getStr()[0].equals(in)) {
				supply.remove(i);
				return in + " succesfully removed.";
			}
		return "There is no " + in + " in the warehouse.";
	}

	public String sell(String out, int db) { // User
		for (int i = 0; i < supply.size(); i++) {
			if (supply.get(i).getStr()[0].equals(out)) {
				if (db > supply.get(i).db)
					return "There are not enough pieces in the store!";
				else {
					supply.get(i).db -= db;
					return "Succesfully sold!";
				}
			}
		}
		return "";
	}

	public List<String> getCats() {
		return category;
	}

	public List<String> getNames() {
		List<String> tmp = new ArrayList<String>();
		for (int i = 0; i < supply.size(); i++)
			tmp.add(supply.get(i).getStr()[0]);
		return tmp;
	}

	public List<Item> getSupply() {
		return supply;
	}
}
