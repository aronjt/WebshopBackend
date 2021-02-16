package org.progmatic.webshop.autodata;

import org.progmatic.webshop.helpers.ClothDataHelper;
import org.progmatic.webshop.helpers.EmailSenderHelper;
import org.progmatic.webshop.helpers.UserDataHelper;
import org.progmatic.webshop.model.*;
import org.progmatic.webshop.services.ImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataLoader implements ApplicationRunner {

    private final ImageService imageService;
    private static final Logger LOG = LoggerFactory.getLogger(DataLoader.class);

    private final PasswordEncoder encoder;

    private final UserData userData;
    private final TypeData typeData;
    private final GenderData genderData;
    private final ClothesData clothesData;
    private final StockData stockData;
    private final EmailData emailData;
    private final OnlineOrderData onlineOrderData;
    private final PurchasedClothData pcData;
    private final ImageData imageData;

    @Autowired
    public DataLoader(PasswordEncoder encoder, UserData userData, TypeData typeData, GenderData genderData,
                      ClothesData clothesData, StockData stockData, EmailData emailData, OnlineOrderData onlineOrderData,
                      PurchasedClothData pcData, ImageData imageData,ImageService imageservice) {
        this.encoder = encoder;
        this.userData = userData;
        this.typeData = typeData;
        this.genderData = genderData;
        this.clothesData = clothesData;
        this.stockData = stockData;
        this.emailData = emailData;
        this.onlineOrderData = onlineOrderData;
        this.pcData = pcData;
        this.imageData = imageData;
        this.imageService = imageservice;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        createUsers();
        createTypes();
        createGenders();
        createClothes();
        putSomeClothesIntoTheStock();
        createEmail();
        createOrder();
        createImages();
        setImagesToGenders();
    }

    public void createUsers() {
        long usersNum = userData.count();

        /* TODO
            password should be encoded
         */
        if (usersNum == 0) {
            User admin = createAdmin();
            userData.save(admin);

            LOG.info("admin added to database with email {} (creation time: {})",
                    admin.getUsername(), admin.getCreationTime());

            User user = createUser();
            userData.save(user);

            LOG.info("user added to database with email {} (creation time: {})",
                    user.getUsername(), user.getCreationTime());
        }
    }

    private User createAdmin() {
        User admin = new User();
        admin.setFirstName("Admin");
        admin.setLastName("Admin");
        admin.setPassword("MRirdatlan007");
        admin.setEmail(EmailSenderHelper.ADMIN_EMAIL_ADDRESS);
        admin.setCountry("Hungary");
        admin.setZipcode(1234);
        admin.setCity("Budapest");
        admin.setAddress("Pf. 666.");
        admin.setPhoneNumber("06 1 234 5678");
        admin.setUserRole(UserDataHelper.ROLE_ADMIN);
        return admin;
    }

    private User createUser() {
        User user = new User();
        user.setFirstName("Elek");
        user.setLastName("Érték");
        user.setPassword(encoder.encode("jelszó"));
        user.setEmail("ertekelek@ertek.el");
        user.setCountry("Óperencia");
        user.setZipcode(9999);
        user.setCity("Túlnan");
        user.setAddress("Felis út 42.");
        user.setPhoneNumber("1111111");
        user.setUserRole(UserDataHelper.ROLE_USER);
        return user;
    }

    public void createTypes() {
        long typeNum = typeData.count();

        if (typeNum == 0) {
            Type shirt = new Type();
            shirt.setType(ClothDataHelper.TYPE_TSHIRT);
            typeData.save(shirt);

            LOG.info("{} type added to database", shirt.getType());

            Type pullover = new Type();
            pullover.setType(ClothDataHelper.TYPE_PULLOVER);
            typeData.save(pullover);

            LOG.info("{} type added to database", pullover.getType());

            Type pants = new Type();
            pants.setType(ClothDataHelper.TYPE_PANTS);
            typeData.save(pants);

            LOG.info("{} type added to database", pants.getType());
        }
    }

    public void createGenders() {
        long genderNum = genderData.count();

        if (genderNum == 0) {
            Gender male = new Gender();
            male.setGender(ClothDataHelper.GENDER_MALE);
            genderData.save(male);

            LOG.info("{} gender added to database", male.getGender());

            Gender female = new Gender();
            female.setGender(ClothDataHelper.GENDER_FEMALE);
            genderData.save(female);

            LOG.info("{} gender added to database", female.getGender());

            Gender child = new Gender();
            child.setGender(ClothDataHelper.GENDER_CHILD);
            genderData.save(child);

            LOG.info("{} gender added to database", child.getGender());

            Gender unisex = new Gender();
            unisex.setGender(ClothDataHelper.GENDER_UNISEX);
            genderData.save(unisex);

            LOG.info("{} gender added to database", unisex.getGender());

        }
    }

    public void createClothes() {
        long clothNum = clothesData.count();

        if (clothNum == 0) {
            Type shirt = typeData.findByType(ClothDataHelper.TYPE_TSHIRT);
            Type pullover = typeData.findByType(ClothDataHelper.TYPE_PULLOVER);
            Type pants = typeData.findByType(ClothDataHelper.TYPE_PANTS);

            Gender male = genderData.findByGender(ClothDataHelper.GENDER_MALE);
            Gender female = genderData.findByGender(ClothDataHelper.GENDER_FEMALE);
            Gender unisex = genderData.findByGender(ClothDataHelper.GENDER_UNISEX);
            Gender child = genderData.findByGender(ClothDataHelper.GENDER_CHILD);

            createShirts(shirt, male, female, unisex);
            createPullovers(pullover, male, female, child);
            createPants(pants, male, female, unisex);
        }
    }

    private void createShirts(Type type, Gender male, Gender female, Gender unisex) {
        Clothes shirt1 = new Clothes();
        shirt1.setName("Lansketon T-Shirt");
        shirt1.setDetails("The best shirt for fans!");
        shirt1.setPrice(19.99f);
        shirt1.setColor(ClothDataHelper.COLOR_BLACK);
        shirt1.setType(type);
        shirt1.setGender(male);
        clothesData.save(shirt1);

        LOG.info("added new {} to the database with name and price: {}, {}",
                type.getType(), shirt1.getName(), shirt1.getPrice());

        Clothes shirt2 = new Clothes();
        shirt2.setName("Hello Kitty");
        shirt2.setDetails("Cutest t-shirt of the world!");
        shirt2.setPrice(14.99f);
        shirt2.setColor(ClothDataHelper.COLOR_PINK);
        shirt2.setType(type);
        shirt2.setGender(female);
        clothesData.save(shirt2);

        LOG.info("added new {} to the database with name and price: {}, {}",
                type.getType(), shirt2.getName(), shirt2.getPrice());

        Clothes shirt3 = new Clothes();
        shirt3.setName("Progmatic");
        shirt3.setDetails("Best Academy of The World");
        shirt3.setPrice(24.99f);
        shirt3.setColor(ClothDataHelper.COLOR_WHITE);
        shirt3.setType(type);
        shirt3.setGender(unisex);
        clothesData.save(shirt3);

        LOG.info("added new {} to the database with name and price: {}, {}",
                type.getType(), shirt3.getName(), shirt3.getPrice());
    }

    private void createPullovers(Type type, Gender male, Gender female, Gender child) {
        Clothes pullover1 = new Clothes();
        pullover1.setName("Jackie");
        pullover1.setDetails("Very comfy.");
        pullover1.setPrice(29.99f);
        pullover1.setColor(ClothDataHelper.COLOR_BLUE);
        pullover1.setType(type);
        pullover1.setGender(male);
        clothesData.save(pullover1);

        LOG.info("added new {} to the database with name and price: {}, {}",
                type.getType(), pullover1.getName(), pullover1.getPrice());

        Clothes pullover2 = new Clothes();
        pullover2.setName("Baby Doll");
        pullover2.setDetails("Nice and soft.");
        pullover2.setPrice(34.99f);
        pullover2.setColor(ClothDataHelper.COLOR_PINK);
        pullover2.setType(type);
        pullover2.setGender(female);
        clothesData.save(pullover2);

        LOG.info("added new {} to the database with name and price: {}, {}",
                type.getType(), pullover2.getName(), pullover2.getPrice());

        Clothes pullover3 = new Clothes();
        pullover3.setName("Mommy Little Baby");
        pullover3.setDetails("For every darling.");
        pullover3.setPrice(24.99f);
        pullover3.setColor(ClothDataHelper.COLOR_WHITE);
        pullover3.setType(type);
        pullover3.setGender(child);
        clothesData.save(pullover3);

        LOG.info("added new {} to the database with name and price: {}, {}",
                type.getType(), pullover3.getName(), pullover3.getPrice());
    }

    private void createPants(Type type, Gender male, Gender female, Gender unisex) {
        Clothes pants1 = new Clothes();
        pants1.setName("Jack's Pants");
        pants1.setDetails("You cannot wear anything better.");
        pants1.setPrice(39.99f);
        pants1.setColor(ClothDataHelper.COLOR_BLACK);
        pants1.setType(type);
        pants1.setGender(male);
        clothesData.save(pants1);

        LOG.info("added new {} to the database with name and price: {}, {}",
                type.getType(), pants1.getName(), pants1.getPrice());

        Clothes pants2 = new Clothes();
        pants2.setName("Meow");
        pants2.setDetails("For ladies only!");
        pants2.setPrice(39.99f);
        pants2.setColor(ClothDataHelper.COLOR_PINK);
        pants2.setType(type);
        pants2.setGender(female);
        clothesData.save(pants2);

        LOG.info("added new {} to the database with name and price: {}, {}",
                type.getType(), pants2.getName(), pants2.getPrice());

        Clothes pants3 = new Clothes();
        pants3.setName("Winter Wearer");
        pants3.setDetails("Cold days will be no longer cold, if you wear these pants!");
        pants3.setPrice(34.99f);
        pants3.setColor(ClothDataHelper.COLOR_GRAY);
        pants3.setType(type);
        pants3.setGender(unisex);
        clothesData.save(pants3);

        LOG.info("added new {} to the database with name and price: {}, {}",
                type.getType(), pants3.getName(), pants3.getPrice());
    }

    public void putSomeClothesIntoTheStock() {
        long stockNum = stockData.count();

        if (stockNum == 0) {
            String[] sizes = {ClothDataHelper.SIZE_S, ClothDataHelper.SIZE_M, ClothDataHelper.SIZE_L, ClothDataHelper.SIZE_XL};
            List<Clothes> clothes = clothesData.findAll();

            for (Clothes c : clothes) {
                Stock stock = new Stock();
                stock.setSize(sizes[(int)(Math.random() * sizes.length)]);
                stock.setQuantity((int)(Math.random() * 5));
                stock.setClothes(c);
                stockData.save(stock);

                LOG.info("added clothes to the stock (in database), clothId {}, size {}, quantity {}",
                        c.getId(), stock.getSize(), stock.getQuantity());
            }
        }
    }

    public void createEmail() {
      long emailNum = emailData.count();

        if (emailNum==0) {
            Email reg= registrationEmail();
            emailData.save(reg);
            LOG.info("added new email with type {}, subject {}",
                    reg.getMessageType(), reg.getSubject());
        Email shop = shoppingEmail();
            emailData.save(shop);
            LOG.info("added new email with type {}, subject {}",
                    shop.getMessageType(), shop.getSubject());
        }
    }


    private Email registrationEmail() {
        Email email = new Email();
        email.setMessageType(EmailSenderHelper.REGISTRATION);
        email.setSubject("Registration verification");
        email.setMessageText(
                "Thank you for your registration! Have a nice day!\n" +
                        "To confirm your account, please click here:");
        return email;
    }

    private Email shoppingEmail() {
        Email email = new Email();
        email.setMessageType(EmailSenderHelper.SHOPPING);
        email.setSubject("Shopping confirmation");
        email.setMessageText(
                "Thank you for your shopping! Have a nice day!");
        return email;
    }

    public void createOrder() {
        long orderNum = onlineOrderData.count();

        if (orderNum == 0) {
            User user = userData.findByEmail("ertekelek@ertek.el");

            if (user != null) {
                OnlineOrder order = new OnlineOrder();
                order.setUser(user);
                order.setFinish(false);
                List<PurchasedClothes> toBuy = putClothesToCart(order);

                float sum = 0;
                for (PurchasedClothes c : toBuy) {
                    sum += c.getClothes().getPrice();
                }
                order.setTotalPrice(sum);

                order.setPurchasedClothesList(toBuy);
                onlineOrderData.save(order);

                LOG.info("online order added to database to user {}, purchased clothes' names are: {}, {}",
                        user.getUsername(), toBuy.get(0).getClothes().getName(), toBuy.get(1).getClothes().getName());
            }

        }
    }

    private List<PurchasedClothes> putClothesToCart(OnlineOrder order) {
        Clothes cloth1 = clothesData.findByName("Lansketon T-Shirt");
        Clothes cloth2 = clothesData.findByName("Jack's Pants");

        List<PurchasedClothes> toBuy = new ArrayList<>();

        PurchasedClothes buy1 = new PurchasedClothes();
        buy1.setClothes(cloth1);
        buy1.setQuantity(1);
        buy1.setSize(ClothDataHelper.SIZE_M);
        buy1.setOnlineOrder(order);

        PurchasedClothes buy2 = new PurchasedClothes();
        buy2.setClothes(cloth2);
        buy2.setQuantity(2);
        buy2.setSize(ClothDataHelper.SIZE_XL);
        buy2.setOnlineOrder(order);

        toBuy.add(buy1);
        toBuy.add(buy2);
        pcData.save(buy1);
        pcData.save(buy2);

        return toBuy;
    }

    public void createImages() {
        long imgNum = imageData.count();

        if (imgNum == 0) {
            if (addImageToDatabase("src/main/resources/images/child_dress.jpg", ClothDataHelper.GENDER_CHILD)) {
                LOG.info("added image to database with name {}", ClothDataHelper.GENDER_CHILD);
            }
            if (addImageToDatabase("src/main/resources/images/man_dress.jpg", ClothDataHelper.GENDER_MALE)) {
                LOG.info("added image to database with name {}", ClothDataHelper.GENDER_MALE);
            }
            if (addImageToDatabase("src/main/resources/images/woman_dress.jpg", ClothDataHelper.GENDER_FEMALE)) {
                LOG.info("added image to database with name {}", ClothDataHelper.GENDER_FEMALE);
            }
            if (addImageToDatabase("src/main/resources/images/unisex.png", ClothDataHelper.GENDER_UNISEX)) {
                LOG.info("added image to database with name {}", ClothDataHelper.GENDER_UNISEX);
            }
        }
    }

    public void setImagesToGenders() {
        Gender man = genderData.findByGender(ClothDataHelper.GENDER_MALE);
        Image manImg = imageData.findByName(ClothDataHelper.GENDER_MALE);
        if (man != null && manImg == null) {
            if (man.getImage() != null) {
                man.setImage(manImg);
                genderData.save(man);
                LOG.info("added image to gender {}", man.getGender());
            }
        }

        Gender woman = genderData.findByGender(ClothDataHelper.GENDER_FEMALE);
        Image womanImg = imageData.findByName(ClothDataHelper.GENDER_FEMALE);
        if (woman != null && womanImg != null) {
            if (woman.getImage() == null) {
                woman.setImage(womanImg);
                genderData.save(woman);
                LOG.info("added image to gender {}", woman.getGender());
            }
        }

        Gender child = genderData.findByGender(ClothDataHelper.GENDER_CHILD);
        Image childImg = imageData.findByName(ClothDataHelper.GENDER_CHILD);
        if (child != null && childImg != null) {
            if (child.getImage() == null) {
                child.setImage(childImg);
                genderData.save(child);
                LOG.info("added image to gender {}", child.getGender());
            }
        }

        Gender uni = genderData.findByGender(ClothDataHelper.GENDER_UNISEX);
        Image unImg = imageData.findByName(ClothDataHelper.GENDER_UNISEX);
        if (uni != null && unImg != null) {
            if (uni.getImage() == null) {
                uni.setImage(unImg);
                genderData.save(uni);
                LOG.info("added image to gender {}", uni.getGender());
            }
        }
    }

    private boolean addImageToDatabase(String filepath, String name) {
        try {
            BufferedImage img = ImageIO.read(new File(filepath));
            WritableRaster raster = img.getRaster();
            DataBufferByte data = (DataBufferByte) raster.getDataBuffer();

            Image toSave = new Image();
            toSave.setData(imageService.compressBytes(data.getData()));
            toSave.setName(name);

            imageData.save(toSave);

            return true;
        } catch (Exception e) {
            LOG.warn("an error happened during adding image to database: {}",
                    e.getMessage());
            return false;
        }
    }

}
