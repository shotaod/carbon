package org.carbon.sample.domain.service;

import org.carbon.sample.tables.pojos.Product;
import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Inject;
import org.jooq.DSLContext;

import java.util.List;

/**
 * @author Shota Oda 2016/10/04.
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
