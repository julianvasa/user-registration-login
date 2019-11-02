package com.staxter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.staxter.model.User;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import java.util.Set;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
@TestPropertySource(locations="classpath:test.properties")
public class UserRegistrationTest {

    @Value("${USER_ID}")
    private int USER_ID;
    @Value("${FIRST_NAME}")
    private String FIRST_NAME;
    @Value("${LAST_NAME}")
    private String LAST_NAME;
    @Value("${USERNAME}")
    private String USERNAME;
    @Value("${PASSWORD}")
    private String PASSWORD;
    @Value("${USER_ALREADY_EXISTS}")
    private static final String USER_ALREADY_EXISTS = "USER_ALREADY_EXISTS";
    @Value("${A_USER_WITH_THE_GIVEN_USERNAME_ALREADY_EXISTS}")
    private static final String A_USER_WITH_THE_GIVEN_USERNAME_ALREADY_EXISTS = "A user with the given username already exists";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Value("${NEW_USER_JSON}")
    private String NEW_USER_JSON;

    private final User USER_MISSING_DATA = User.builder().lastName(LAST_NAME).userName(USERNAME).plainTextPassword(PASSWORD).build();

    @Test
    public void whenCreatingNewUser_thenUserCreatedIfThereIsNotAnyOtherUserWithSameUsername() {
        try {
            this.mockMvc.perform(
                    post("/userservice/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(NEW_USER_JSON))
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(USER_ID)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", is(FIRST_NAME)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", is(LAST_NAME)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.userName", is(USERNAME)));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void whenCreatingNewUserAndUsernameAlreadyExists_thenThrowUserAlreadyExistsException() {
        try {
            this.mockMvc.perform(
                    post("/userservice/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(NEW_USER_JSON))
                    .andExpect(status().isConflict())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.code", is(USER_ALREADY_EXISTS)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.description", is(A_USER_WITH_THE_GIVEN_USERNAME_ALREADY_EXISTS)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void whenCreatingNewUserwithMissingInputData_thenThrowsValidationError() throws JsonProcessingException {
        final Set<ConstraintViolation<User>> violations = Validation.buildDefaultValidatorFactory().getValidator().validate(USER_MISSING_DATA);
        assertFalse(violations.isEmpty());
    }

}
