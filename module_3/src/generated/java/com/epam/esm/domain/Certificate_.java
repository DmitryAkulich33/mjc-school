package com.epam.esm.domain;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Certificate.class)
public abstract class Certificate_ {

	public static volatile SingularAttribute<Certificate, Integer> duration;
	public static volatile SingularAttribute<Certificate, Double> price;
	public static volatile SingularAttribute<Certificate, LocalDateTime> lastUpdateDate;
	public static volatile SingularAttribute<Certificate, String> name;
	public static volatile SingularAttribute<Certificate, String> description;
	public static volatile SingularAttribute<Certificate, Integer> lock;
	public static volatile ListAttribute<Certificate, Order> orders;
	public static volatile SingularAttribute<Certificate, Long> id;
	public static volatile SingularAttribute<Certificate, LocalDateTime> createDate;
	public static volatile ListAttribute<Certificate, Tag> tags;

	public static final String DURATION = "duration";
	public static final String PRICE = "price";
	public static final String LAST_UPDATE_DATE = "lastUpdateDate";
	public static final String NAME = "name";
	public static final String DESCRIPTION = "description";
	public static final String LOCK = "lock";
	public static final String ORDERS = "orders";
	public static final String ID = "id";
	public static final String CREATE_DATE = "createDate";
	public static final String TAGS = "tags";

}

