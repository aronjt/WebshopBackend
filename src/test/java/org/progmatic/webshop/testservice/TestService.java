package org.progmatic.webshop.testservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.progmatic.webshop.helpers.ImageHelper;
import org.progmatic.webshop.model.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;

@Service
public class TestService {

    @Autowired
    private ObjectMapper objectMapper;

    @PersistenceContext
    EntityManager em;

    public long getOneImageId() {
        List<Image> images = em.createQuery("SELECT i FROM Image i", Image.class).getResultList();
        if (images.size() > 0) {
            return images.get(0).getId();
        }
        return -1;
    }

    public MockMultipartFile loadImage() {
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
