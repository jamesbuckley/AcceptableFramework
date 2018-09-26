package com.buckley.AcceptableFramework.controllers;

import com.buckley.AcceptableFramework.models.RequestModel;
import com.buckley.AcceptableFramework.utils.RequestResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("**")
@ConditionalOnProperty(prefix = "acceptable.mode", name = "mock", matchIfMissing = true)
public class MockServerController {

    private static final Logger logger = LoggerFactory.getLogger(MockServerController.class);

    private RequestModel requests;

    @Autowired
    public void setRequests(RequestModel requests) {
        this.requests = requests;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String getMethod(HttpServletRequest httpRequest) {

        RequestModel.Request duplicateRequest = findURIMapEntry(httpRequest, RequestMethod.GET);
        if(duplicateRequest != null){
            return duplicateRequest.getResponse();
        }else{
            throw new RuntimeException("Non mocked Request");
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public String postMethod(HttpServletRequest httpRequest) {

        RequestModel.Request duplicateRequest = findURIMapEntry(httpRequest, RequestMethod.POST);
        if(duplicateRequest != null){
            return duplicateRequest.getResponse();
        }else{
            throw new RuntimeException("Non mocked Request");
        }
    }


    private RequestModel.Request findURIMapEntry(HttpServletRequest httpRequest, RequestMethod methodType) {
        List<RequestModel.Request> requestList = null;
        if(methodType.equals(RequestMethod.GET)){
            requestList = requests.getGetRequests();
        }else if(methodType.equals(RequestMethod.POST)){
            requestList = requests.getPostRequests();
        }
        if(requestList != null){
            for (RequestModel.Request requestCandidate : requestList) {
                if (httpRequest.getRequestURI().equals(requestCandidate.getUrl())) {
                    String orginalQuery = httpRequest.getQueryString();
                    if(orginalQuery == null) {
                        return requestCandidate;
                    }else{
                        String formattedQuery = RequestResponseWriter.formatQueryString(orginalQuery);
                        if(formattedQuery.equalsIgnoreCase(requestCandidate.getQuery())){
                            return requestCandidate;
                        }
                    }
                }
            }
        }
        return null;
    }

}