package com.bookStore.SpringBootPractice.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bookStore.SpringBootPractice.entities.Author;
import com.bookStore.SpringBootPractice.entities.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer>{

	Page<Book> findByTittle(String title, Pageable p);

	Page<Book> findByIsbn(String isbn, Pageable p);

	Page<Book> findByCategory(String category, Pageable pageable);
	Page<Book> findByAuthors( Author author, Pageable pageable);

	//Book findByBookId(Integer bookId);

	

}
