package com.farmer.Form.Repository;


import com.farmer.Form.Entity.Farmer;
import org.springframework.data.jpa.repository.JpaRepository;
 
public interface FarmerRepository extends JpaRepository<Farmer, Long> {
}