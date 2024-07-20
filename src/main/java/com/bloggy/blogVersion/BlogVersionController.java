package com.bloggy.blogVersion;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
public class BlogVersionController {

    private final BlogVersionService service;

//    @MessageMapping("blogVersion.addVersion") // maps incoming messages to /app/blogVersions.addVersions
//    @SendTo("/topic/addVersion") // sends response to /topic/addVersion
//    public BlogVersionResponse addVersion(
//            Authentication auth,
//            BlogVersionRequest request
//    ) {
//        return service.addVersion(request, auth.getName());
//    }

    @PostMapping("/blogs/{blogId}/versions")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BlogVersionResponse> addVersion(
            Authentication auth,
            @RequestBody BlogVersionRequest request,
            @PathVariable Long blogId
    ) {
        return ResponseEntity.ok(service.addVersion(request, blogId, auth.getName()));
    }

}
