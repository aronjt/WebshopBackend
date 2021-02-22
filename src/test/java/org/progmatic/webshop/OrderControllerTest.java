package org.progmatic.webshop;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.progmatic.webshop.dto.OrderDto;
import org.progmatic.webshop.helpers.EmailSenderHelper;
import org.progmatic.webshop.model.OnlineOrder;
import org.progmatic.webshop.returnmodel.Feedback;
import org.progmatic.webshop.returnmodel.Message;
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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
    private ObjectMapper objectMapper;

    @PersistenceContext
    EntityManager em;

    final String USER_EMAIL = "ertekelek@ertek.el";

    @Test
    void send_order_without_authentication() throws Exception {
        String json = objectMapper.writeValueAsString(createOrder());
        mockMvc.perform(
                post("/orders")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is(302))
                .andReturn();
    }

    @Test
    @WithUserDetails(USER_EMAIL)
    void send_order_with_authentication() throws Exception {
        String json = objectMapper.writeValueAsString(createOrder());
        mockMvc.perform(
                post("/orders")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @WithUserDetails(EmailSenderHelper.ADMIN_EMAIL_ADDRESS)
    void get_all_orders_as_admin() throws Exception {
        mockMvc.perform(
                get("/orders"))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @WithUserDetails(USER_EMAIL)
    void get_all_orders_as_user() throws Exception {
        mockMvc.perform(
                get("/orders"))
                .andExpect(status().is(403))
                .andReturn();
    }

    @Test
    @WithUserDetails(EmailSenderHelper.ADMIN_EMAIL_ADDRESS)
    void get_not_existing_order_as_admin() throws Exception {
        MvcResult result = mockMvc.perform(
                get("/orders/{id}", 1))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        Message fb = objectMapper.readValue(response, Message.class);
        assertFalse(fb.isSuccess());
        assertEquals("order cannot be found", fb.getMessage());
    }

    @Test
    @WithUserDetails(USER_EMAIL)
    void get_not_existing_order_as_user() throws Exception {
        mockMvc.perform(
                get("/orders/{id}", 1))
                .andExpect(status().is(403))
                .andReturn();
    }

    @Test
    @WithUserDetails(EmailSenderHelper.ADMIN_EMAIL_ADDRESS)
    void get_existing_order_as_admin() throws Exception {
        long id = getOrderId();
        MvcResult result = mockMvc.perform(
                get("/orders/{id}", id))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        assertTrue(response.contains("totalPrice"));
    }

    @Test
    @WithUserDetails(EmailSenderHelper.ADMIN_EMAIL_ADDRESS)
    void finish_order_as_admin() throws Exception {
        long id = getOrderId();

        mockMvc.perform(
                put("/orders/{id}", id)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @WithUserDetails(USER_EMAIL)
    void finish_order_as_user() throws Exception {
        mockMvc.perform(
                put("/orders/{id}", 1)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().is(403))
                .andReturn();
    }

    private long getOrderId() {
        List<OnlineOrder> orders = em.createQuery("SELECT o FROM OnlineOrder o", OnlineOrder.class)
                .getResultList();
        if (orders.size() > 0) {
            return orders.get(0).getId();
        }
        return 0;
    }

    private OrderDto createOrder() {
        OrderDto order = new OrderDto();
        order.setTotalPrice(39.99f);
        order.setPurchasedClothesList(new ArrayList<>());
        return order;
    }

}
