package com.tresin.controller;

import com.alibaba.fastjson.JSONObject;
import com.tresin.entity.ProductPlanTable;
import com.tresin.service.ProductPlanService;
import fv.pojo.ResultModel;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Admin on 2018/7/17.
 */
@RestController
@Log
public class ProductPlanTableController {

    @Autowired
    ProductPlanService productPlanService;

    @RequestMapping(value = "/getProductPlanTable", produces = "application/json; charset=utf-8")
    public JSONObject getProductPlanTable(){
        ResultModel resultModel = null;
        try {
            List<ProductPlanTable> items = productPlanService.getProducts();
            if(items == null || items.isEmpty()){
                log.info("[getProductPlanTable] return no result ");
                resultModel = new ResultModel(ResultModel.StatusEnum.NO_RESULT.getValue(),
                        ResultModel.StatusEnum.NO_RESULT.getMessage(), null);
                return (JSONObject)JSONObject.toJSON(resultModel);
            }
            log.info("[getProductPlanTable] result size: " + items.size());
            resultModel = new ResultModel(ResultModel.StatusEnum.SUCESS.getValue(),
                    ResultModel.StatusEnum.SUCESS.getMessage(), JSONObject.toJSON(items));
            return (JSONObject)JSONObject.toJSON(resultModel);
        }catch (Exception e){
            log.info("[getProductPlanTable] error:\n" + e.getMessage());
            resultModel = new ResultModel(ResultModel.StatusEnum.UNKNOWN_EXCEPTION.getValue(),
                    ResultModel.StatusEnum.UNKNOWN_EXCEPTION.getMessage(), null);
            return (JSONObject)JSONObject.toJSON(resultModel);
        }
    }
}
