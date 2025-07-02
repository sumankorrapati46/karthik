package com.farmer.Form.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.farmer.Form.Entity.Country;
import com.farmer.Form.Entity.State;

import java.util.List;
 
@Repository
public interface StateRepository extends JpaRepository<State, Long> {
 
    // Custom method to get all states by selected country
    List<State> findByCountry(Country country);
}
 
