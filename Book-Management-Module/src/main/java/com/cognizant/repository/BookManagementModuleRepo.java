package com.cognizant.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cognizant.entity.Book;

@Repository
public interface BookManagementModuleRepo extends JpaRepository<Book, Long> {
	Optional<Book> findByIsbn(String isbn);
	List<Book> findByAvailableCopiesGreaterThan(Integer copies);
	List<Book> findByTitleContainingIgnoreCase(String title);
	List<Book> findByAuthorContainingIgnoreCase(String author);
	List<Book> findByGenreContainingIgnoreCase(String genre);
	List<Book> findByYearPublished(Integer yearPublished);
	
	@Query(value = "SELECT * FROM books WHERE year_published BETWEEN :startYear AND :endYear", 
			nativeQuery = true)
	List<Book> searchBookByYearRange(@Param("startYear") Integer startYear,@Param("endYear") Integer endYear);
}
