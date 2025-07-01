package com.farmer.Form.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)  // ✅ Ignore unknown fields like "Circle", etc.
public class PincodeApiResponse {

    @JsonProperty("Status")
    private String status;

    @JsonProperty("Message")
    private String message;

    @JsonProperty("PostOffice")
    private List<PostOffice> postOffice;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PostOffice {

        @JsonProperty("Name")
        private String name;

        @JsonProperty("District")
        private String district;

        @JsonProperty("State")
        private String state;

        @JsonProperty("Block")
        private String block;

        @JsonProperty("Country")
        private String country;

        @JsonProperty("Division")
        private String division;
    }
}
