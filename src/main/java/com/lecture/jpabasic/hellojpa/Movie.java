package com.lecture.jpabasic.hellojpa;

import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Movie extends Item{

	private String director;
	private String actor;
}
