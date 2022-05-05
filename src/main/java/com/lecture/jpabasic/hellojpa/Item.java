package com.lecture.jpabasic.hellojpa;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import jdk.jfr.Enabled;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn //(name = "DIS_TYPE") //DTYPE : 어느 테이블에서 들어온 것인지 알기 위해, 자동으로는 DTYPE으로 들어왔는데 컬럼명을 명시해 줄 수 있다.
public class Item {
	@Id
	@GeneratedValue
	private Long id;

	private String name;
	private int price;
}
