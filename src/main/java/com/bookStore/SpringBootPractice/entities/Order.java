package com.bookStore.SpringBootPractice.entities;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "Status cannot be null")
    @Size(min = 3, max = 50, message = "Status must be between 3 and 50 characters")
    private String status;

    @NotNull(message = "Order date cannot be null")
    private LocalDate orderDate = LocalDate.now();

    @NotNull(message = "Update date cannot be null")
    private LocalDate updateDate = LocalDate.now();

    @NotNull(message = "Payment method cannot be null")
    @Size(min = 3, max = 50, message = "Payment method must be between 3 and 50 characters")
    private String paymentMethod;

    @NotNull(message = "Total amount cannot be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Total amount must be greater than zero")
    private Double totalAmount;

    @NotNull(message = "User cannot be null")
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private User user;

    @NotNull(message = "Shipping address cannot be null")
    @Size(min = 10, max = 255, message = "Shipping address must be between 10 and 255 characters")
    private String shippingAddress;

    @NotEmpty(message = "Order must have at least one order detail")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "order")
    private List<OrderDetail> orderDetail = new ArrayList<>();

    @Size(max = 500, message = "Comment must be less than 500 characters")
    private String comment;
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

   public LocalDate getOrderDate() {
	   return orderDate;
   }
    public LocalDate getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDate updateDate) {
        this.updateDate = updateDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }


    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<OrderDetail> getOrderDetail() {
		return orderDetail;
	}

	public void setOrderDetail(List<OrderDetail> orderDetail) {
		this.orderDetail = orderDetail;
	}

	public void setOrderDate(LocalDate orderDate) {
		this.orderDate = orderDate;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
    
}

