## Spring Boot API for Oracle Weblogic
Application to demonstrate various parts of a service oriented RESTfull application.

### Technology Stack
Component         | Technology
---               | ---
REST              | SpringBoot (Java)
Security          | Token Based (Spring Security and JWT )
caching           | Redis
DB                | ORACLE database 21c
Persistence       | JPA (Using Spring Data)
Server Build Tools| Maven(Java)
Server            | Oracle Weblogic 14c

## Prerequisites
Ensure you have this installed before proceeding further
- Java 1.8
- Maven 3.x.x
- ORACLE database 21c
- Oracle Weblogic 14c
- Redis

## Features of the Project
- Login (username + password)
- View Courses
- Register to a course
- Cancel a course registration
- Get course schedule as PDF.

## Steps to Setup
**1. Clone the application**

**2. Create Oracle database**
```bash
create database STUDENTSCH
```
- run `src/main/resources/DBScript.sql`

**3. Change Oracle username and password and Redis data as per your installation**
+ open `src/main/resources/application.properties`
+ change `spring.datasource.url` , `spring.datasource.username` and `spring.datasource.password` as per your oracle installation
+ change `spring.redis.host` , `spring.redis.port` as per your redis installation

**4. Build and deploy the application to weblogic server**

The application server will start at <http://localhost:7001/app>

#### Database Schema
![ER Diagram](/screenshots/ERD.PNG?raw=true)

#### test-cases for all the APIs as postman project file
- `src/main/resources/ajaxClient.html`
