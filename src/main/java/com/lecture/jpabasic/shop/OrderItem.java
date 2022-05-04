package com.lecture.jpabasic.shop;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class OrderItem {

	@Id
	@GeneratedValue
	@Column(name = "ORDER_ITEM_ID")
	private Long id;

	@Column(name = "ORDER_ID")
	private Long order_id;

	@Column(name = "ITEM_ID")
	private Long itemId;

	private int orderPrice;
	private int count;
}
