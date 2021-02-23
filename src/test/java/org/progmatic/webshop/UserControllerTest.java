package org.progmatic.webshop;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.progmatic.webshop.helpers.EmailSenderHelper;
import org.progmatic.webshop.testservice.TestService;
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
    private TestService service;

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

    @Test
    void null_pointer() throws Exception {
        mockMvc.perform(
                get("/user"))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void get_one_user() throws Exception {
        long id = service.getOneUser();
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
        long id = service.getUserWithOrder();

        if (id != 0) {
            MvcResult result = mockMvc.perform(
                    get("/user/order/{id}", id))
                    .andExpect(status().isOk())
                    .andReturn();
            String response = result.getResponse().getContentAsString();
            assertTrue(response.contains("true"));
        }

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
    @WithUserDetails(TestService.USER_EMAIL)
    void edit_user_data() throws Exception {
        String json = service.createJson(service.userForEditUserTest());
        if (json != null) {
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
    }

}
