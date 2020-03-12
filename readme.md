# Cefalo Newsportal Backend API 
The newsportal API is built as part of the intern training process to implementing a RESTful service.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

* [Java jdk >= 8.0](https://www.oracle.com/java/technologies/javase-jdk8-downloads.html)
* [Spring Boot](https://docs.spring.io/spring-boot/docs/current/reference/html/getting-started.html)


### Installing

Use the following command after navigating to the project root directory to run the apllication 

```
Maven
    mvn spring-boot:run
```

```
Gradle
    ./gradlew bootRun
```

### API Documentation
The API can negotiation content feeds in both JSON and XML formats.

#### Posts
| url | Description
| --- | --- 
| `[GET]` `/posts` |  responds with all posts in the database |
| `[GET]` `/posts/${post_id}` |  responds with post with the id specified |
| `[GET]` `/posts?page=${page_number}` |  adds panigation support with page size = 8, page_number starts at 0
| `[GET]` `/posts?page=${page_number}&size=${page_size}` | pagination with custom page size 

```
Sample Response for /posts
{
    "content": [
        {
            "createdAt": "creation time",
            "updatedAt": "last modified date",
            "id": post_id,
            "title": "post title",
            "body": "main body",
            "authorId": "author unique Id"
        },
        {
            ...........
        },
    ]
    ........
    //in case of pagination//
        
    "totalPages": total_number_of_pages,
    "totalElements": total_number_of_elements,
    ................
    "numberOfElements": number_of_elements_received
    ...
}
```

| url `!!JWT Auth Required!!`| Description
| --- | --- 
| `[POST]` `/posts` |  creates new post |
| `[PUT]` `/posts` |  updates existing post with post_id specified in the request body|
| `[DELETE]` `/posts/${post_id}` |  deletes post with specified post_id|



Sample request/response for /posts POST method

```
request:

// all fields required
{
    "title": "post title",
    "body": "main post body"
}

response:
//returns post entity with currently logged in userId stored as authorId
{
    "createdAt": "creation time",
    "updatedAt": "last modified time",
    "id": post_id,
    "title": "post title",
    "body": "main post body",
    "authorId": "current_loggedIn_userId"
}
```
Sample request body for /posts PUT method

```
// all fields required
{
    "id": post_id,
    "title": "updated title",
    "body": "updated body"
}
```

#### User

| url | Description
| --- | --- 
| `[POST]` `/user/registration` |  creates new user [Must provide unique userId]|
| `[POST]` `/user/authenticate` |  autenticates user login and responds with a JWT for the user(expiration time: 2 hours)|


Sample request/response for /user/registration POST method

```
request:

// all fields required
{
	"name": "user name",
	"userId": "unique userId",
	"password": "user password"
}

response:
//returns user entity hiding the password in response stored after hashing in the database
{
    "createdAt": "creation time",
    "updatedAt": "last modified time",
    "id": id,
    "userId": "unique userId",
    "name": "user name"
}
```


Sample request/response body for /user/authenticate POST method

```
// all fields required
{
    "userId": "unique userId",
    "password": "user password"
    
}

response:
//returns JWT() for the userId if authentication is successful
{
    "jwt": "user_JWT"
}
```