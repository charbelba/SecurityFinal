package com.example.SecurityFinal.Api.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Entity
@Table(name = "product_purchase_order")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ProductPurchaseOrder extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;


    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonIgnore
    @ToString.Exclude
    private Product product;

    @Positive(message = "Quantity must be positive")
    private long quantity;





    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductPurchaseOrder)) return false;
        ProductPurchaseOrder supplier = (ProductPurchaseOrder) o;
        return id == supplier.id;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }
}
