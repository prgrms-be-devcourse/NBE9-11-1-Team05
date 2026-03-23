package com.back.orderplz_01.search.ownersearch.repository;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.back.orderplz_01.orders.entity.Orders;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

@Repository
public class OwnerOrderSearchRepository {

	@PersistenceContext
	private EntityManager em;

	public Page<Orders> findAllOrderByOrderedAtDesc(Pageable pageable) {
		long total = em.createQuery("SELECT COUNT(o) FROM Orders o", Long.class).getSingleResult();

		TypedQuery<Integer> idQuery = em.createQuery(
			"SELECT o.id FROM Orders o ORDER BY o.orderedAt DESC",
			Integer.class
		);
		idQuery.setFirstResult((int) pageable.getOffset());
		idQuery.setMaxResults(pageable.getPageSize());
		List<Integer> ids = idQuery.getResultList();

		if (ids.isEmpty()) {
			return new PageImpl<>(List.of(), pageable, total);
		}

		List<Orders> rows = em.createQuery(
			"SELECT DISTINCT o FROM Orders o JOIN FETCH o.coffee WHERE o.id IN :ids",
			Orders.class
		).setParameter("ids", ids).getResultList();

		Map<Integer, Orders> byId = rows.stream().collect(Collectors.toMap(Orders::getId, o -> o, (a, b) -> a));
		List<Orders> ordered = ids.stream().map(byId::get).filter(Objects::nonNull).toList();

		return new PageImpl<>(ordered, pageable, total);
	}
}
