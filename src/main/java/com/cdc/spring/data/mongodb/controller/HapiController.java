package com.cdc.spring.data.mongodb.controller;

import com.cdc.spring.data.mongodb.model.Document;
import com.cdc.spring.data.mongodb.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jaxrs.server.AbstractJaxRsResourceProvider;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
//import org.hl7.fhir.dstu3.model.HumanName;
//import org.hl7.fhir.dstu3.model.IdType;
//import org.hl7.fhir.dstu3.model.Patient;

import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.springframework.stereotype.Component;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.parser.IParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.json.JSONObject;

@CrossOrigin
@RestController
@RequestMapping("/hapi")
public class HapiController {


  @PostMapping("/Patient/save")
  public String savePatientDetails(@RequestBody String p) {

    String BASE_URL = "http://hapi.fhir.org/baseR4";
    String RAW_JSON = "?_format=json";

    System.out.println( "========================================" );

    System.out.println( p );

    System.out.println( "========================================" );

    FhirContext fhirContext = FhirContext.forR4();

    System.out.println( "FhirContext created " + fhirContext );

    IGenericClient client = fhirContext.newRestfulGenericClient(BASE_URL);

    System.out.println( "IGenericClient created " + client );

    IParser parser = fhirContext.newJsonParser();

    System.out.println( "IParser created " + parser );

    Patient firObject=parser.parseResource(Patient.class,p);

    System.out.println( "Patient created " + firObject );

    //MethodOutcome s = client.create().resource(firObject).prettyPrint()
      //      .encodedJson()
        //    .execute();

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
