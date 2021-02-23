package org.progmatic.webshop;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.progmatic.webshop.helpers.ImageHelper;
import org.progmatic.webshop.model.Image;
import org.progmatic.webshop.testservice.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class ImageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    TestService service;

    @Test
    void get_one_image() throws Exception {
        long id = service.getOneImageId();
        if (id > -1) {
            mockMvc.perform(
                    get("/images/{id}", id))
                    .andExpect(status().isOk())
                    .andReturn();
        }
    }

    @Test
    void send_image() throws Exception {
        MockMultipartFile file = service.loadImage();
        if (file != null) {
            MvcResult result = mockMvc.perform(
                    MockMvcRequestBuilders.multipart("/image")
                        .file(file)
                    .with(SecurityMockMvcRequestPostProcessors.csrf()))
                    .andExpect(status().isOk())
                    .andReturn();
            String response = result.getResponse().getContentAsString();
            assertTrue(response.contains("successful image upload"));
        }
    }

}
