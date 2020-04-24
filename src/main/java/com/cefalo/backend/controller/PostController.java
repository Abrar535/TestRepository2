package com.cefalo.backend.controller;


import com.cefalo.backend.model.Post;
import com.cefalo.backend.service.IPostService;
import com.cefalo.backend.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.Optional;

@RestController
public class PostController {


    private static String STATIC_DIR = "/images/";

    private final IPostService iPostService;
    private final UploadService uploadService;


    @Autowired
    public PostController(IPostService iPostService, UploadService uploadService) {
        this.iPostService = iPostService;
        this.uploadService = uploadService;
    }

    @RequestMapping(
            value = "/posts",
            method = RequestMethod.GET,
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,
            }
    )
    public ResponseEntity<Iterable<Post>> getAllPosts(@RequestParam(name = "page", required = false) String pageCount,
                                                      @RequestParam(name = "size", required = false) String dataSize) {

        int pageNumber, pageSize;
        try {
            pageNumber = Integer.parseInt(pageCount);
        } catch (NumberFormatException e) {
            pageNumber = -1;
        }
        try {
            pageSize = Integer.parseInt(dataSize);
        } catch (NumberFormatException e) {
            pageSize = 8;
        }
        return new ResponseEntity<>(iPostService.getAllPostsByPage(pageNumber, pageSize), HttpStatus.OK);
    }


    @RequestMapping(
            value = "/posts/draft",
            method = RequestMethod.GET,
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,
            }
    )
    public ResponseEntity<Iterable<Post>> getDraftUserPosts(@RequestParam(name = "page", required = false) String pageCount,
                                                            @RequestParam(name = "size", required = false) String dataSize,
                                                            Principal principal) {

        int pageNumber, pageSize;
        try {
            pageNumber = Integer.parseInt(pageCount);
        } catch (NumberFormatException e) {
            pageNumber = -1;
        }
        try {
            pageSize = Integer.parseInt(dataSize);
        } catch (NumberFormatException e) {
            pageSize = 8;
        }
        return new ResponseEntity<>(iPostService.getAllUserDraftsByPage(pageNumber, pageSize, principal.getName()), HttpStatus.OK);
    }

    @RequestMapping(
            value = "/posts/{id}",
            method = RequestMethod.GET,
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,
            }
    )
    public ResponseEntity<Post> getPostById(@PathVariable Long id) {
        Optional<Post> post = iPostService.findById(id);
        return post.map(p -> new ResponseEntity<>(p, HttpStatus.OK))
                .orElseGet(() -> ResponseEntity.notFound().build());

    }


    @RequestMapping(
            value = "/posts",
            method = RequestMethod.POST,
            consumes = {
                    MediaType.MULTIPART_FORM_DATA_VALUE,
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,
            },
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,
            }
    )
    public ResponseEntity<?> createPost(@RequestParam("title") String title,
                                        @RequestParam("body") String body,
                                        @RequestParam(value = "draft") String draft,
                                        @RequestParam("photo") MultipartFile file,
                                        Principal principal) {
        Optional<Post> post = iPostService.createNewPost(new Post(title, body, Boolean.valueOf(draft)), principal.getName());
        if (post.isPresent()) {
            Post tempPost = post.get();
            boolean isUploadComplete = uploadService.consumeFile(file, String.valueOf(post.get().getId()));
            if (isUploadComplete) {
                tempPost.setPhotoFilePath(STATIC_DIR + "post" + post.get().getId() + ".png");
                post = iPostService.save(tempPost);
            }

        }

        return (post.isPresent()) ? new ResponseEntity<>(post, HttpStatus.CREATED)
                : new ResponseEntity<>("Unable to save post, violating contraints", HttpStatus.BAD_REQUEST);

    }


    @RequestMapping(
            value = "/posts",
            method = RequestMethod.PUT,
            consumes = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,

            },
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,
            }
    )
    public ResponseEntity<?> editPost(@RequestBody Post requestPost, Principal principal) {
        Optional<Post> post = iPostService.updatePost(requestPost, principal.getName());
        return (post.isPresent()) ? new ResponseEntity<>(post, HttpStatus.NO_CONTENT)
                : new ResponseEntity<>("Unauthorized access to post", HttpStatus.UNAUTHORIZED);
    }


    @RequestMapping(
            value = "/posts/{id}",
            method = RequestMethod.DELETE
    )
    public ResponseEntity<?> deletePost(@PathVariable Long id, Principal principal) {
        return (iPostService.deletePost(id, principal.getName())) ? new ResponseEntity<>(true, HttpStatus.NO_CONTENT)
                : new ResponseEntity<>("Unauthorized access to post", HttpStatus.UNAUTHORIZED);
    }

}
