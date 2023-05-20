package com.ecommerce.library.model;

import java.util.Collection;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "customers", uniqueConstraints = @UniqueConstraint(columnNames = {"username", "image", "phone_number"}))
public class Customer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "customer_id")
	private Long id;
	
	private String firstName;
	
	private String lastName;
	
	private String username;
	
	private String address;
	
	private String password;
	
	private String country;
	@Column(name = "phone_number")
	private String phoneNumber;
	@Lob
	@Column(columnDefinition = "MEDIUMBLOB")
	private String image;
	
	@Column( name = "city")
	private String city;
	
	@Column(name = "provider_id")
	private String providerId;
	
	@OneToOne(mappedBy = "customer")
	private ShoppingCart shoppingCart;
	
	@OneToMany(mappedBy = "customer")
	private List<Order> orders;
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
	@JoinTable(name = "customers_roles", 
	joinColumns = @JoinColumn(name = "customer_id", referencedColumnName = "customer_id")
	,inverseJoinColumns = @JoinColumn(name ="role_id",referencedColumnName = "role_id"))
	private  Collection<Role> roles;
}
