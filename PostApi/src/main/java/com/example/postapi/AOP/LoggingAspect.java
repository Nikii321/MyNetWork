package com.example.postapi.AOP;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Service;

@Service
@Aspect
@Slf4j
public class LoggingAspect {

    @Pointcut("execution(* com.example.postapi.service.*.*(..))")
    private void ServiceMethods(){}
    @Pointcut("execution(* com.example.postapi.handlers.*.*(..))")
    private void ControllerMethods(){}
    @Pointcut("ServiceMethods() || ControllerMethods()")
    private void allMethod(){}




    @Before("allMethod())")
    public void loggingBefore(JoinPoint joinPoint){
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        log.info("Started");
        log.info("Class = {}, \n Method = {},\n Parameters = {}", methodSignature, methodSignature.getMethod(),joinPoint.getArgs());
    }

    @AfterReturning(pointcut = "allMethod()", returning = "result")
    public void afterReturning(Object result){
        log.info("result = {}",result);
        log.info("Finished");
    }
    @AfterThrowing(pointcut = "allMethod()" , throwing = "exeption")
    public void afterThrowing(Throwable exeption){
        log.error("exeption: {}",exeption);
        log.info("Error");
    }
}
