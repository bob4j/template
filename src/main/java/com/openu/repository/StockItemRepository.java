package com.openu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.openu.model.Product;
import com.openu.model.ProductColor;
import com.openu.model.ProductSize;
import com.openu.model.StockItem;

public interface StockItemRepository extends PagingAndSortingRepository<StockItem, Long> {

    @Query("select distinct color from StockItem where product = :product")
    List<ProductColor> findDistinctColorByProduct(@Param("product") Product product);

    @Query("select distinct size from StockItem where product = :product and color = :color")
    List<ProductSize> findDistinctSizeByProductAndColor(@Param("product") Product product, @Param("color") ProductColor color);
    @Query("from StockItem where product = :product and color = :color and size = :size group by id")
    StockItem findStockItemByFields(@Param("product") Product product, @Param("color") ProductColor color,  @Param("size") ProductSize size );

}
