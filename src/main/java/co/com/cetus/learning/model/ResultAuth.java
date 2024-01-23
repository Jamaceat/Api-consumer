package co.com.cetus.learning.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResultAuth {

    @JsonProperty("accessToken")
    private String accessToken;

    public ResultAuth(String accessToken) {
        this.accessToken = accessToken;
    }

    public ResultAuth() {
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

}
