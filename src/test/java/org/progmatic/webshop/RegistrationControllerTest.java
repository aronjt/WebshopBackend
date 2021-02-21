package org.progmatic.webshop;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.progmatic.webshop.dto.RegisterUserDto;
import org.progmatic.webshop.model.ConfirmationToken;
import org.progmatic.webshop.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class RegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @PersistenceContext
    EntityManager em;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void registration() throws Exception {
        String json = objectMapper.writeValueAsString(createUser());
        mockMvc.perform(
                post("/register")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void confirm_registration() throws Exception {
        String token = findToken(findUser()).getConfirmationToken();

        MvcResult result = mockMvc.perform(
                get("/confirm-account")
                    .param("token", token))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        assertTrue(response.contains("account verified"));
    }

    private RegisterUserDto createUser() {
        RegisterUserDto user = new RegisterUserDto();
        user.setFirstName("Egyed");
        user.setLastName("Mindmeg");
        user.setPassword("szilvásbukta");
        user.setEmail("mindmegegyed@gmail.com");
        user.setCountry("Valhalla");
        user.setZipcode(666);
        user.setCity("Ygdrassil");
        user.setAddress("Második ág 1.");
        user.setPhoneNumber("666-66-66");
        return user;
    }

    private ConfirmationToken findToken(User user) {
        return em.createQuery("SELECT t FROM ConfirmationToken t WHERE t.user = :user", ConfirmationToken.class)
                .setParameter("user", user)
                .getSingleResult();
    }

    private User findUser() {
        return em.createQuery("SELECT u FROM User u WHERE u.username = :uName", User.class)
                .setParameter("uName", "burkolorant@gmail.com")
                .getSingleResult();
    }

}