# 📚 Bookstore API

Welcome to the Bookstore API, a backend service for managing users, books, categories, shopping carts, and orders in an online bookstore. This project is built with Spring Boot, utilizing modern Java technologies and practices to deliver a scalable, secure, and efficient solution for managing an e-commerce bookstore.  
**🌐 Live Demo**: [Bookstore API on AWS](http://ec2-3-72-10-51.eu-central-1.compute.amazonaws.com/api/swagger-ui/index.html)

## Table of Contents
- [Introduction](#-introduction)
- [Features](#-features)
- [Technologies](#%EF%B8%8F-technologies)
- [API Endpoints](#-api-endpoints)
- [Setup](#-setup)
- [Challenges and Solutions](#-challenges-and-solutions)
- [Postman Collection](#-postman-collection)
- [Conclusion](#-conclusion)

## 🚀 Introduction

The Bookstore API was created to simplify the process of managing books and orders for both users and administrators. It supports essential functionalities for an online bookstore, including user registration, book browsing, shopping cart management, and order processing. The application also provides admin-level access to manage books and categories.

## 🌟 Features

### Shopping Cart Management
- **Add Books to Cart**: Users can add and manage books in their shopping cart.
- **Update Quantity**: Modify the quantity of a selected book.
- **View Cart**: Retrieve the user's current shopping cart.

### Book and Category Management
- **CRUD Operations**: Admins can add, edit, delete, and retrieve books.
- **Category Assignment**: Organize books into categories for better navigation.
- **Book Search**: Users can search for books by title, author, or category.

### Authentication and Authorization
- **User Registration**: New users can register with an email and password.
- **JWT Authentication**: Secure login that provides a JWT token for future requests.
- **Role-Based Access**: Distinct access levels for admins and users.

### Order Management
- **Place Orders**: Convert a shopping cart into an order and track its status.
- **Order History**: Users can view their past orders.

## 🛠️ Technologies

The Bookshop API leverages a number of modern Java technologies:
- **Spring Boot**: Core framework for building REST APIs.
- **Spring MVC**: Facilitates building web applications and RESTful APIs using the MVC pattern.
- **Spring Security**: Secures the application with JWT-based authentication.
- **Spring Data JPA**: Handles interactions with the relational database using Hibernate.
- **MySQL**: Relational database for storing books, users, and order information.
- **JUnit 5**: A testing framework for unit and integration tests.
- **Lombok**: Reduces boilerplate code with annotations for getters, setters, constructors, etc.
- **MapStruct**: Simplifies object mapping between DTOs and entities.
- **Liquibase**: Manages database schema changes with version control.
- **Swagger/OpenAPI**: Provides an interactive UI for exploring the API documentation.
- **Docker**: Containerization to run the application in isolated environments.

## 📋 API Endpoints

| Method | Endpoint        | Description                                           |
|------|-----------------|-------------------------------------------------------|
| POST | `/api/auth/registration` | Register a new user                                   |
| POST | `/api/auth/login` | Login and receive a JWT                               |
| GET  | `/api/books`    | Get a list of all books                               |
| GET  | `/api/books/{id}` | Get details of a specific book                        |
| GET  | `/api/books/search` | Search books by specific parameters                   |
| POST | `/api/books`    | Add a new book (admin only)                           |
| PUT  | `/api/books/{id}` | Update an existing book (admin only)                  |
| DELETE | `/api/books/{id}` | Delete a book (admin only)                            |
| GET  | `/api/categories` | Get a list of all categories                          |
| POST | `/api/categories` | Add a new category (admin only)                       |
|GET |	`api/categories/{id}` | 	Get one category according to its ID                 |
|PUT 	| `api/categories/{id}` | 	Update an existing category by its ID (admin only)   |
|DELETE |	`api/categories/{id}` | 	Delete one category according to its ID (admin only) |
|GET 	| `api/categories/{id}/books` 	 | Get list of books by specific category ID             |
| GET  | `/api/cart`     | Get the current user's shopping cart                  |
| POST | `/api/cart`     | Add an item to the shopping cart                      |
| PUT  | `/api/cart/items/{cartItemId}` | Update item quantity in the cart                      |
| DELETE | `/api/cart/items/{cartItemId}` | Remove an item from the cart                          |
| POST | `/api/orders`   | Create a new order                                    |
| GET  | `/api/orders`   | Get the user's order history                          |
|PATCH |	`api/orders/{id}` | 	Update orders status by its ID      |
|GET 	| `api/orders/{orderId}/items/{itemId}` |	Get order item by its ID and orders ID |
|GET 	| `api/orders/{orderId}/items` |	Get order items by orders ID |

## 💻 Setup

### Prerequisites
- **Java 17+**
- **Maven**
- **MySQL** (or a compatible database)
- **Docker** (optional for containerized deployment)

### Local Setup

1. **Clone the repository**:
   ```bash
   git clone https://github.com/apanyutin/book-store.git
   cd bookstore-api
   ```

2. **Set up MySQL**:
   Create a database and update `application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/bookstore
   spring.datasource.username=root
   spring.datasource.password=yourpassword
   spring.jpa.hibernate.ddl-auto=update
   ```

3. **Build and Run**:
   Build the project and start the application.
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

4. **Access the API**:
   Visit `http://localhost:8080/api/swagger-ui/index.html` to explore the API documentation via Swagger UI.

### Running with Docker

1. Build the Docker image:
   ```bash
   docker build -t bookstore-api .
   ```

2. Run the application:
   ```bash
   docker-compose up
   ```

3. Access the API via `http://localhost:8080`.

## 🔍 Challenges and Solutions

During the development of the Bookshop API, several challenges arose:

1. **Efficient Book Search**: Implementing search functionality that scales with large datasets was a key challenge. The solution was to use indexing on frequently searched fields like book titles and categories.

2. **Managing Complex Entity Relationships**: Relationships between users, books, orders, and shopping carts introduced complexity. Utilizing `@EntityGraph` and optimizing lazy loading helped in addressing performance issues.

3. **Securing the Application**: Ensuring secure authentication was achieved through JWT tokens, which provided stateless, secure communication between the frontend and backend.

## 🧾 Postman Collection

You can test the API endpoints using Postman. A collection of the requests is provided for your convenience. [Download the Postman collection here](https://github.com/apanyutin/book-store/blob/master/book-store.postman_collection.json) and import it into your Postman client.

## 📎 Conclusion

The Bookstore API provides a robust and secure foundation for managing users, books, categories, and orders in an online bookstore environment. With modern technologies like Spring Boot, Spring MVC, Spring Security, and Spring Data JPA, it is designed for scalability and maintainability.

Feel free to clone, modify, and experiment with the code for your own projects. Happy coding!