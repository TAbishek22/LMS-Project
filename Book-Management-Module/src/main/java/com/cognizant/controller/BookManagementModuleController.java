package com.cognizant.controller;
 
import java.util.List;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
 
import com.cognizant.dto.BookDTO;
import com.cognizant.service.BookManagementModuleService;
 
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/books")
public class BookManagementModuleController {
    @Autowired
    private BookManagementModuleService bookManagementModuleService;
 
// 		----------------------------GET MAPPING-----------------------------
    @GetMapping("/allBooks")
//    @GetMapping
    public ResponseEntity<List<BookDTO>> getAllBooks(){
    	List<BookDTO> books = bookManagementModuleService.getAllBooks();
    	return new ResponseEntity<>(books, HttpStatus.OK);
//    	return ResponseEntity.ok(books);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBookById(@Valid @PathVariable Long id){
    	BookDTO book = bookManagementModuleService.getBookById(id);
    	return new ResponseEntity<>(book, HttpStatus.OK);
    }
    
    @GetMapping("/search/title/{title}")
    public ResponseEntity<List<BookDTO>> searchBookByTitle(@Valid @PathVariable String title) {
        List<BookDTO> books = bookManagementModuleService.searchBookByTitle(title);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }
 
    @GetMapping("/search/author/{author}")
    public ResponseEntity<List<BookDTO>> searchBookByAuthor(@Valid @PathVariable String author) {
        List<BookDTO> books = bookManagementModuleService.searchBookByAuthor(author);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }
 
    @GetMapping("/search/genre/{genre}")
    public ResponseEntity<List<BookDTO>> searchBookByGenere(@Valid @PathVariable String genre) {
        List<BookDTO> books = bookManagementModuleService.searchBookByGenre(genre);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }
    
    @GetMapping("/search/isbn/{isbn}")
    public ResponseEntity<BookDTO> searchBookByIsbn(@Valid @PathVariable String isbn) {
        BookDTO books = bookManagementModuleService.searchBookByIsbn(isbn);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }
    
    @GetMapping("/search/year/{year}")
    public ResponseEntity<List<BookDTO>> searchBookByIsbn(@Valid @PathVariable Integer year) {
        List<BookDTO> books = bookManagementModuleService.searchBookByYear(year);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }
    
    @GetMapping("/serach/year-range")
    public ResponseEntity<List<BookDTO>> searchBookByYearRange(
    		@RequestParam Integer startYear,
    		@RequestParam Integer endYear){
    	List<BookDTO> books = bookManagementModuleService.searchBookByYearRange(startYear, endYear);
    	return new ResponseEntity<>(books, HttpStatus.OK);
    }
    
    
   
  //----------------------------POST MAPPING-----------------------------
    @PostMapping("/addBooks")
    public ResponseEntity<BookDTO> addBook(@Valid @RequestBody BookDTO bookDTO) {
        BookDTO newBook = bookManagementModuleService.addBook(bookDTO);
        return new ResponseEntity<>(newBook, HttpStatus.CREATED);
    }
    
    @PostMapping("/bulkUpload")
    public ResponseEntity<List<BookDTO>> uploadBooks(@Valid @RequestBody List<BookDTO> bookList){
    	List<BookDTO> books = bookManagementModuleService.uploadBooks(bookList);
    	return new ResponseEntity<>(books, HttpStatus.CREATED);
    }
    
    
  //----------------------------PUT -----------------------------
    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> updateBook(@Valid @PathVariable Long id,@Valid @RequestBody BookDTO bookDTO ){
    	BookDTO updatedBook = bookManagementModuleService.updateBook(id, bookDTO);
    	return new ResponseEntity<>(updatedBook, HttpStatus.OK);
    }

    
  //----------------------------DELETE -----------------------------
    @DeleteMapping("deleteAllBooks")
    public ResponseEntity<String> deleteAllBooks(){
    	bookManagementModuleService.deleteAllBook();
    	return new ResponseEntity<>("Deleted all Books", HttpStatus.OK);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookById(@Valid @PathVariable Long id){
    	bookManagementModuleService.deleteBookById(id);
    	return ResponseEntity.noContent().build();
    }
}
 
 