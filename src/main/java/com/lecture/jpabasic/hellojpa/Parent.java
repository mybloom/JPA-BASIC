package com.lecture.jpabasic.hellojpa;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Parent {

	@Id
	@GeneratedValue
	@Column(name = "MEMBER_ID")
	private Long id;

	private String name;

	@OneToMany(mappedBy = "parent",cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Child> childList = new ArrayList<>();

	public void addChild(Child child) {
		childList.add(child);
		child.setParent(this);
	}
}
