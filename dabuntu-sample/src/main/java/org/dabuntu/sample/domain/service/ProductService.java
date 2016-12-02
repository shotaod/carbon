package org.dabuntu.sample.domain.service;

import org.dabunt.sample.tables.pojos.Product;
import org.dabuntu.component.annotation.Component;
import org.dabuntu.component.annotation.Inject;
import org.jooq.DSLContext;

import java.util.List;

/**
 * @author ubuntu 2016/10/04.
 */
@Component
public class ProductService {
	@Inject
	private DSLContext jooq;

	public List<Product> getProductsAll() {
		return null;
	}

	public Product getProduct(Long productId) {
		return null;
	}
}
