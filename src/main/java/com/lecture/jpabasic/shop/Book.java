package com.lecture.jpabasic.shop;


import javax.persistence.Entity;
import lombok.Setter;

@Entity
@Setter
public class Book extends Item {

	private String author;
	private String isbn;
}
