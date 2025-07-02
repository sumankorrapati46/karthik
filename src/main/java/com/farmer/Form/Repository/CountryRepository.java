package com.farmer.Form.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.farmer.Form.Entity.Country;
 
@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
    // Add custom methods if needed later
}
