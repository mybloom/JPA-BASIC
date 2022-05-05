package com.lecture.jpabasic.hellojpa;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Book extends Item{

	private String author;
	private String isbn;


}
