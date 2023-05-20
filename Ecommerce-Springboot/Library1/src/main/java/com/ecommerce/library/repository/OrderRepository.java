package com.ecommerce.library.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ecommerce.library.model.Order;


@Repository
public interface OrderRepository extends JpaRepository<Order, Long>{
	@Query("select o from Order o order by o.id desc")
	List<Order> findAllOrder();
	
	@Query("select o from Order o order by o.id desc")
	Page<Order> pageOrder(Pageable pageable);
	
}
