package org.bashirov.cashdeposit.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bashirov.cashdeposit.entity.Users;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
public class UsersTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private static String randomEmail;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Order(1)
    @Test
    public void givenUser_whenMockMVC_thenIsOk() throws Exception {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        randomEmail = getUniqueEmailString();
        Users users = new Users("Barsik", 5, randomEmail);

        this.mockMvc
                .perform(post("/users/create")
                        .contentType("application/json").content(gson.toJson(users)))
                .andDo(print()).andExpect(status().isOk());
    }

    @Order(2)
    @Test
    public void givenEmailWithParameter_whenMockMVC_thenVerifyUsername() throws Exception {
        this.mockMvc
                .perform(get("/users/usersByEmail").param("email", randomEmail))
                .andDo(print()).andExpect(status().isOk())

                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.name").value("Barsik"));
    }

    private String getUniqueEmailString() {
        String dt = java.time.LocalDateTime.now().toString();
        dt = dt.replaceAll("[^\\d.]", "");
        return dt + "@mail.ru";
    }
}
