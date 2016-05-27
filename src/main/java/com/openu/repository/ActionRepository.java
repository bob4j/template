package com.openu.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.openu.model.Action;

public interface ActionRepository extends PagingAndSortingRepository<Action, Long> {

}
