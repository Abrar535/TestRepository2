# Cefalo Newsportal Backend API 
The newsportal API is built as part of the intern training process to implementing a RESTful service.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites
Dependencies used for this project
* [Java jdk >= 8.0](https://www.oracle.com/java/technologies/javase-jdk8-downloads.html)
* [Spring Boot v2.2.4.RELEASE](https://docs.spring.io/spring-boot/docs/current/reference/html/getting-started.html)
* [Git](https://git-scm.com/downloads)

### Installing

Clone the repository using git
```
git clone https://github.com/fuadmmnf/Cefalo-newsportal-backend.git
```

Use the following command after navigating to the project root directory to run the apllication 

```
Maven
    mvn spring-boot:run
```

```
Gradle
    ./gradlew bootRun
```

## API Documentation
The API can negotiation content feeds in both JSON and XML formats.


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
    "password": "hashed password"
}


curl sample request:

curl --request POST 'http://localhost:8080/user/registration' --header 'Content-Type: application/json' \
--data-raw '{
	"name": "fuad",
	"userId": "fuadmmnf",
	"password": "fuadmmnf12345"
}'

```


Sample request/response body for /user/authenticate POST method

```
request:
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



curl sample request:

curl --request POST 'http://localhost:8080/user/authentication' --header 'Content-Type: application/json' \
--data-raw '{
	"userId": "fuadmmnf",
	"password": "fuadmmnf12345"
}'

curl response: 
    "jwt": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJmdWFkbW1uZjIiLCJleHAiOjE1ODgzMzIwMTksImlhdCI6MTU4ODMyNDgxOX0.HqoCn984ZukTJAHx9rUOATWPBCB5s8x0NVaYkc5qC6k"

```


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


curl sample request:
curl --request GET 'http://localhost:8080/posts'

```

| url `!!JWT Auth Required!!`| Description
| --- | --- 
| `[POST]` `/posts` |  creates new post |
| `[PUT]` `/posts` |  updates existing post with post_id specified in the request body|
| `[DELETE]` `/posts/${post_id}` |  deletes post with specified post_id|



Sample request/response for /posts POST method

```
multipart form request:

{
    "title": "post title",
    "body": "main post body"
    "photo": #file to upload#
    "draft": "true"/"false"
    "scheduledPublishTime": "time string in format yyyy-MM-dd'T'HH:mm:ss'Z'" //optional
}

response:
//returns post entity with currently logged in userId stored as authorId
{
    "createdAt": "creation time",
    "updatedAt": "last modified time",
    "id": post_id,
    "title": "post title",
    "body": "main post body",
    "draft": "draft status boolean",
    "published": "publish status boolean",
    "scheduledPublishTime": "publish time string"/null,
    "photoFilePath": "link to fetch image file",
    "authorId": "current_loggedIn_userId"
}


curl sample request:

curl --header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJmdWFkbW1uZjIiLCJleHAiOjE1ODgzMzIwMTksImlhdCI6MTU4ODMyNDgxOX0.HqoCn984ZukTJAHx9rUOATWPBCB5s8x0NVaYkc5qC6k'\
 -F title=curl -F body=body -F draft=false -F photo=@image.png -F scheduledPublishTime=2020-05-02T11:20:40Z\
 http://localhost:8080/posts

curl response:
{
    "createdAt":"2020-05-01T10:04:59.446+0000",
    "updatedAt":"2020-05-01T10:04:59.446+0000",
    "id":3,
    "title":"curl",
    "body":"body",
    "draft":false,
    "published":false,
    "scheduledPublishTime":"2020-05-02T11:20:40.000+0000",
    "photoFilePath":"/images/post3.png",
    "authorId":"fuadmmnf"
}


```
Sample request/response body for /posts PUT method

```
request:
// all fields required
{
    "id": post_id,
    "title": "updated title",
    "body": "updated body"
    "draft": "true"/"false"
}

response: no body with 201(created) status code

curl sample request:
curl --request PUT 'http://localhost:8080/posts' --header 'Content-Type: application/json' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJmdWFkbW1uZjIiLCJleHAiOjE1ODgzMzIwMTksImlhdCI6MTU4ODMyNDgxOX0.HqoCn984ZukTJAHx9rUOATWPBCB5s8x0NVaYkc5qC6k' \
--data-raw '{"id": 3, "title": "edited Curl Title", "body":"edited body", "draft":false }'

```



### Error Messages
| status code | message | probable cause
| --- | --- | ---
|`400(bad request)`|  json with error list, message, status| request body required field missing|
|`401(unauthorized)` | url path, timestamp, unauthorized message as JSON  |  update or delete post which is not associated with the JWT user/ invalid Auth credential/ missing Authorization request header/ unauthorized or expired JWT 
|`422(unprocessable entity)` | userID: ${userId} already exists  | requested user registration with existing userId 
|`404(not found)` | no body sent | request to non existing resource
