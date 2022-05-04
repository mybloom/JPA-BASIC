package com.lecture.jpabasic.hellojpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Member {

	@Id
	private Long id;
	@Column(unique = true, length = 10)
	private String name;

	@Override
	public String toString() {
		return "Member{" +
			"id=" + id +
			", name='" + name + '\'' +
			'}';
	}
}
