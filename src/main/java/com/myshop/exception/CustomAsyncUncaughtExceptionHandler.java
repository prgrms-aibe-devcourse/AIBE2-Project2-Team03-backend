package com.myshop.exception;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

public class CustomAsyncUncaughtExceptionHandler implements AsyncUncaughtExceptionHandler {

    @Override
    public void handleUncaughtException(Throwable throwable, Method method, Object... obj) {
        System.err.println("Async method " + method.getName() + " threw an exception: " + throwable.getMessage());
    }
}
