package org.example.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class LegoStore {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String name;

    @Column
    private Integer numberOfPieces;

    @Column
    private Integer price;

    @Column
    private Integer quantity;

    public LegoStore(Long id, String name, Integer numberOfPieces, Integer price, Integer quantity) {
        this.id = id;
        this.name = name;
        this.numberOfPieces = numberOfPieces;
        this.price = price;
        this.quantity=quantity;
    }

    public LegoStore() {

    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getNumberOfPieces() {
        return numberOfPieces;
    }

    public Integer getPrice() {
        return price;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setNumberOfPieces(Integer numberOfPieces) {
        this.numberOfPieces = numberOfPieces;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Lego {" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", no pieces=" + numberOfPieces+
                '}';
    }
}
