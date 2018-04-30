package com.buckley.AcceptableFramework.interceptors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ConditionalOnProperty(prefix = "acceptable.mode", name = "record")
public class RecorderInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(RecorderInterceptor.class);


    @Override
    @RequestMapping(method = RequestMethod.POST)
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        return true;
    }
}
