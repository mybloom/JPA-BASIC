package com.lecture.jpabasic.shop;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.extern.apachecommons.CommonsLog;

@Entity
@Table(name = "ORDERS")
public class Order {

	@Id
	@GeneratedValue
	@Column(name = "ORDER_ID")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "MEMBER_ID")
	private Member member;

	@OneToOne
	@JoinColumn(name = "DELIVERY_ID")
	private Delivery delivery;

	@OneToMany(mappedBy = "order")
	private List<OrderItem> orderItemList = new ArrayList<>();

	private LocalDateTime orderDate;

	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus;

	public void addOrderItem(OrderItem orderItem) {
		orderItemList.add(orderItem);
		orderItem.setOrder(this);
	}
}
