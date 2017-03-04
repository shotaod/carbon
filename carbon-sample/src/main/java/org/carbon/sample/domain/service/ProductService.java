package org.carbon.sample.domain.service;

import java.util.List;

import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Inject;
import org.carbon.sample.ext.jooq.tables.pojos.Product;
import org.jooq.DSLContext;

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
