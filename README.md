## Spring Boot based Application for Health Level 7 (HL7) and hapi fihr
   
Backend Spring Boot application that access HAPI-FHIR, MongoDB and MySQL
```
Technology Stack:
Java: 17
Spring Boot: 3.1.0
HAPI-FHIR: R4
MongoDB: 7.0
MySQL: 8.0
```

## Access latest HAPI-FHIR (R4) from Spring boot 3.1.0

Backend Spring Boot application that access HAPI-FHIR, MongoDB and MySQL

## Getting Started

To get started, just download ( gh, git or zip ), build, run and access from your favorite browser

## Prerequisites

Make sure JDK 17 is installed.

```
Give examples

To retrieve a patent details by id, GET [URL]/hapi/Patient/getWithID?id=592824
To create a new patent, POST [URL]/hapi/Patient/save  with a JSON.

To test CRUD with Mongo
To retrieve all files, GET [URL]/hapi/api/documents
To retrieve one file, GET [URL]/hapi/api/documents//{id}
To create a new file, POST [URL]/hapi/api/documents with JSON
To delete one file, GET [URL]/hapi/api/documents/{id}
To delete all files, GET [URL]/hapi/api/documents
To retrieve all files, GET [URL]/hapi/api/documents
To update one files, PUT [URL]/hapi/api/documents/{id}

```

## Installing

Use gh, git or download the .zip file.

mvn clean install

mvn spring-boot:run

visit URL http://localhost:8080/api/documents when running locally



## Deployment

Add additional notes about how to deploy this on a live system

## Built With

Maven

## Contributing

Michael peng

## Versioning

1.0.1

## Authors

**Michael Peng** - *Initial work* - [mpeng](https://github.com/mpeng)

https://www.theoptionslab.com

https://talkingcalendarreminder.com

https://audenty.herokuapp.com


## License

This project is licensed under the MIT License 

## Acknowledgments

* Hat tip to anyone whose code was used
* Inspiration
* etc

