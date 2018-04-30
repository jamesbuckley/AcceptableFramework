package com.buckley.AcceptableFramework.controllers;

import com.buckley.AcceptableFramework.services.RequestForwardingService;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping("**")
@ConditionalOnProperty(prefix = "acceptable.mode", name = "record")
public class RecordingController {

    private static final Logger logger = LoggerFactory.getLogger(MockServerController.class);

    private RequestForwardingService requestForwardingService;

    @Autowired
    public void setRequestForwardingService(RequestForwardingService requestForwardingService) {
        this.requestForwardingService = requestForwardingService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String getMethod(HttpServletRequest httpRequest) {
        logger.info("Get Request recording: "+ httpRequest.getRequestURI());
        try {
             return requestForwardingService.forwardRequest(httpRequest, HttpGet.METHOD_NAME);
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public String postMethod(HttpServletRequest httpRequest) {
        logger.info("Post Request recording: "+ httpRequest.getRequestURI());
        try {
            return requestForwardingService.forwardRequest(httpRequest, HttpPost.METHOD_NAME);
        } catch (Exception e) {
            return e.getMessage();
        }
    }

}
