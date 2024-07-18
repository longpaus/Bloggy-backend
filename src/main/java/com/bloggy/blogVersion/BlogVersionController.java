package com.bloggy.blogVersion;

import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

@AllArgsConstructor
@Controller
public class BlogVersionController {

    private final BlogVersionService service;

    @MessageMapping("blogVersion.addVersion") // maps incoming messages to /app/blogVersions.addVersions
    @SendTo("/topic/addVersion") // sends response to /topic/addVersion
    public BlogVersionResponse addVersion(
            Authentication auth,
            BlogVersionRequest request
    ) {
        return service.addVersion(request, auth.getName());
    }
}
