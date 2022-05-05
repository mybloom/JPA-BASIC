package com.lecture.jpabasic.shop;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Delivery {

	@Id
	@GeneratedValue
	private Long id;

	private String city;
	private String street;
	private String zipcode;
	private DeliveryStatus deliveryStatus;

	@OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY )
	private Order order;

}
