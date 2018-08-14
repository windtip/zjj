package com.tresin.aop;

import com.tresin.dao.SqlServerDao;
import com.tresin.service.MultipleDataSource;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * Created by Admin on 2018/7/16.
 */
@Component
@Aspect
public class MultipleDSAOP {

    @Around("execution(* com.tresin.dao.*.*(..)) || execution(* fv.dao.*.*(..))")
    public Object doAround(ProceedingJoinPoint jp) throws Throwable {
        if (jp.getTarget() instanceof SqlServerDao) {
            MultipleDataSource.setDataSourceKey("sqlServerDS");
        } else {
            MultipleDataSource.setDataSourceKey("dataSource");
        }
        return jp.proceed();
    }
}
