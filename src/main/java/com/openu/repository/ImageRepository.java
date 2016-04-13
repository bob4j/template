package com.openu.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.openu.model.Image;

public interface ImageRepository extends PagingAndSortingRepository<Image, Long> {

}
