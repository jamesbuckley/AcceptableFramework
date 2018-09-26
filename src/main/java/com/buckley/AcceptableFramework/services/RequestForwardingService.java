package com.buckley.AcceptableFramework.services;

import com.buckley.AcceptableFramework.utils.RequestResponseWriter;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import org.springframework.core.env.Environment;
import java.util.*;

import static java.nio.charset.StandardCharsets.UTF_8;

@Service
public class RequestForwardingService {

    private static final Logger logger = LoggerFactory.getLogger(RequestForwardingService.class);
    private HttpClient client = HttpClientBuilder.create().build();
    private Environment environment;

    @Autowired
    public void setEnvironment(Environment environment){
        this.environment = environment;
    }

    public String forwardRequest(HttpServletRequest request, String requestType) throws IOException, URISyntaxException {

        String originalUrl = environment.getProperty("acceptable.original.server.url");
        String originalPort = environment.getProperty("acceptable.original.server.port");
        String origin = originalUrl + originalPort;
        String queryString = request.getQueryString();
        String urlString = (queryString != null && queryString.length() == 0) ? origin + request.getRequestURI() : origin + request.getRequestURI() + "?" + queryString;

        if(requestType.equals(HttpGet.METHOD_NAME)){

            HttpGet getRequest = new HttpGet(urlString);
            setHeaders(getRequest, request);
            HttpResponse response = client.execute(getRequest);
            return processGetResponse(getRequest, response, queryString);
        }else if(requestType.equals(HttpPost.METHOD_NAME)){
            HttpPost postRequest = new HttpPost(origin + request.getRequestURI());
            setHeaders(postRequest, request);
            List<NameValuePair> params = URLEncodedUtils.parse(new URI(urlString), UTF_8);
            postRequest.setEntity(new UrlEncodedFormEntity(params));
            HttpResponse response = client.execute(postRequest);
            return processPostResponse(postRequest, response,  queryString);
        }
        return null;
    }

    private String returnResponseBody(HttpResponse response) throws IOException {
        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        return String.valueOf(result);
    }

    private String processGetResponse(HttpRequestBase request, HttpResponse originalResponse, String queryString) throws IOException {
        String responseBody = returnResponseBody(originalResponse);
        HashMap<String, Object> requestResponseMap = processResponse(request, responseBody, queryString);
        RequestResponseWriter.addToGetRequestResponseMaps(requestResponseMap);
        return responseBody;
    }

    private String processPostResponse(HttpRequestBase request, HttpResponse originalResponse, String queryString) throws IOException {
        String responseBody = returnResponseBody(originalResponse);
        HashMap<String, Object> requestResponseMap = processResponse(request, responseBody, queryString);
        RequestResponseWriter.addToPostRequestResponseMaps(requestResponseMap);
        return responseBody;
    }

    private HashMap<String, Object> processResponse(HttpRequestBase request, String responseBody, String queryString) throws IOException {
        HashMap<String, Object> requestResponseMap = new HashMap<>();
        requestResponseMap.put("request", request);
        requestResponseMap.put("response", responseBody);
        if(queryString != null){
            requestResponseMap.put("query", RequestResponseWriter.formatQueryString(queryString));
        }else{
            requestResponseMap.put("query", null);
        }
        return requestResponseMap;
    }

    private void setHeaders(HttpRequestBase getRequest, HttpServletRequest request){

        Enumeration names = request.getHeaderNames();
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            Enumeration values = request.getHeaders(name); // support multiple values
            if (values != null && !name.equalsIgnoreCase("Content-Length")) {
                while (values.hasMoreElements()) {
                    getRequest.setHeader(name, values.nextElement().toString());
                }
            }
        }
    }
}
