package com.bookStore.SpringBootPractice.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bookStore.SpringBootPractice.entities.Author;
import com.bookStore.SpringBootPractice.entities.Book;
@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer>{

	Page<Book> findByName(String authorName, Pageable p);



    @Query("SELECT a FROM Author a WHERE LOWER(a.name) LIKE %:keyword%")
    List<Author> searchAuthor(@Param("keyword") String keyword);

}
