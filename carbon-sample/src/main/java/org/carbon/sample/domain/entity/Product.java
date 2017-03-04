package org.carbon.sample.domain.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Shota Oda 2016/10/04.
 */
@Getter
@Setter
@Entity
public class Product {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private Integer price;
}
