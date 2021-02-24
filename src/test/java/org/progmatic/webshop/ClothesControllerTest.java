package org.progmatic.webshop;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.progmatic.webshop.helpers.ClothDataHelper;
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
class ClothesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestService service;

    /*
    put /clothes/{id}
        missing
     */

    @Test
    void getAllClothes() throws Exception {
        MvcResult result = mockMvc.perform(get("/clothes"))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        assertTrue(response.contains("true"));
    }

    @Test
    void get_one_clothes() throws Exception {
        long id = service.getOneClothId();
        if (id > -1) {
            MvcResult result = mockMvc.perform(
                    get("/clothes/{id}", id))
                    .andExpect(status().isOk())
                    .andReturn();
            String response = result.getResponse().getContentAsString();
            assertTrue(response.contains("true"));
            assertTrue(response.contains("name"));
        }
    }

    @Test
    void get_clothes_with_given_gender() throws Exception {
        MvcResult result = mockMvc.perform(
                get("/clothes")
                    .param("gender", ClothDataHelper.GENDER_FEMALE))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        assertTrue(response.contains("true"));
        assertTrue(response.contains("name"));
    }

    @Test
    void get_one_clothes_from_stock() throws Exception {
        long id = service.getOneClothId();

        if (id > -1) {
            MvcResult result = mockMvc.perform(
                    get("/stock/{id}", id))
                    .andExpect(status().isOk())
                    .andReturn();
            String response = result.getResponse().getContentAsString();
            assertTrue(response.contains("true"));
        }
    }

    /* TODO
        fix addNewCloth method in ClothesController
            ClothDto does not contain TYPE and GENDER, but should (I think)
     */
    @Test
    void add_new_clothes() throws Exception {
        String json = service.createJson(service.createClothes());
        if (json != null) {
            MvcResult result = mockMvc.perform(
                    post("/clothes")
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isOk())
                    .andReturn();
            String response = result.getResponse().getContentAsString();
            assertTrue(response.contains("true"));
            assertTrue(response.contains("Cloth successfully added"));
        }
    }

    @Test
    void filter_clothes_with_null_filter() throws Exception {
        String json = service.createJson(service.createNullFilter());
        if (json != null) {
            MvcResult result = mockMvc.perform(
                    get("/clothes/filter")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isOk())
                    .andReturn();
            String response = result.getResponse().getContentAsString();
            assertTrue(response.contains("true"));
        }
    }

    @Test
    void filter_clothes_with_some_filter_data() throws Exception {
        String json = service.createJson(service.createFilterWithSomeData());
        if (json != null) {
            MvcResult result = mockMvc.perform(
                    get("/clothes/filter")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isOk())
                    .andReturn();
            String response = result.getResponse().getContentAsString();
            assertTrue(response.contains("true"));
        }
    }

    @Test
    void filter_clothes_with_full_filter_data() throws Exception {
        String json = service.createJson(service.createFilterWithAllData());
        if (json != null) {
            MvcResult result = mockMvc.perform(
                    get("/clothes/filter")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isOk())
                    .andReturn();
            String response = result.getResponse().getContentAsString();
            assertTrue(response.contains("true"));
        }
    }

}
