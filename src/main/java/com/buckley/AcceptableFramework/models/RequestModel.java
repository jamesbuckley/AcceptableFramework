package com.buckley.AcceptableFramework.models;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RequestModel {

    private String error;
    private List<Request> getRequests = new ArrayList<>();
    private List<Request> postRequests = new ArrayList<>();

    public static class Request {
        private String url;
        private String query;
        private String response;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getQuery() {
            return query;
        }

        public void setQuery(String query) {
            this.query = query;
        }

        public String getResponse() {
            return response;
        }

        public void setResponse(String response) {
            this.response = response;
        }
    }

    public void createGetRequest(String query, String url, String response){
        getRequests.add(createRequest(query, url, response));
    }

    public void createPostRequest(String query, String url, String response){
        postRequests.add(createRequest(query, url, response));
    }

    public List<Request> getGetRequests() {
        return getRequests;
    }

    public void setGetRequests(List<Request> getRequests) {
        this.getRequests = getRequests;
    }

    public List<Request> getPostRequests() {
        return postRequests;
    }

    public void setPostRequests(List<Request> postRequests) {
        this.postRequests = postRequests;
    }

    private Request createRequest(String query, String url, String response){
        Request request = new Request();
        request.setQuery(query);
        request.setUrl(url);
        request.setResponse(response);
        return request;
    }
}
