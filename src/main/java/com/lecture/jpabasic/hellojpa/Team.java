package com.lecture.jpabasic.hellojpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Team {

	@Id
	@GeneratedValue
	@Column(name = "TEAM_ID")
	private Long id;
	private String name;
}
