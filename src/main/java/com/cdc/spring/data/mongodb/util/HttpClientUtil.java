package com.cdc.spring.data.mongodb.util;

import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import org.springframework.stereotype.Component;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;


import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.lang.invoke.MethodHandles;


@Component
public class HttpClientUtil {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    final HttpClient httpClient;

    public HttpClientUtil(HttpClient httpClient) {

        this.httpClient = httpClient;
    }

    public String send(String URL, String requestBody) {

        String responseBody = null;
        try {
            logger.info("RSU: " + httpClient);
            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(URI.create(URL))
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .build();

            logger.info("RSU : " + httpClient);
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            logger.info("RSU  : ");
            logger.info(response.body());
            responseBody = response.body();
        } catch (Exception e) {
            if (e != null ) {
                logger.info( "RSU   : ");
                throw new RuntimeException(e.getMessage());
            }
        }

        return responseBody;
    }
}
