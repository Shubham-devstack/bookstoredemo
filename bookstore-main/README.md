# Bookstore
Bookstore Management System

# Bookstore Project

This project implements a RESTful API for managing a bookstore's catalog, allowing users to interact with book and author data.

## API Endpoints

The API provides the following endpoints for managing the bookstore:

-   **GET `/api/v1/books/{isbn}`:**  Retrieve book details by ISBN.
-   **PUT `/api/v1/books/{isbn}`:**  Update book details.
-   **DELETE `/api/v1/books/{isbn}`:**  Remove a book from the catalog.
-   **GET `/api/v1/books/`:**  Retrieve the complete book catalog.
-   **POST `/api/v1/books/`:**  Create a new book entry.
-   **GET `/api/v1/books/search`:**  Search books by title and/or author.

**Swagger UI:**

You can explore and interact with the API through the Swagger UI at: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html) (once the application is running).

## Getting Started

### Prerequisites

-   Java 21 or higher
-   Maven
-   Docker

### Setup

1. **Clone the repository:**

    ```bash
    git clone https://github.com/Shreyasd10/bookstore.git
    cd bookstore
    ```

2. **Database Setup (using Docker):**
    The included `docker-compose` can be used to set up the required database. You'll need to build and run a Docker image b to prepare your database environment.
   
    ```bash
    cd bookstore
    docker-compose up -d  
    ```


4. **Run the Application:**

    After setting up the database, build and run the Spring Boot application using Maven:

    ```bash
    mvn clean install
    mvn spring-boot:run
    ```

    The application will start on port 8080.

## Contributing

Contributions are welcome! Please fork the repository and submit a pull request with your changes.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

