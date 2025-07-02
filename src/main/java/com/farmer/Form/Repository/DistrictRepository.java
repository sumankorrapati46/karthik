package com.farmer.Form.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.farmer.Form.Entity.District;
import com.farmer.Form.Entity.State;

import java.util.List;
 
@Repository
public interface DistrictRepository extends JpaRepository<District, Long> {
 
    // Custom method to get all districts by selected state
    List<District> findByState(State state);
}