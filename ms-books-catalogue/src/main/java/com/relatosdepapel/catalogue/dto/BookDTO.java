package com.relatosdepapel.catalogue.dto;

<<<<<<< HEAD
=======
import java.math.BigDecimal;
>>>>>>> 6baa5623e840a7fe5089992f8bef7184d6b8b5e5
import java.time.LocalDate;

public class BookDTO {

    private Long id;
    private String title;
    private String author;
    private String isbn;
<<<<<<< HEAD
=======
    private BigDecimal price;
    private Integer stock;
>>>>>>> 6baa5623e840a7fe5089992f8bef7184d6b8b5e5
    private String category;
    private LocalDate publicationDate;
    private Integer rating;
    private Boolean visible;
<<<<<<< HEAD
=======
    private String coverImageUrl;
>>>>>>> 6baa5623e840a7fe5089992f8bef7184d6b8b5e5

    public BookDTO() {
        // Constructor por JPA
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

<<<<<<< HEAD
=======
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

>>>>>>> 6baa5623e840a7fe5089992f8bef7184d6b8b5e5
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }
<<<<<<< HEAD
=======

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }
>>>>>>> 6baa5623e840a7fe5089992f8bef7184d6b8b5e5
}
