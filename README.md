# Community Forum

Community Forum is a full-stack discussion platform where users can create posts, participate in conversations, reply to comments, like posts, and organize discussions with tags. The project is built with Spring Boot following a layered architecture and includes both a REST API and a Spring MVC (Thymeleaf) frontend.

---

## Features

- User registration and authentication
- Create, edit and delete posts
- Comment on posts
- Reply to comments
- Like and unlike posts
- Tag posts
- Browse all posts
- View latest posts
- View top commented posts
- User profiles
- REST API with Swagger documentation
- Spring MVC frontend using Thymeleaf

---

## Technologies

- Java 17
- Spring Boot
  - Spring MVC
  - Spring Security
  - Spring Data JPA
  - Spring Validation
- Thymeleaf
- Hibernate
- MariaDB
- Swagger (OpenAPI)
- Gradle
- JUnit 5
- Mockito

---

## Swagger Documentation

After starting the application, Swagger is available at:

```
http://localhost:8080/swagger-ui.html
```

---

## Running the Project

### Prerequisites

- JDK 17 or newer
- MariaDB running on port **3306**

### 1. Clone the repository

```bash
git clone https://github.com/TelerikAcademyAlpha/CommunityForum.git
cd CommunityForum
```

*(Update the repository URL if it changes.)*

### 2. Create the database

```bash
mysql -u root -p -e "CREATE DATABASE community_forum"
```

### 3. Load the database schema

```bash
mysql -u root -p community_forum < src\main\resources\schema.sql
mysql -u root -p community_forum < src\main\resources\insert_data.sql
```

If `mysql` is not recognized, either add MariaDB's `bin` directory to your PATH or run it using its full path.

Example:

```bash
"C:\Program Files\MariaDB <version>\bin\mysql.exe" -u root -p
```

### 4. Configure database credentials

If your MariaDB username or password differs from the default configuration, update:

```
src/main/resources/application.properties
```

### 5. Run the application

```bash
gradlew.bat bootRun
```

The application will start at:

```
http://localhost:8080
```

---

## Database

The application uses MariaDB with the following tables:

- users
- posts
- comments
- tags
- post_tags
- post_likes

Database scripts are located in:

```
src/main/resources/
```

- `schema.sql`
- `insert_data.sql`

---

## ER Diagram

![ER Diagram](./docs/er-diagram.png)

---

## Project Structure

```
src/main/java/com/backend/communityforum/

├── config/          # Spring & Hibernate configuration
├── controllers/     # MVC (Thymeleaf) controllers
├── rest/            # REST API controllers
├── services/        # Business logic
├── repositories/    # Data access layer
├── mapper/          # Entity ↔ DTO mapping
├── models/          # Entities and DTOs
├── security/        # Authentication & authorization
└── exceptions/      # Custom exceptions & global handler
```

---

## REST API

### Users

```
/api/users
```

- Register
- Login
- Logout
- View profile
- Update profile
- Block / Unblock users (Admin)
- Delete users

### Posts

```
/api/posts
```

- Create
- Update
- Delete
- Get by ID
- Get all posts
- Latest posts
- Top commented posts
- Search posts
- Like / Unlike posts

### Comments

```
/api/comments
```

- Create
- Update
- Delete
- Reply
- Get comments by post
- Get comments by author

### Tags

```
/api/tags
```

- Create
- Get by name
- List all tags

Complete endpoint documentation is available through Swagger.

---

## Running Tests

```bash
gradlew.bat test
```

---

## Team

- amishinev
- seyhan0126
- kirilvasilev00

---

## Repository

```
https://github.com/TelerikAcademyAlpha/CommunityForum
```