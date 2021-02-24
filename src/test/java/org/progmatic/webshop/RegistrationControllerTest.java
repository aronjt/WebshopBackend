package org.progmatic.webshop;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.progmatic.webshop.testservice.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class RegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    TestService service;

    @Test
    void registration_with_new_user() throws Exception {
        String json = service.createJson(service.createUser("mindmegegyed@gmail.com"));
        if (json != null) {
            MvcResult result = mockMvc.perform(
                    post("/register")
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isOk())
                    .andReturn();
            String response = result.getResponse().getContentAsString();
            assertTrue(response.contains("true"));
            assertTrue(response.contains("Confirmation token sent to New User"));
        }
    }

    @Test
    void registration_with_taken_email_address() throws Exception {
        String json = service.createJson(service.createUser(TestService.USER_EMAIL));
        if (json != null) {
            MvcResult result = mockMvc.perform(
                    post("/register")
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isOk())
                    .andReturn();
            String response = result.getResponse().getContentAsString();
            assertTrue(response.contains("false"));
            assertTrue(response.contains("This email adress already exists"));
        }
    }

    @Test
    void confirmation_with_valid_token() throws Exception {
        String token = service.findToken(service.findUser(TestService.TEST_REGISTERED_USER)).getConfirmationToken();

        if (token != null) {
            MvcResult result = mockMvc.perform(
                    get("/confirm-account")
                            .param("token", token))
                    .andExpect(status().isOk())
                    .andReturn();

            String response = result.getResponse().getContentAsString();
            assertTrue(response.contains("true"));
            assertTrue(response.contains("account verified"));
        }
    }

    @Test
    void confirmation_with_invalid_token() throws Exception {
        String token = service.findToken(service.findUser(TestService.USER_EMAIL)).getConfirmationToken();

        if (token != null) {
            MvcResult result = mockMvc.perform(
                    get("/confirm-account")
                            .param("token", token))
                    .andExpect(status().isOk())
                    .andReturn();

            String response = result.getResponse().getContentAsString();
            assertTrue(response.contains("false"));
            assertTrue(response.contains("This token is broken "));
        }
    }

}
