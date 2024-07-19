package com.bloggy.blogVersion;

import com.bloggy.auth.AuthRequest;
import com.bloggy.auth.RegistrationRequest;
import com.bloggy.blog.Blog;
import com.bloggy.blog.IBlogRepository;
import com.bloggy.user.IUserRepository;
import com.bloggy.user.Role;
import com.bloggy.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class BlogVersionControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IBlogRepository blogRepository;

    @Autowired
    private IUserRepository userRepository;

    @LocalServerPort
    private Integer port;

    private WebSocketStompClient stompClient;
    private BlockingQueue<BlogVersionResponse> blockingQueue;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        stompClient = new WebSocketStompClient(new StandardWebSocketClient());
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        blockingQueue = new LinkedBlockingQueue<>();
    }

    @Test
    public void testAddVersion() throws Exception {
        User user1 = User.builder()
                .email("longpaus@gmail.com")
                .role(Role.USER)
                .password("password")
                .build();
        User savedUser1 = userRepository.save(user1);

        Blog blog = Blog.builder()
                .user(savedUser1)
                .title("Long's first blog")
                .build();
        Blog savedBlog = blogRepository.save(blog);


        // Obtain JWT token using MockMvc
        String token = getJwtToken("longpaus@gmail.com", "password");

        StompHeaders headers = new StompHeaders();
        headers.add("Authorization", "Bearer " + token);

        StompSession session = stompClient.connectAsync("ws://localhost:" + port + "/websocket", new StompSessionHandlerAdapter() {
                }, headers)
                .get(1, TimeUnit.SECONDS);

        session.subscribe("/topic/addVersion", new StompFrameHandler() {

            @Override
            public Type getPayloadType(StompHeaders headers) {
                return BlogVersionResponse.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                blockingQueue.add((BlogVersionResponse) payload);
            }
        });

        BlogVersionRequest request1 = BlogVersionRequest.builder()
                .blogId(savedBlog.getId())
                .content("version 1")
                .build();

        session.send("/app/blogVersion.addVersion", request1);

        BlogVersionResponse response = blockingQueue.poll(5, TimeUnit.SECONDS);
        assertNotNull(response);
    }

    private String getJwtToken(String email, String password) throws Exception {
        MvcResult res = mockMvc.perform(post("/api/v1/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new AuthRequest(email, password))))
                .andExpect(status().isOk())
                .andReturn();
        String response = res.getResponse().getContentAsString();
        return objectMapper.readTree(response).get("token").asText();
    }

    private void register(String username, String email, String password) throws Exception {
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new RegistrationRequest(username, email, password))))
                .andExpect(status().isOk());
    }
}
