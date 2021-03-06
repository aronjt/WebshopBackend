package org.progmatic.webshop;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.progmatic.webshop.services.MyUserDetailsService;
import org.progmatic.webshop.testhelper.FeedbackMessageHelper;
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

    @Autowired
    private MyUserDetailsService userService;

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
        String username = TestService.ADMIN_EMAIL;
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
                SecurityMockMvcRequestBuilders.formLogin().user(TestService.ADMIN_EMAIL).password("admin"))
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
    @WithUserDetails(TestService.ADMIN_EMAIL)
    void get_all_users() throws Exception {
        MvcResult result = mockMvc.perform(
                get("/users"))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        assertTrue(response.contains("true"));
        assertTrue(response.contains("email"));
        assertTrue(response.contains("userRole"));
    }

    @Test
    @WithUserDetails(TestService.USER_EMAIL)
    void get_logged_in_user_if_logged_in() throws Exception {
        MvcResult result = mockMvc.perform(
                get("/user"))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        assertTrue(response.contains("true"));
        assertTrue(response.contains("email"));
        assertTrue(response.contains("address"));
    }

    @Test
    void get_logged_in_user_if_not_logged_in() throws Exception {
        MvcResult result = mockMvc.perform(
                get("/user"))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        assertTrue(response.contains("false"));
        assertTrue(response.contains(FeedbackMessageHelper.USER_NOT_LOGGED_IN));
    }

    @Test
    @WithUserDetails(TestService.ADMIN_EMAIL)
    void get_one_user() throws Exception {
        long id = service.getOneUser();
        if (id != 0) {
            MvcResult result = mockMvc.perform(
                    get("/users/{id}", id))
                    .andExpect(status().isOk())
                    .andReturn();
            String response = result.getResponse().getContentAsString();
            assertTrue(response.contains("true"));
            assertTrue(response.contains("email"));
        }
    }

    @Test
    @WithUserDetails(TestService.USER_EMAIL)
    void get_user_orders_if_exist() throws Exception {
        long id = service.getUserWithOrder();

        if (id != 0) {
            MvcResult result = mockMvc.perform(
                    get("/user/order"))
                    .andExpect(status().isOk())
                    .andReturn();
            String response = result.getResponse().getContentAsString();
            assertTrue(response.contains("true"));
            assertTrue(response.contains("totalPrice"));
        }

    }

    @Test
    @WithUserDetails(TestService.ADMIN_EMAIL)
    void get_user_orders_if_not_exist() throws Exception {
        MvcResult result = mockMvc.perform(
                get("/user/order"))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        assertTrue(response.contains("false"));
        assertTrue(response.contains(FeedbackMessageHelper.USER_ORDER_GET_NO_ORDER));
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
            assertTrue(response.contains("true"));
            assertTrue(response.contains(FeedbackMessageHelper.USER_PUT_SUCCESS));
        }
    }

    @Test
    void get_existing_user_if_user_not_exists() {
        boolean userExists = userService.userExists("kiscica@gmail.com");
        assertFalse(userExists);
    }

    @Test
    void get_existing_user_if_user_exists() {
        boolean userExists = userService.userExists(TestService.USER_EMAIL);
        assertTrue(userExists);
    }

    @Test
    void get_existing_user_if_null() {
        boolean userExists = userService.userExists(null);
        assertFalse(userExists);
    }

    @Test
    @WithUserDetails(TestService.USER_EMAIL)
    void get_login_success_page() throws Exception {
        mockMvc.perform(
                get("/login/success"))
                .andExpect(status().isOk())
                .andReturn();
    }

}
