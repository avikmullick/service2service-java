package com.sap.cc.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    private static final String API_V1_USERS_PATH = "/api/v1/users";

    @Autowired
    private UserStorage userStorage;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void beforeEach() {
        userStorage.deleteAllUsers();
    }

    @Test
    public void signUpUser_returnsCreatedUser() throws Exception {

        User myUser = new User("John", "06252 584828", "0");
        String jsonBody = objectMapper.writeValueAsString(myUser);

        MockHttpServletResponse response = this.mockMvc
                .perform(post(API_V1_USERS_PATH).content(jsonBody)
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.name", is(myUser.getName())))
                .andExpect(jsonPath("$.phoneNumber", is(myUser.getPhoneNumber())))
                .andExpect(jsonPath("$.fontPreference", is(myUser.getFontPreference())))
                .andExpect(jsonPath("$.id", notNullValue())).andReturn().getResponse();

        User returnedUser = objectMapper.readValue(response.getContentAsString(), User.class);
        assertThat(response.getHeader("location"), is(API_V1_USERS_PATH + "/" + returnedUser.getId()));
    }

    @Test
    void printPrettyPage_returnsUserAttributes() throws Exception {
        User myUser = new User("John", "06252 584828", "0");
        String jsonBody = objectMapper.writeValueAsString(myUser);

        MockHttpServletResponse response = this.mockMvc
                .perform(post(API_V1_USERS_PATH).content(jsonBody)
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getContentAsString(), containsString("John"));
        assertThat(response.getContentAsString(), containsString("06252 584828"));
    }
}
