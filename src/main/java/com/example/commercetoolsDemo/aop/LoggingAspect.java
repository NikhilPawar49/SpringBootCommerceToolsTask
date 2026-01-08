package com.example.commercetoolsDemo.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Around("execution(* com.example.commercetoolsDemo.service..*(..))")
    public Object logServiceMethods(ProceedingJoinPoint joinPoint) throws Throwable {

        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        long startTime = System.currentTimeMillis();

        // 🔹 START log
        log.info("START  {}.{} | args={}", className, methodName, Arrays.toString(args));

        try {
            Object result = joinPoint.proceed(); // calls actual method

            long timeTaken = System.currentTimeMillis() - startTime;

            // 🔹 END log
            log.info("END    {}.{} | result={} | time={}ms",
                    className, methodName, result, timeTaken);

            return result;

        } catch (Exception ex) {
            log.error("ERROR  {}.{} | message={}",
                    className, methodName, ex.getMessage(), ex);
            throw ex;
        }
    }
}
