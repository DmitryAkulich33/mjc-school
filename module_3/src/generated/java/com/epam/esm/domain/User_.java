package com.epam.esm.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(User.class)
public abstract class User_ {

	public static volatile SingularAttribute<User, String> surname;
	public static volatile SingularAttribute<User, String> name;
	public static volatile SingularAttribute<User, Integer> lock;
	public static volatile ListAttribute<User, Order> orders;
	public static volatile SingularAttribute<User, Long> id;

	public static final String SURNAME = "surname";
	public static final String NAME = "name";
	public static final String LOCK = "lock";
	public static final String ORDERS = "orders";
	public static final String ID = "id";

}

