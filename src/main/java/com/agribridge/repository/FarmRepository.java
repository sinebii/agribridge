package com.agribridge.repository;

import com.agribridge.model.Farm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FarmRepository extends JpaRepository<Farm,Long> {
    @Query("SELECT f FROM Farm f WHERE f.isApproved = true")
    List<Farm> findApprovedFarms();

}
