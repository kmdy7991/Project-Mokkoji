package com.ssafy.mokkoji.common.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Aspect
@Slf4j
@Component
public class LogAspect {
    /*
    * 관점 지향 - AOP
    * 핵심 기능과 공통 기능의 분리
    *
    *
    * pointcut - 분리될 공통 기능의 지점을 설정한다
    *
    * pointcut 실행 시점 작성예정
    *
    * */

    @Pointcut("execution(* com.ssafy.mokkoji.common.chat.controller..*.*(..))")
    public void chatBeforeExecute() {}

    @Pointcut("execution(* com.ssafy.mokkoji.member.*.controller..*.*(..))")
    public void memberBeforeExecute() {}

    @Pointcut("execution(* com.ssafy.mokkoji.game.*.controller..*.*(..))")
    public void gameBeforeExecute() {}

    // 실행되기전 동작
    @Before("chatBeforeExecute() || memberBeforeExecute() || gameBeforeExecute()")
    public void requestLogging(JoinPoint joinPoint) {

        // PRINT - method name, time
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        log.info(method.getName() + "() METHOD START");

        log.info("START TIME = {}", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        // 매개변수 배열을 읽음
        Object[] paramArgs = joinPoint.getArgs();

        // 매개변수 배열의 종류와 값을 출력
        for (Object object : paramArgs) {
            if (Objects.nonNull(object)) {
                log.info("PARAMETER TYPE => {}", object.getClass().getSimpleName());
                log.info("PARAMETER VALUE => {}", object);
            }
        }
    }

    // 실행 종료 시점에서 동작
    @AfterReturning("chatBeforeExecute() || memberBeforeExecute() || gameBeforeExecute()")
    public void afterRequesting(JoinPoint joinPoint) {
        // GET method name
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();

        log.info("END TIME = {}", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        log.info("END METHOD => {}", method.getName());
    }

    // 예외 발생 이후 시점에 동작
    @AfterThrowing(pointcut = "chatBeforeExecute() || memberBeforeExecute() || gameBeforeExecute()", throwing = "exception")
    public void exceptionLogging(JoinPoint joinPoint, Exception exception) {

        // PRINT method name, time
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();

        log.error(method.getName() + "() EXCEPTION!!!!!");

        log.error("EXCEPT TIME = {}", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }
}