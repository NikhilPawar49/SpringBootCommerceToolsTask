package com.example.inventory_service.repository;

import com.example.inventory_service.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, String> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT i FROM Inventory i WHERE i.skuId = :skuId")
    Optional<Inventory> findByIdWithLock(@Param("skuId") String skuId);

    @Query("SELECT i FROM Inventory i WHERE i.skuId IN :skuIds")
    List<Inventory> findAllBySkuIdIn(@Param("skuIds") List<String> skuIds);
}