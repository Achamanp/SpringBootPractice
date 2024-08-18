package com.bookStore.SpringBootPractice.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
public class Review {

	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Integer id;

	    @NotBlank(message = "Content cannot be blank")
	    @Size(max = 1000, message = "Content must be less than 1000 characters")
	    @Column(nullable = false)
	    private String content;

	    @NotNull(message = "Rating cannot be null")
	    @Min(value = 1, message = "Rating must be at least 1")
	    @Max(value = 5, message = "Rating must be at most 5")
	    @Column(nullable = false)
	    private Integer rating;

	    @NotNull(message = "CreatedAt cannot be null")
	    @PastOrPresent(message = "CreatedAt cannot be in the future")
	    @Column(nullable = false)
	    private LocalDateTime createdAt;

	    @NotNull(message = "Book cannot be null")
	    @ManyToOne
	    @JoinColumn(name = "book_id", nullable = false)
	    private Book book;

	    @NotNull(message = "User cannot be null")
	    @ManyToOne
	    @JoinColumn(name = "user_id", nullable = false)
	    private User user;

  
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
    
}
