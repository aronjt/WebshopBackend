package org.progmatic.webshop;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.progmatic.webshop.returnmodel.Message;
import org.progmatic.webshop.testhelper.FeedbackMessageHelper;
import org.progmatic.webshop.testservice.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    TestService service;

    @Test
    void send_order_without_authentication() throws Exception {
        String json = service.createJson(service.createOrder());
        if (json != null) {
            mockMvc.perform(
                    post("/orders")
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().is(302))
                    .andReturn();
        }
    }

    @Test
    @WithUserDetails(TestService.USER_EMAIL)
    void send_order_with_authentication() throws Exception {
        String json = service.createJson(service.createOrder());
        if (json != null) {
            MvcResult result = mockMvc.perform(
                    post("/orders")
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isOk())
                    .andReturn();
            String response = result.getResponse().getContentAsString();
            assertTrue(response.contains("true"));
            assertTrue(response.contains(FeedbackMessageHelper.ORDER_POST_SUCCESS));
        }
    }

    @Test
    @WithUserDetails(TestService.ADMIN_EMAIL)
    void get_all_orders_as_admin() throws Exception {
        MvcResult result = mockMvc.perform(
                get("/orders"))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        assertTrue(response.contains("true"));
    }

    @Test
    @WithUserDetails(TestService.USER_EMAIL)
    void get_all_orders_as_user() throws Exception {
        mockMvc.perform(
                get("/orders"))
                .andExpect(status().is(403))
                .andReturn();
    }

    @Test
    @WithUserDetails(TestService.ADMIN_EMAIL)
    void get_not_existing_order_as_admin() throws Exception {
        MvcResult result = mockMvc.perform(
                get("/orders/{id}", 1))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        Message fb = service.createObject(response, Message.class);

        if (fb != null) {
            assertFalse(fb.isSuccess());
            assertEquals(FeedbackMessageHelper.ORDER_GET_NOT_FOUND, fb.getMessage());
        }
    }

    @Test
    @WithUserDetails(TestService.USER_EMAIL)
    void get_not_existing_order_as_user() throws Exception {
        mockMvc.perform(
                get("/orders/{id}", 1))
                .andExpect(status().is(403))
                .andReturn();
    }

    @Test
    @WithUserDetails(TestService.ADMIN_EMAIL)
    void get_existing_order_as_admin() throws Exception {
        long id = service.getOneOrderId();

        if (id != -1) {
            MvcResult result = mockMvc.perform(
                    get("/orders/{id}", id))
                    .andExpect(status().isOk())
                    .andReturn();
            String response = result.getResponse().getContentAsString();
            assertTrue(response.contains("true"));
            assertTrue(response.contains("totalPrice"));
        }
    }

    @Test
    @WithUserDetails(TestService.ADMIN_EMAIL)
    void finish_order_as_admin() throws Exception {
        long id = service.getOneOrderId();

        if (id != -1) {
            MvcResult result = mockMvc.perform(
                    put("/orders/{id}", id)
                            .with(SecurityMockMvcRequestPostProcessors.csrf()))
                    .andExpect(status().isOk())
                    .andReturn();
            String response = result.getResponse().getContentAsString();
            assertTrue(response.contains("true"));
            assertTrue(response.contains(FeedbackMessageHelper.ORDER_PUT_SUCCESS));
        }
    }

    @Test
    @WithUserDetails(TestService.USER_EMAIL)
    void finish_order_as_user() throws Exception {
        mockMvc.perform(
                put("/orders/{id}", 1)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().is(403))
                .andReturn();
    }

}
