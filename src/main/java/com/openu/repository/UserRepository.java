package com.openu.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.openu.model.User;

public interface UserRepository extends PagingAndSortingRepository<User, Long> {

    User findByUsername(String username);

}
