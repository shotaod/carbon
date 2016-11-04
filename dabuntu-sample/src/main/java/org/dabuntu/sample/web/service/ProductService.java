package org.dabuntu.sample.web.service;

import org.dabuntu.component.annotation.Component;
import org.dabuntu.sample.exception.ResouceNotFoundException;
import org.dabuntu.sample.repository.Products;

import java.util.List;
import java.util.Optional;

/**
 * @author ubuntu 2016/10/04.
 */
@Component
public class ProductService {
	private Products products = Products.repository;

	public List<Products.Product> getProductsAll() {
		return products.getProducts();
	}

	public Products.Product getProduct(Integer productId) {
		Optional<Products.Product> opProduct = products.getProductById(productId);
		if (!opProduct.isPresent()) {
			throw new ResouceNotFoundException("プロダクト(Id=" + productId + ") は存在しません");
		}

		System.out.println(opProduct.get().getName());
		return opProduct.get();
	}

	public void buyProduct(Integer productId) {
		products.getProductById(productId).ifPresent(product -> {
			System.out.println("Buy " + product.getName() + ", Price is ￥" + product.getPrice() * 1.08);
		});
	}
}
