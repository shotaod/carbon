package org.dabuntu.sample.domain.entity;

import java.util.*;

/**
 * @author ubuntu 2016/10/04.
 */
public class Products {

	public static class Product {
		private String name;
		private Integer price;

		public Product(String name, Integer price) {
			this.name = name;
			this.price = price;
		}

		public Integer getPrice() {
			return price;
		}

		public String getName() {
			return name;
		}
	}

	public static Products repository = new Products();

	// ===================================================================================
	//                                                                          Private
	//                                                                          ==========
	private List<Product> products;

	private Products() {
		this.products = Arrays.asList(
			new Product("トマト", 100),
			new Product("かぼちゃ", 500),
			new Product("グレープフルーツ", 150),
			new Product("スニッカーズ", 120),
			new Product("ラッキーストライク", 460),
			new Product("赤マル", 440),
			new Product("ピース", 460),
			new Product("ICOS", 9800)
		);
	}

	public List<Product> getProducts() {
		return this.products;
	}

	public Optional<Product> getProductById(Integer id) {
		if (this.products.size() <= id) {
			return Optional.empty();
		}

		return Optional.of(this.products.get(id));
	}
}
