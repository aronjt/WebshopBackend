/*
package org.progmatic.webshop;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class SearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void get_all_genders() throws Exception {
        mockMvc.perform(
                get("/genders"))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void get_all_types() throws Exception {
        mockMvc.perform(
                get("/types"))
                .andExpect(status().isOk())
                .andReturn();
    }

}*/
