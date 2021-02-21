package org.progmatic.webshop;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.progmatic.webshop.helpers.EmailSenderHelper;
import org.progmatic.webshop.helpers.JsonBuilder;
import org.progmatic.webshop.model.OnlineOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @PersistenceContext
    EntityManager em;

    final String USER_EMAIL = "ertekelek@ertek.el";

    @Test
    void send_order_without_authentication() throws Exception {
        String json = JsonBuilder.newBuilder()
                .add("totalPrice", 39.99f)
                .addEmptyList("purchasedClothesList")
                .build();
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
        String json = JsonBuilder.newBuilder()
                .add("totalPrice", 39.99f)
                .addEmptyList("purchasedClothesList")
                .build();
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
    void get_one_order_as_admin() throws Exception {
        mockMvc.perform(
                get("/orders/{id}", 1))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @WithUserDetails(USER_EMAIL)
    void get_one_order_as_user() throws Exception {
        mockMvc.perform(
                get("/orders/{id}", 1))
                .andExpect(status().is(403))
                .andReturn();
    }

    @Test
    @WithUserDetails(EmailSenderHelper.ADMIN_EMAIL_ADDRESS)
    void finish_order_as_admin() throws Exception {
        long id = getOrderId();
        if (id != 0) {
            mockMvc.perform(
                    put("/orders/{id}", id)
                            .with(SecurityMockMvcRequestPostProcessors.csrf()))
                    .andExpect(status().isOk())
                    .andReturn();
        }
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

}