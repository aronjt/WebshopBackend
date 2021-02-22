package org.progmatic.webshop;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.progmatic.webshop.dto.RegisterUserDto;
import org.progmatic.webshop.helpers.EmailSenderHelper;
import org.progmatic.webshop.model.OnlineOrder;
import org.progmatic.webshop.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @PersistenceContext
    EntityManager em;

    final String USER_EMAIL = "ertekelek@ertek.el";

    @Test
    void testCsrf() throws Exception{
        MvcResult mvcResult = mockMvc.perform(get("/csrf"))
                .andExpect(status().isOk())
                .andReturn();
        String csrfToken = mvcResult.getResponse().getContentAsString();
        assertNotNull(csrfToken);
    }

    @Test
    void login_without_csrf() throws Exception {
        String username = EmailSenderHelper.ADMIN_EMAIL_ADDRESS;
        mockMvc.perform(
                post("/login")
                        .contentType("multipart/form-data")
                        .content("username:" + username + "\n" +
                                "password:admin"))
                .andExpect(status().is(403));
    }

    @Test
    void login_as_admin() throws Exception {
        mockMvc.perform(
                SecurityMockMvcRequestBuilders.formLogin().user(EmailSenderHelper.ADMIN_EMAIL_ADDRESS).password("admin"))
                .andExpect(SecurityMockMvcResultMatchers.authenticated());
    }

    @Test
    void login_with_invalid_credentials() throws Exception {
        mockMvc.perform(
                SecurityMockMvcRequestBuilders.formLogin().user("admin").password("aaa"))
                .andExpect(status().is(302));

        mockMvc.perform(
                SecurityMockMvcRequestBuilders.formLogin().user("aaa").password("aaa"))
                .andExpect(status().is(302));
    }

    @Test
    void get_all_users() throws Exception {
        MvcResult result = mockMvc.perform(
                get("/users"))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @WithUserDetails("ertekelek@ertek.el")
    void get_logged_in_user() throws Exception {
        mockMvc.perform(
                get("/user"))
                .andExpect(status().isOk())
                .andReturn();
    }

    /* TODO
        still null pointer
    */
    /* should fix
    @Test
    void null_pointer() throws Exception {
        mockMvc.perform(
                get("/user"))
                .andExpect(status().isOk())
                .andReturn();
    }*/

    @Test
    void get_one_user() throws Exception {
        long id = getOneUser();
        if (id != 0) {
            MvcResult result = mockMvc.perform(
                    get("/users/{id}", id))
                    .andExpect(status().isOk())
                    .andReturn();
            String response = result.getResponse().getContentAsString();
            assertTrue(response.contains("email"));
        }
    }

    @Test
    void get_user_orders_if_exist() throws Exception {
        long id = getUserWithOrder();
        MvcResult result = mockMvc.perform(
                get("/user/order/{id}", id))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        assertTrue(response.contains("true"));
    }

    @Test
    void get_user_orders_if_not_exist() throws Exception {
        MvcResult result = mockMvc.perform(
                get("/user/order/{id}", 666))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        assertTrue(response.contains("Don't have any orders yet"));
    }

    @Test
    @WithUserDetails(USER_EMAIL)
    void edit_user_data() throws Exception {
        String json = objectMapper.writeValueAsString(userForEditUserTest());
        MvcResult result = mockMvc.perform(
                put("/user")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        assertTrue(response.contains("Successfully changed"));
    }

    private long getOneUser() {
        List<User> users = em.createQuery("SELECT u FROM User u", User.class).getResultList();
        if (users.size() > 0) {
            return users.get(0).getId();
        }
        return 0;
    }

    private long getUserWithOrder() {
        List<OnlineOrder> orders = em.createQuery("SELECT o FROM OnlineOrder o", OnlineOrder.class).getResultList();
        if (orders.size() > 0) {
            return orders.get(0).getUser().getId();
        }
        return 0;
    }

    private RegisterUserDto userForEditUserTest() {
        RegisterUserDto user = new RegisterUserDto();
        user.setFirstName("Erik");
        user.setLastName("Kis M.");
        user.setCountry("Ország");
        user.setZipcode(6969);
        user.setCity("Város");
        user.setAddress("Cím út 1.");
        user.setPhoneNumber("111-11-11");
        return user;
    }

}
