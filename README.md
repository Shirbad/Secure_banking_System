#  Banking System Backend

A secure and scalable **Banking System Backend** built using **Java and Spring Boot**, implementing real-world banking operations with strong focus on **security, transaction processing, and business logic**.

---

## Features

###  Authentication & Security

* User registration and login
* Password encryption using **BCrypt**
* **JWT-based authentication**
* Secure REST APIs with token validation

---

###  Account Management

* Create and manage bank accounts
* Fetch account details
* View all accounts

---

###  Core Banking Operations

* Deposit money
* Withdraw money
* Transfer funds between accounts

---

###  Transactions

* Record all transactions (Deposit, Withdraw, Transfer)
* View transaction history per account

---

###  Payments & Cashback

* Payment processing system
* **2% cashback calculation**
* Scheduled cashback processing using Spring Scheduler
* Payment status tracking (PENDING / COMPLETED)

---

###  Advanced Features

* Top spenders API (analytics)
* Fraud detection system:

  * Large transfer detection
  * Multiple transactions within short time detection
* Centralized exception handling with structured JSON responses
* Clean layered architecture (Controller → Service → Repository)

---

##  Tech Stack

* **Java**
* **Spring Boot**
* **Spring Security**
* **JWT (JSON Web Token)**
* **Spring Data JPA**
* **Hibernate**
* **MySQL**
* **Maven**
* **Postman (API Testing)**

---

##  Project Structure

```
src/main/java/net/javalearn/banking

├── controller
├── service
├── service/impl
├── repository
├── entity
├── dto
├── config
├── security
├── exception
├── scheduler
```

---

##  API Endpoints

###  Authentication

* POST `/api/auth/register`
* POST `/api/auth/login`

---

###  Accounts

* POST `/api/accounts`
* GET `/api/accounts/{id}`
* GET `/api/accounts`

---

###  Transactions

* PUT `/api/accounts/{id}/deposit`
* PUT `/api/accounts/{id}/withdraw`
* PUT `/api/accounts/transfer`
* GET `/api/transactions/{accountId}`

---

###  Payments

* POST `/api/payments/pay`
* GET `/api/payments/{paymentId}`

---

###  Analytics

* GET `/api/accounts/top-spenders?n=5`

---

##  Authentication

After login, include JWT token in request headers:

```
Authorization: Bearer <your_token>
```

---

##  Setup Instructions

### 1️⃣ Clone the Repository

```
git clone https://github.com/your-username/banking-system-backend.git
cd banking-system-backend
```

---

### 2️⃣ Configure Database

Update `application.properties`:

```
spring.datasource.url=jdbc:mysql://localhost:3306/banking_db
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```

---

### 3️⃣ Run the Application

```
mvn spring-boot:run
```

---

##  Testing

Use **Postman** to test APIs:

1. Register a user
2. Login → receive JWT token
3. Add token in Authorization header
4. Access secured endpoints

---

##  Key Learnings

* Implemented secure authentication using JWT and BCrypt
* Designed real-world banking workflows (transactions, payments, cashback)
* Built scheduler-based background processing
* Implemented fraud detection logic for suspicious transactions
* Structured API responses with proper error handling
* Applied clean architecture and separation of concerns

---

##  Author

**Disha Shirbad**
Backend Developer | Java | Spring Boot

---

##  Project Highlights

* Real-world banking backend system
* Secure authentication & authorization
* Fraud detection implementation
* Scheduler-based cashback processing
* Production-level backend design

---

##  Future Improvements

* Role-based access control (ADMIN/USER)
* Email/OTP verification
* Docker deployment
* Cloud deployment (AWS / Render)
