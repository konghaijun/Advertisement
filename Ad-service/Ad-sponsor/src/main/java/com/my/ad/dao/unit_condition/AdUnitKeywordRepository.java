package com.my.ad.dao.unit_condition;

import com.my.ad.entity.unit_condition.AdUnitKeyword;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AdUnitKeywordRepository extends
        JpaRepository<AdUnitKeyword, Long> {
}
