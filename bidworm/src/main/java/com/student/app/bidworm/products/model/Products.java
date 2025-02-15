package com.student.app.bidworm.products.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Table(name = "product_listings")
public class Products {

    @Id
    @GeneratedValue(generator = "UUID")
    private String postid;


    @Column(name = "seller_id")
    private String sellerId;

    @Column(name = "title",nullable = false)
    private String title;

    private String description;

    private double price;
//    private String category;

    @Column(name = "photos")
    private String photos;

    @Column(name = "status")
    private String status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    

    public Products(){}



}
