package application;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;

public class Inventory {
	
	String item, quantity, category;
	double price;
	CheckBox available;
	
	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public CheckBox getAvailable() {
		return available;
	}

	public void setAvailable(CheckBox available) {
		this.available = available;
	}
	
	public Inventory(String item, String quantity, String category, double price, byte available) {
		super();
		this.item = item;
		this.quantity = quantity;
		this.category = category;
		this.price = price;
		this.available = new CheckBox();
		if(available == 1) {
			this.available.setSelected(true);
		}
		this.available.selectedProperty().addListener(
				(ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) -> {
					UserDB.itemAvailable(this.available.isSelected(), this.item, this.quantity);
				});
	}

}
