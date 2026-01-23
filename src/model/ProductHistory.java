package model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "historical_inventory")

public class ProductHistory {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id", unique = true) 
	private int id;
	
	@Column(name = "available")
	private boolean available;
	
	@Column(name = "created_at")
	private String createdAt;
	
	@Column(name = "id_product")
	private int idProduct;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "price")
	private double price;
	
	@Column(name = "stock")
	private int stock;
	
	// (product.getId(), product.getName(), product.getWholesalerPrice(),
	// product.isAvailable(), product.getStock(), LocalDate.now().toString());
	public ProductHistory(int productId, String name, double wholesalerPrice, boolean available, int stock) {
		super();
		this.available = available;
		this.createdAt = LocalDateTime.now().toString();
		this.idProduct = productId;
		this.name = name;
		this.price = wholesalerPrice;
		this.stock = stock;
	}
	
	public ProductHistory(){} 

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPublicPrice() {
		return this.price * 2;
	}

	public void setPublicPrice(double publicPrice) {
		this.price = publicPrice / 2;
	}

	public double getWholesalerPrice() {
		return this.price;
	}

	public void setWholesalerPrice(double wholesalerPrice) {
		this.price = wholesalerPrice;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	@Override
	public String toString() {
		return "Product [name=" + name + ", publicPrice=" + (price * 2) + ", wholesalerPrice=" + price
				+ ", available=" + available + ", stock=" + stock + "]";
	}

}
