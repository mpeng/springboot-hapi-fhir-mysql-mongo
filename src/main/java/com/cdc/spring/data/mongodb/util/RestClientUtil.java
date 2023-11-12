package com.cdc.spring.data.mongodb.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.lang.invoke.MethodHandles;


@Component
public class RestClientUtil {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    final RestTemplate restTemplate;

    public RestClientUtil(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public <T, R> T getHttpResponse(String url, Class<T> resultClass, HttpMethod method, R request) {
        T response = null;
        ResponseEntity<T> responseEntity = null;

        try {

            switch (method.name()) {
                case "GET":
                    responseEntity = restTemplate.getForEntity(url, resultClass);
                    break;
                case "POST":
                    responseEntity = restTemplate.postForEntity(url, request, resultClass);
                    break;
                case "PUT":
                    HttpEntity<R> entity = new HttpEntity<R>(request);
                    responseEntity = restTemplate.exchange(url, HttpMethod.PUT, entity, resultClass);
                    break;
                default:
                    logger.error("AUDIT Not supported method: {}", method);
                    break;
            }
            if (responseEntity != null && responseEntity.getStatusCode() != null && responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                response = responseEntity.getBody();
            } else {
                logger.error("AUDIT Error while getting response for the url {} {} and response class {}", method, url,
                        resultClass);
                throw new RuntimeException(responseEntity != null ? responseEntity.getBody().toString() :
                        "responseEntity is null");
            }
        } catch (Exception e) {
            if (e != null && e instanceof HttpClientErrorException && StringUtils.isNotBlank(((HttpClientErrorException) e).getResponseBodyAsString())) {
                throw new RuntimeException(((HttpClientErrorException) e).getResponseBodyAsString());
            }
            if (e != null && e instanceof HttpServerErrorException && StringUtils.isNotBlank(((HttpServerErrorException) e).getResponseBodyAsString())) {
                throw new RuntimeException(((HttpServerErrorException) e).getResponseBodyAsString());
            }
            logger.error(e.getMessage());
        }
        return response;
    }
}
