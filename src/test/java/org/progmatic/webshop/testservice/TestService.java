package org.progmatic.webshop.testservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.progmatic.webshop.dto.OrderDto;
import org.progmatic.webshop.helpers.ImageHelper;
import org.progmatic.webshop.model.Image;
import org.progmatic.webshop.model.OnlineOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class TestService {

    @Autowired
    private ObjectMapper objectMapper;

    @PersistenceContext
    EntityManager em;

    public static final String USER_EMAIL = "ertekelek@ertek.el";

    public String createJson(Object o) {
        try {
            return objectMapper.writeValueAsString(o);
        } catch (Exception e) {
            return null;
        }
    }

    public <T> T createObject(String json, Class<T> toReturn) {
        try {
            return objectMapper.readValue(json, toReturn);
        } catch (Exception e) {
            return null;
        }
    }

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

    public long getOneOrderId() {
        List<OnlineOrder> orders = em.createQuery("SELECT o FROM OnlineOrder o", OnlineOrder.class)
                .getResultList();
        if (orders.size() > 0) {
            return orders.get(0).getId();
        }
        return -1;
    }

    public OrderDto createOrder() {
        OrderDto order = new OrderDto();
        order.setTotalPrice(39.99f);
        order.setPurchasedClothesList(new ArrayList<>());
        return order;
    }

}
