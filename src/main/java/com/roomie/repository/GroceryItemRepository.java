package com.roomie.repository;

import com.roomie.entity.GroceryItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroceryItemRepository
    extends JpaRepository<GroceryItem, Long>
{
    List<GroceryItem> findByGroceryList_GroceryListId(Long groceryListId);
    List<GroceryItem> findByProfile_ProfileId(Long profileId);
    List<GroceryItem> findByIsPurchased(boolean isPurchased);
}
