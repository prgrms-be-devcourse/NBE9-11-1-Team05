package com.back.orderplz_01.search.ownersearch.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.back.orderplz_01.search.ownersearch.dto.OwnerOrderSummaryDto;
import com.back.orderplz_01.search.ownersearch.repository.OwnerOrderSearchRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OwnerOrderSearchService {

	private final OwnerOrderSearchRepository ownerOrderSearchRepository;

	@Transactional(readOnly = true)
	public Page<OwnerOrderSummaryDto> findAllOrderLinesForOwner(Pageable pageable) {
		return ownerOrderSearchRepository.findAllOrderByOrderedAtDesc(pageable)
			.map(OwnerOrderSummaryDto::from);
	}
}
