package com.intask.Task.Repository;

import com.intask.Task.Entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PurchaseRepo extends JpaRepository<Purchase, Long> {
    List<Purchase> findByUserId(Long userId);
}
