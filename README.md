
# **📌 Job Listing Portal API**

A RESTful API for job seekers and employers to **post, search, apply**, and **bookmark jobs**.

---

## **📖 Table of Contents**
- [🚀 Features](#-features)
- [🛠️ Tech Stack](#-tech-stack)
- [🖥️ Setup & Installation](#-setup--installation)
- [🔐 Authentication & JWT](#-authentication--jwt)
- [⚡ API Documentation](#-api-documentation)
  - [📝 User Authentication](#-user-authentication)
  - [📌 Job Management](#-job-management)
  - [📑 Job Applications](#-job-applications)
  - [⭐ Bookmark Jobs](#-bookmark-jobs)
- [📄 License](#-license)

---

## **🚀 Features**
- **User Registration & Authentication** (JWT-based)
- **Employers** can **post, update, and delete jobs**
- **Job Seekers** can **apply for jobs**
- **Search & filter** jobs by **position, location, type**
- **Bookmark jobs** for future reference

---

## **🛠️ Tech Stack**
- **Backend:** Spring Boot, Hibernate, Spring Data JPA
- **Database:** MySQL
- **Authentication:** JWT (JSON Web Token)

---

## **🖥️ Setup & Installation**

### **🔧 Prerequisites**
- Install **Java 23**
- Install **Maven 3.9.9**
- Install **MySQL 9.2.0**

### **📌 Steps**
1. **Clone the repository**
   ```bash
   git clone https://github.com/ShivamRajChauhan007/JobPortal.git
   cd job-listing-portal
   ```

2. **Configure the Database**  
   Update `application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/jobportal
   spring.datasource.username=root
   spring.datasource.password=yourpassword
   spring.jpa.hibernate.ddl-auto=update
   ```

3. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

4. **Access API**  
   - API runs on: `http://localhost:8080/jobListingPortal`

---

## **🔐 Authentication & JWT**
- API uses **JWT tokens** for authentication.
- **Public Endpoints (No Token Required)**:
  - `POST /jobListingPortal/register`
  - `POST /jobListingPortal/login`
  - `GET /jobListingPortal/list`
- **All other endpoints require JWT Token** in the `Authorization` header:
  ```
  Authorization: Bearer <JWT_TOKEN>
  ```

---

## **⚡ API Documentation**

### **📝 User Authentication**

#### **🔹 Register a User**
- **Endpoint:** `POST /jobListingPortal/register`
- **Request Body (JSON):**
  ```json
  {
    "name": "John Doe",
    "email": "johndoe@example.com",
    "password": "SecurePassword123",
    "role": "JOB_SEEKER"
  }
  ```
- **Response:**  
  ```
  User Registered Successfully!
  ```

#### **🔹 Login & Get JWT Token**
- **Endpoint:** `POST /jobListingPortal/login`
- **Request Body (JSON):**
  ```json
  {
    "email": "johndoe@example.com",
    "password": "SecurePassword123"
  }
  ```
- **Response:**
  ```json
  {
    "token": "eyJhbGciOiJIUzI1NiJ9..."
  }
  ```

---

### **📌 Job Management**
#### **🔹 Create a Job** _(EMPLOYER Only)_
- **Endpoint:** `POST /jobListingPortal/create`
- **Request Body (JSON):**
  ```json
  {
    "position": "Software Engineer",
    "description": "Develop and maintain Java applications.",
    "skills": ["Java", "Spring Boot", "Microservices"],
    "location": "New York, NY",
    "jobType": "FULL_TIME",
    "organisation": "Tech Solutions Inc.",
    "noOfVacencies": 3
  }
  ```
- **Response:**  
  ```
  Job created Successfully with jobID: 2
  ```

#### **🔹 Update a Job** _(EMPLOYER Only)_
- **Endpoint:** `PUT /jobListingPortal/update/{jobId}`
- **Request Body (JSON):**
  ```json
  {
    "position": "Senior Software Engineer",
    "description": "Develop, deploy and maintain enterprise Java applications.",
    "skills": ["Java", "Spring Boot", "AWS", "Microservices"],
    "location": "New York, NY",
    "jobType": "FULL_TIME",
    "organisation": "Tech Solutions Inc.",
    "noOfVacencies": 2
  }
  ```
- **Response:**  
  ```
  Updated job with jobId: 1
  ```

#### **🔹 Delete a Job** _(EMPLOYER Only)_
- **Endpoint:** `DELETE /jobListingPortal/{jobId}`
- **Response:**  
  ```
  Job deleted successfully
  ```

#### **🔹 List Jobs (Filterable)**
- **Endpoint:** `GET /jobListingPortal/list`
- **Query Params (Optional):**  
  - `position`
  - `location`
  - `type`
- **Example:**  
  ```
  GET /jobListingPortal/list?position=Software Engineer&location=NY&type=FULL_TIME
  ```
- **Response:**
  ```json
  {
    "content": [
      {
        "position": "Software Engineer",
        "description": "Develop and maintain Java applications.",
        "skills": ["Java", "Spring Boot", "Microservices"],
        "location": "New York, NY",
        "jobType": "FULL_TIME",
        "organisation": "Tech Solutions Inc.",
        "noOfVacencies": 3
      }
    ]
  }
  ```

---

### **📑 Job Applications**

#### **🔹 Apply for a Job** _(JOB_SEEKER Only)_
- **Endpoint:** `POST /jobListingPortal/apply`
- **Request Body (JSON):**
  ```json
  {
    "jobId": 2,
    "resumeLink": "https://drive.com/johndoe_resume",
    "coverLetterLink": "https://drive.com/johndoe_coverletter"
  }
  ```
- **Response:**
  ```json
  {
    "id": 1,
    "jobid": 2,
    "position": "Software Engineer",
    "resume": "https://drive.com/johndoe_resume",
    "coverLetter": "https://drive.com/johndoe_coverletter",
    "status": "PENDING",
    "appliedAt": "2025-02-23T01:12:23.454931"
  }
  ```

#### **🔹 View My Applications** _(JOB_SEEKER Only)_
- **Endpoint:** `GET /jobListingPortal/my-applications`
- **Response:** _(List of applied jobs)_

#### **🔹 View Job Applications** _(EMPLOYER Only)_
- **Endpoint:** `GET /jobListingPortal/job/{jobId}/applications`
- **Response:** _(List of applicants)_

---

### **⭐ Bookmark Jobs**

#### **🔹 Bookmark a Job** 
- **Endpoint:** `POST /jobListingPortal/bookmark/{jobId}`
- **Response:**  
  ```
  Job bookmarked successfully!
  ```

#### **🔹 Get My Bookmarked Jobs** 
- **Endpoint:** `GET /jobListingPortal/my-bookmarks`
- **Response:** _(List of bookmarked jobs)_

---

