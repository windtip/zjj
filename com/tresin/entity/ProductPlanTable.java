package com.tresin.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Transient;
import java.io.Serializable;

/**
 * Created by Admin on 2018/7/16.
 */
@Getter
@Setter
@ToString(exclude = {"id"})
public class ProductPlanTable implements Serializable{
	private Long id;
    private String companyCode;     //公司代码
    private String taskCode;        //任务编号
    private String startDate;       //开始时间（日期）
    private String endDate;         //结束时间（日期）
    private String onturn;          //班次
    private String status;          //完成情况

    private String projNo;          //工程号
    private String shipNo;          //单船号
}
