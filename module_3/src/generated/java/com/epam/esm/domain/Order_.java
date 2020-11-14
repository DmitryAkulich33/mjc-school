package com.epam.esm.domain;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Order.class)
public abstract class Order_ {

	public static volatile SingularAttribute<Order, LocalDateTime> purchaseDate;
	public static volatile SingularAttribute<Order, Double> total;
	public static volatile ListAttribute<Order, Certificate> certificates;
	public static volatile SingularAttribute<Order, Integer> lock;
	public static volatile SingularAttribute<Order, Long> id;
	public static volatile SingularAttribute<Order, User> user;

	public static final String PURCHASE_DATE = "purchaseDate";
	public static final String TOTAL = "total";
	public static final String CERTIFICATES = "certificates";
	public static final String LOCK = "lock";
	public static final String ID = "id";
	public static final String USER = "user";

}

