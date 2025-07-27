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
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "books")
public class Book {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long bookId;

	private String title;
	private String author;
	private String genre;
	private String isbn;
	private int yearPublished;
	private int availableCopies;

	@OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<BorrowingTransaction> transactions;
	
	public Book(Long bookId, String title, String author, String genre, String isbn, int yearPublished, int availableCopies) {
	    this.bookId = bookId;
	    this.title = title;
	    this.author = author;
	    this.genre = genre;
	    this.isbn = isbn;
	    this.yearPublished = yearPublished;
	    this.availableCopies = availableCopies;
	}

}
