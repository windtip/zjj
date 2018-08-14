package com.tresin.dao;

import com.tresin.entity.ProductPlanTable;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Admin on 2018/7/16.
 */
@Repository
public interface ProductPlanDao extends SqlServerDao{

    List<ProductPlanTable> getProductPlanTables();
}
