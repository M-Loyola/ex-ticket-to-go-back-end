package com.tickettogo.TicketToGoBackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tickettogo.TicketToGoBackend.entity.User;
import com.tickettogo.TicketToGoBackend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void should_show_all_users_when_perform_get_given_few_users() throws Exception {
        //given
        User savedUser1 = userRepository.save(new User("Jens", "Jovellano", "jensjovellano@gmail.com", "09228509618", "jenspassword"));
        User savedUser2 = userRepository.save(new User("Clark", "Kent", "clarkkent@gmail.com", "123123123", "wewewew"));
        //when and then
        mockMvc.perform(get("/users"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath(("$[0].firstName")).value("Jens"))
                .andExpect(MockMvcResultMatchers.jsonPath(("$[1].firstName")).value("Clark"));
    }

    @Test
    void should_specific_user_when_perform_get_given_user_id() throws Exception {
        //given
        User savedUser1 = userRepository.save(new User("Jens", "Jovellano", "jensjovellano@gmail.com", "09228509618", "jenspassword"));
        //when and then
        mockMvc.perform(get("/users/{userId}", savedUser1.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath(("$.firstName")).value("Jens"));
    }

    @Test
    void should_create_new_user_when_perform_post_given_new_user_info() throws Exception {
        //given
        User savedUser1 = userRepository.save(new User("Jens", "Jovellano", "jensjovellano@gmail.com", "09228509618", "jenspassword"));
        ObjectMapper objectMapper = new ObjectMapper();
        String newUserString = objectMapper.writeValueAsString(savedUser1);
        //when
        mockMvc.perform(post("/users/createUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newUserString))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Jens"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Jovellano"));
        //then
    }
}