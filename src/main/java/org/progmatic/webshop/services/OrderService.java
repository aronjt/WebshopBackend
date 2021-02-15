package org.progmatic.webshop.services;

import org.progmatic.webshop.dto.FeedbackDto;
import org.progmatic.webshop.dto.OrderDto;
import org.progmatic.webshop.dto.PurchasedClothDto;
import org.progmatic.webshop.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private static final Logger LOG = LoggerFactory.getLogger(OrderService.class);

    @PersistenceContext
    EntityManager em;

    @Transactional
    public OrderDto getOneOrder(long id) {
        OnlineOrder order = em.find(OnlineOrder.class, id);
        if (order != null) {
            LOG.debug("order with id {} found, for user {} with {} purchased cloth(es)",
                    id, order.getUser().getEmail(), order.getPurchasedClothesList().size());
            return new OrderDto(order);
        }
        return new OrderDto();
    }

    @Transactional
    public List<OrderDto> getAllOrders() {
        List<OnlineOrder> orders = em.createQuery(
                "SELECT o FROM OnlineOrder o", OnlineOrder.class
        ).getResultList();

        List<OrderDto> orderDtos = new ArrayList<>();
        for (OnlineOrder o : orders) {
            orderDtos.add(new OrderDto(o));
        }

        LOG.info("all orders founded: {} orders in list",
                orders.size());

        return orderDtos;
    }

    /* TODO
        how to do dis? only change field "isFinish" from false to true?
     */
    /*@Transactional
    public OrderDto changeOrder(long id, OnlineOrder newOrder) {
        OnlineOrder oldOlder = em.find(OnlineOrder.class, id);
        LOG.info("old older with id {} was: {}, {}, {}",
                id, oldOlder.getPurchasedClothesList().size(), oldOlder.getTotalPrice(), oldOlder.isFinish());
        oldOlder.setPurchasedClothesList(newOrder.getPurchasedClothesList());
        oldOlder.setTotalPrice(sumTotalPrice(oldOlder.getPurchasedClothesList()));
        oldOlder.setFinish(newOrder.isFinish());
        LOG.info("new older with id {} is now: {}, {}, {}",
                id, oldOlder.getPurchasedClothesList().size(), oldOlder.getTotalPrice(), oldOlder.isFinish());
        return new OrderDto(oldOlder);
    }*/

    @Transactional
    public FeedbackDto finishOrder(long id) {
        OnlineOrder order = em.find(OnlineOrder.class, id);

        if (order.isFinish()) {
            LOG.info("order with id {} is already finished", id);
            return new FeedbackDto(id, "Order is already finished.");
        }

        order.setFinish(true);

        LOG.info("order with id {} is now finished", id);

        return new FeedbackDto(id, "Successfully finished the order.");
    }

    @Transactional
    public OrderDto sendOrder(List<PurchasedClothDto> clothes) {
        User user = getLoggedInUser();
        if (user != null) {
            OnlineOrder order = createNewOrder(user);

            addPurchasedClothesToDBAndToOrder(clothes, order);
            order.setTotalPrice(sumTotalPrice(order.getPurchasedClothesList()));
            changeQuantitiesInStock(order.getPurchasedClothesList());

            LOG.info("new order created, list size {}, username {}, total price {}",
                    order.getPurchasedClothesList().size(), order.getUser().getUsername(), order.getCreationTime());

            em.persist(order);

            return new OrderDto(order);
        }

        return null;
    }

    private float sumTotalPrice(List<PurchasedClothes> clothes) {
        float sum = 0;
        for (PurchasedClothes c : clothes) {
            sum += (c.getQuantity() * c.getClothes().getPrice());
        }
        return sum;
    }

    private User getLoggedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            Object principal = auth.getPrincipal();
            if (principal instanceof User) {
                return (User) principal;
            }
        }
        return null;
    }

    private OnlineOrder createNewOrder(User user) {
        OnlineOrder order = new OnlineOrder();
        order.setUser(user);
        order.setFinish(false);
        order.setPurchasedClothesList(new ArrayList<>());
        return order;
    }

    @Transactional
    public void addPurchasedClothesToDBAndToOrder(List<PurchasedClothDto> clothes, OnlineOrder order) {
        for (PurchasedClothDto c : clothes) {
            PurchasedClothes newC = new PurchasedClothes(c);
            newC.setOnlineOrder(order);
            order.getPurchasedClothesList().add(newC);
            em.persist(newC);
        }
    }

    @Transactional
    public void changeQuantitiesInStock(List<PurchasedClothes> clothes) {
        for (PurchasedClothes c : clothes) {
            try {
                Stock stock = em.createQuery("SELECT s FROM Stock s WHERE s.clothes = :cloth", Stock.class)
                        .setParameter("cloth", c)
                        .getSingleResult();
                stock.setQuantity(stock.getQuantity() - c.getQuantity());
            } catch (NoResultException e) {
                LOG.warn("cloth with id {} not found in stock",
                        c.getId());
            }
        }
    }

    @Transactional
    public byte[] getImage(String filepath) {
        try {
            BufferedImage img = ImageIO.read(new File(filepath));
            WritableRaster raster = img.getRaster();
            DataBufferByte data = (DataBufferByte) raster.getDataBuffer();
            LOG.info("image is now a byte array, its length: {}", data.getData().length);
            Image toSave = new Image();
            toSave.setData(data.getData());
            toSave.setName("test_img");
            em.persist(toSave);
            LOG.info("image is now in the database with id {}", toSave.getId());
            return data.getData();
        } catch (Exception e) {
            LOG.info("cannot get image because of an exception: {}", e.getMessage());
            return null;
        }
    }

}
