package com.buckley.AcceptableFramework.controllers;

import com.buckley.AcceptableFramework.models.RequestModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;

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

        RequestModel.Request duplicateRequest = findURIMapEntry(httpRequest);
        if(duplicateRequest != null){
            return duplicateRequest.getResponse();
        }else{
            throw new RuntimeException("Non mocked Request");
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public String postMethod(HttpServletRequest httpRequest) {

        RequestModel.Request duplicateRequest = findURIMapEntry(httpRequest);
        if(duplicateRequest != null){
            return duplicateRequest.getResponse();
        }else{
            throw new RuntimeException("Non mocked Request");
        }
    }


    private RequestModel.Request findURIMapEntry(HttpServletRequest httpRequest) {
        // TODO Need to handle request params, currently ignored
        for (RequestModel.Request requestCandidate : requests.getRequests()) {
            if (httpRequest.getRequestURI().equals(requestCandidate.getUrl())) {
                //if (httpRequest.getQueryString() == null || httpRequest.getQueryString().equals(requestCandidate.getQuery())) {
                    return requestCandidate;
                //}
            }
        }
        return null;
    }

}