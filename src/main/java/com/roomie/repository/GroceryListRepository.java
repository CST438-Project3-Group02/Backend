package com.roomie.repository;

import com.roomie.entity.GroceryList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroceryListRepository
    extends JpaRepository<GroceryList, Long> {}
