package com.kala.model;

public class AssitResponse {
    
    private Boolean isQuestion;
    private String query;
    private String response;

    public void setIsQuestion(Boolean isQuestion) {
        this.isQuestion = isQuestion;
    }
    
    public Boolean getIsQuestion() {
        return isQuestion;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }

    public void setResponse(String response) {
        this.response = response;
    }
    
    public String getResponse() {
        return response;
    }
}
