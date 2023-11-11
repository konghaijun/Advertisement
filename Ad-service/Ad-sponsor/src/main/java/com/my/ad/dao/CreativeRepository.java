package com.my.ad.dao;

import com.my.ad.entity.Creative;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CreativeRepository extends JpaRepository<Creative, Long> {
}
