package com.farmer.Form.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import com.farmer.Form.Entity.Farmer;

public interface FarmerRepository extends JpaRepository<Farmer, Long> { }
