package com.lecture.jpabasic.hellojpa;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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

	@Column(name= "name", nullable = false, updatable = false)
	private String name;

	private Integer age;

	@Enumerated(EnumType.STRING)
//	@Enumerated(EnumType.ORDINAL)
	private RoleType roleType;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;

	@Temporal(TemporalType.TIMESTAMP)
	private Date lastModifiedDate;

	private LocalDate testLocalDate;
	private LocalDateTime testLocalDateTime;

	@Lob
	private String description;
}
