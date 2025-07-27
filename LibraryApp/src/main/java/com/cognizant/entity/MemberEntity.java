package com.cognizant.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "members")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long memberId;

	private String name;
	private String email;
	private String phone;
	private String address;
	private String membershipStatus;

	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<BorrowingTransaction> transactions;
	
	public MemberEntity(Long memberId, String name, String email, String phone, String address, String membershipStatus) {
	    this.memberId = memberId;
	    this.name = name;
	    this.email = email;
	    this.phone = phone;
	    this.address = address;
	    this.membershipStatus = membershipStatus;
	}
}
