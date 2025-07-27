package com.cognizant.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "books")
public class Book {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long bookId;
	@Column(nullable = false)
	private String title;
	@Column(nullable = false)
	private String author;
	private String genre;
	@Column(unique = true)
	private String isbn;
	private Integer yearPublished;
	@Column(nullable = false)
	private Integer availableCopies;
}
