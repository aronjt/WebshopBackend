package org.progmatic.webshop;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.progmatic.webshop.helpers.ImageHelper;
import org.progmatic.webshop.model.Image;
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
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class ImageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @PersistenceContext
    EntityManager em;

    @Test
    void get_one_image() throws Exception {
        long id = getOneImageId();
        if (id != 0) {
            mockMvc.perform(
                    get("/images/{id}", id))
                    .andExpect(status().isOk())
                    .andReturn();
        }
    }

    @Test
    void send_image() throws Exception {
        MockMultipartFile file = loadImage();
        if (file != null) {
            mockMvc.perform(
                    MockMvcRequestBuilders.multipart("/image")
                        .file(file)
                    .with(SecurityMockMvcRequestPostProcessors.csrf()))
                    .andExpect(status().isOk())
                    .andReturn();
        }
    }

    private long getOneImageId() {
        List<Image> images = em.createQuery("SELECT i FROM Image i", Image.class).getResultList();
        if (images.size() > 0) {
            return images.get(0).getId();
        }
        return 0;
    }

    private MockMultipartFile loadImage() {
        try {
            File file = new File("src/main/resources/images/unisex.png");
            FileInputStream input = new FileInputStream(file);
            return new MockMultipartFile("image",
                    "unisex", ImageHelper.PNG, IOUtils.toByteArray(input));
        } catch (Exception e) {
            return null;
        }

    }

}