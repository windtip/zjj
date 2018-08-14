package com.tresin.service;

import com.alibaba.fastjson.JSONObject;
import com.tresin.dao.ProductPlanDao;
import com.tresin.entity.ProductPlanTable;
import fv.Utils.RestTemplateUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Admin on 2018/7/13.
 */

@Service
public class ProductPlanService {
    private final static String DEFAULT_SHIP_NO = "H2618";
    private final static List<String> SHIP_NO_PREFIX = Arrays.asList("A","C","H");
    private final static String URL = "/JNIII_WebService/getProjectNo/data=";

    @Autowired
    ProductPlanDao productPlanDao;

    @Autowired
    RestTemplateUtil restTemplate;

    @Value("${webservice.address}")
    String wsAddress;

    public List<ProductPlanTable> getProducts(){
        return process(productPlanDao.getProductPlanTables());
    }

    private List<ProductPlanTable> process(List<ProductPlanTable> items){
        if(items == null || items.isEmpty()) return items;
        for(ProductPlanTable item : items){
            String taskCode = item.getTaskCode();
            if(StringUtils.isBlank(taskCode)) continue;
            String projNo, shipNo;
            shipNo = taskCode.split("-")[0];
            if(StringUtils.isBlank(shipNo)) continue;
            if (!SHIP_NO_PREFIX.contains(String.valueOf(shipNo.charAt(0)).toUpperCase())) {
                shipNo = DEFAULT_SHIP_NO;
            }
            item.setShipNo(shipNo);
            //get projNo from TC via shipNo
            projNo = restTemplate.sendPost(new JSONObject(), wsAddress + URL + shipNo);
            if(StringUtils.isNotBlank(projNo)){
                projNo = projNo.replaceAll("\"","");
            }
            item.setProjNo(projNo);

            String status = item.getStatus();
            if(StringUtils.isBlank(status)) continue;
            status = "1".equals(status) ? "已完成":"未完成";
            item.setStatus(status);
        }
        return items;
    }
}
