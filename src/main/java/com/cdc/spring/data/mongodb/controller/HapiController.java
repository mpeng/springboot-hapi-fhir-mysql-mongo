package com.cdc.spring.data.mongodb.controller;

import com.cdc.spring.data.mongodb.model.Document;
import com.cdc.spring.data.mongodb.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jaxrs.server.AbstractJaxRsResourceProvider;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;

import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.springframework.stereotype.Component;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.client.api.IHttpRequest;
import ca.uhn.fhir.rest.client.api.IHttpResponse;
import ca.uhn.fhir.rest.api.EncodingEnum;

import com.cdc.spring.data.mongodb.util.*;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import org.json.JSONObject;

@CrossOrigin
@RestController
@RequestMapping("/hapi")
public class HapiController {

  @Autowired
  private RestClientUtil restClientUtil;

  private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  private static final String BASE_URL = "http://hapi.fhir.org/baseR4";
  private static final String RAW_JSON = "?_format=json";
  private final FhirContext fhirContext = FhirContext.forR4();


  @GetMapping("/Patient/getDetail")
  public String getPatientDetail() {
    String jsonString = new JSONObject()
            .put("status", 500)
            .toString();

    System.out.println( "=================Patient getDetail=======================" );
    final String url = BASE_URL + "Patient/592824?_format=json";

    try {
      Patient patient = Optional.of(restClientUtil.getHttpResponse(url, Patient.class, HttpMethod.GET, null)).get();
      jsonString = new JSONObject()
              .put("status", 200)
              .toString();
    }
    catch (final RuntimeException ex){
      logger.error("Error in HapiController.getPatientDetail", ex);
      jsonString = new JSONObject()
              .put("status", 500)
              .toString();
      return jsonString;
    }
    return jsonString;
  }


  @GetMapping("/Patient/getAll")
  public String getAllPatient() {
    System.out.println( "=================Patient getAll=======================" );
    IGenericClient client = fhirContext.newRestfulGenericClient(BASE_URL);

    String jsonString = new JSONObject()
            .put("status", 200)
            .toString();
    try {
      IHttpRequest httpRequest = client.getHttpClient()
              .createGetRequest(fhirContext, EncodingEnum.JSON);
      IHttpResponse httpResponse = httpRequest.execute();

      System.out.println("IHttpResponse received " + httpResponse +
              ", " + httpResponse.getResponse() + ", " + httpResponse.getStatus());

      jsonString = new JSONObject()
              .put("status", httpResponse.getStatus())
              .toString();
    } catch ( IOException e ) {
      jsonString = new JSONObject()
              .put("status", 500)
              .toString();
    }
    return jsonString;
  }

  // http://localhost:8080/Patient/getWithID?id=002
  @RequestMapping(value="Patient/getWithID", method = RequestMethod.GET)
  public String getPatientDetailsWithID(@RequestParam("id") String id) {

    System.out.println( "=================Patient getWithID for " + id + "=======================" );
    IGenericClient client = fhirContext.newRestfulGenericClient(BASE_URL);
    Patient patient = client.read().resource(Patient.class).withId(id).execute();
    //String rtn = fhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(patient);
    //System.out.println("Patient get::rtn for id: " + id);
    //System.out.println(rtn);
    //System.out.println("--------------------");

    String json = fhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient);

    System.out.println("Patient get JSON::rtn for id: " + id);
    System.out.println(json);
    System.out.println("--------------------");

    try {
      ObjectMapper mapper = new ObjectMapper();

      Map<String, Object> map = mapper.readValue(json, Map.class);

      for (Map.Entry<String, Object> e : map.entrySet()) {
        System.out.println(e.getKey() + " => " + e.getValue());
      }
    } catch ( JsonProcessingException e ) {
      json = "{ \"status\": 500 }";
    }

    return json;
  }


  @GetMapping("/Patient/getEntity")
  public ResponseEntity<String>  getPatientEntity() {
    System.out.println( "=================Patient getEntity=======================" );
    String rtn = "";

    try {
      IGenericClient client = fhirContext.newRestfulGenericClient(BASE_URL);
      Patient patient = client.read().resource(Patient.class).withId("592824").execute();
      rtn = fhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient);

      System.out.println("Patient getEntity::rtn = ");
      System.out.println(rtn);
      System.out.println("--------------------");

      if (patient != null) {
        return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body(rtn);
      } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON).body(rtn);
      }
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON).body(rtn);
    }
  }



  @GetMapping("/Patient/get")
  public String getPatientDetails() {
    System.out.println( "=================Patient get=======================" );
    IGenericClient client = fhirContext.newRestfulGenericClient(BASE_URL);

    Patient patient = client.read().resource(Patient.class).withId("592824").execute();
    String rtn = fhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient);

    System.out.println("Patient get::rtn = ");
    System.out.println(rtn);
    System.out.println("--------------------");

    return rtn;
  }


  @PostMapping("/Patient/save")
  public String savePatientDetails(@RequestBody String p) {
    System.out.println( "==================Patient save======================" );
    System.out.println( p );

    IGenericClient client = fhirContext.newRestfulGenericClient(BASE_URL);
    IParser parser = fhirContext.newJsonParser();
    Patient firObject=parser.parseResource(Patient.class,p);
    System.out.println( "Patient created " + firObject );

    MethodOutcome s = client.create().resource(firObject).encodedJson().execute();
    System.out.println( "ID " + s.getId().getBaseUrl() + ", " + s.getId().getIdPart() + ", " +
            s.getId().getValue());

    System.out.println( "MethodOutcome created " + s );
    String jsonString = new JSONObject()
            .put("id", s.getId().getIdPart())
            .put("url", s.getId().getValue() + RAW_JSON)
            .put("status", 200)
            .toString();
    System.out.println(jsonString);

    return jsonString;
  }

}
