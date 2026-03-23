package com.back.orderplz_01.search.customersearch.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.back.orderplz_01.orders.entity.Orders;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

@Repository
public class CustomerOrderSearchRepository {

	@PersistenceContext
	private EntityManager em;

	public List<Orders> findByEmailIgnoreCaseAndZipCodeIgnoreCaseOrderByOrderedAtDesc(String email, String zipCode) {
		TypedQuery<Orders> q = em.createQuery(
			"""
				SELECT o FROM Orders o JOIN FETCH o.coffee
				WHERE LOWER(o.email) = LOWER(:email) AND LOWER(o.zipCode) = LOWER(:zipCode)
				ORDER BY o.orderedAt DESC
				""",
			Orders.class
		);
		q.setParameter("email", email);
		q.setParameter("zipCode", zipCode);
		return q.getResultList();
	}

	public Optional<Orders> findByIdWithCoffee(Integer id) {
		List<Orders> list = em.createQuery(
			"SELECT o FROM Orders o JOIN FETCH o.coffee WHERE o.id = :id",
			Orders.class
		).setParameter("id", id).setMaxResults(1).getResultList();
		return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
	}
}
