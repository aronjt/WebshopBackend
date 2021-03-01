package org.progmatic.webshop.configurators;

import org.apache.commons.io.IOUtils;
import org.progmatic.webshop.jpareps.*;
import org.progmatic.webshop.helpers.ClothDataHelper;
import org.progmatic.webshop.helpers.EmailSenderHelper;
import org.progmatic.webshop.helpers.ImageHelper;
import org.progmatic.webshop.helpers.UserDataHelper;
import org.progmatic.webshop.jpareps.AdminData;
import org.progmatic.webshop.model.*;
import org.progmatic.webshop.services.ImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * An {@link ApplicationRunner} class to fill up the database with default data for testing and presentation
 * if the analyzed table of the database does not contain any data.<br>
 * @apiNote It may not be used when the application runs in real life.
 */
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
    private final AdminData adminData;

    @Autowired
    public DataLoader(PasswordEncoder encoder, UserData userData, TypeData typeData, GenderData genderData,
                      ClothesData clothesData, StockData stockData, EmailData emailData, OnlineOrderData onlineOrderData,
                      PurchasedClothData pcData, ImageData imageData, ImageService imageservice, AdminData adminData) {
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
        this.adminData = adminData;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        createUsers();
        createAdminData();
        createImages();
        createTypes();
        createGenders();
        createClothes();
        putSomeClothesIntoTheStock();
        createEmails();
        createOrder();
    }

    /**
     * Uploads two {@link User} entity with ROLE_ADMIN and one with ROLE_USER to the database.<br>
     *     Roles can be found in {@link UserDataHelper}.
     */
    public void createUsers() {
        long usersNum = userData.count();

        if (usersNum == 0) {
            User admin = createUser("Admin", "Admin", EmailSenderHelper.ADMIN_EMAIL_ADDRESS,
                    "admin", 1234, "Hungary", "Budapest", "Pf. 666.",
                    "06 1 234 5678", UserDataHelper.ROLE_ADMIN);
            userData.save(admin);

            LOG.debug("admin added to database with email {} (creation time: {})",
                    admin.getUsername(), admin.getCreationTime());

            User joAdmin = createUser("Jo", "King", "iamjoking@gmail.com",
                    "password", 555, "Goland", "Module", "123 Syntax Street",
                    "555 55 55", UserDataHelper.ROLE_ADMIN);
            userData.save(joAdmin);

            LOG.debug("admin added to database with email {} (creation time: {})",
                    joAdmin.getUsername(), joAdmin.getCreationTime());

            User user = createUser("Elek", "Érték", "ertekelek@ertek.el",
                    "jelszó", 9999, "Óperencia", "Túlnan", "Felis út 1.",
                    "1111111", UserDataHelper.ROLE_USER);
            userData.save(user);

            LOG.debug("user added to database with email {} (creation time: {})",
                    user.getUsername(), user.getCreationTime());
        }
    }

    /**
     * Creates a {@link User}.
     * @param firstName is the first name
     * @param lastName is the last name
     * @param email is the email address (also username)
     * @param pw is the password
     * @param zip is the zip code
     * @param country is the country
     * @param city is the city
     * @param address is the address
     * @param phone is the phone number
     * @param role is the role (comes from {@link UserDataHelper})
     * @return the created {@link User} object
     */
    private User createUser(String firstName, String lastName, String email, String pw, int zip, String country,
                            String city, String address, String phone, String role) {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPassword(encoder.encode(pw));
        user.setUsername(email);
        user.setCountry(country);
        user.setZipcode(zip);
        user.setCity(city);
        user.setAddress(address);
        user.setPhoneNumber(phone);
        user.setUserRole(role);
        user.setEnabled(true);
        return user;
    }

    /**
     * Uploads extra data for admin to the database by creating an {@link ExtraData} object.
     */
    public void createAdminData() {
        long adminNum = adminData.count();

        if (adminNum == 0) {
            ExtraData data = new ExtraData();
            data.setId(EmailSenderHelper.ID);
            data.setSecret("MRirdatlan007");
            adminData.save(data);

            LOG.debug("admin data added to database with id {}", data.getId());
        }
    }

    /**
     * Uploads three {@link Type} entities to the database:<br>
     *     <ul>
     *     <li>t-shirt</li>
     *     <li>pullover</li>
     *     <li>pants</li>
     *     </ul>
     *     <br>
     * These types can be found in {@link ClothDataHelper}.
     */
    public void createTypes() {
        long typeNum = typeData.count();

        if (typeNum == 0) {
            Type shirt = new Type();
            shirt.setType(ClothDataHelper.TYPE_TSHIRT);
            typeData.save(shirt);

            LOG.debug("{} type added to database", shirt.getType());

            Type pullover = new Type();
            pullover.setType(ClothDataHelper.TYPE_PULLOVER);
            typeData.save(pullover);

            LOG.debug("{} type added to database", pullover.getType());

            Type pants = new Type();
            pants.setType(ClothDataHelper.TYPE_PANTS);
            typeData.save(pants);

            LOG.debug("{} type added to database", pants.getType());
        }
    }

    /**
     * Uploads four {@link Gender} entities to the database:<br>
     *     <ul>
     *     <li>male</li>
     *     <li>female</li>
     *     <li>child</li>
     *     <li>unisex</li>
     *     </ul>
     *     <br>
     *     These genders can be found in {@link ClothDataHelper}.
     */
    public void createGenders() {
        long genderNum = genderData.count();

        if (genderNum == 0) {
            Image male = imageData.findByName(ClothDataHelper.GENDER_MALE);
            Image female = imageData.findByName(ClothDataHelper.GENDER_FEMALE);
            Image child = imageData.findByName(ClothDataHelper.GENDER_CHILD);
            Image unisex = imageData.findByName(ClothDataHelper.GENDER_UNISEX);

            createGender(ClothDataHelper.GENDER_MALE, male);
            createGender(ClothDataHelper.GENDER_FEMALE, female);
            createGender(ClothDataHelper.GENDER_CHILD, child);
            createGender(ClothDataHelper.GENDER_UNISEX, unisex);
        }
    }

    /**
     * Uploads a {@link Gender} entity to the database.
     * @param gender is the name of the gender comes from {@link ClothDataHelper}
     * @param image is the image for the gender comes from the database
     */
    private void createGender(String gender, Image image) {
        Gender g = new Gender();
        g.setGender(gender);
        g.setImage(image);
        genderData.save(g);
        LOG.debug("{} gender added to database with image id {}", g.getGender(), image.getId());
    }

    /**
     * Uploads nine {@link Clothes} entities to the database.<br>
     *     The clothes' information (names, details, prices) are random, but colors can be found in {@link ClothDataHelper},
     *     and types, genders and images come from the database.
     */
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

            Image femaleImg = imageData.findByName(ClothDataHelper.GENDER_FEMALE);
            Image unImg = imageData.findByName(ClothDataHelper.GENDER_UNISEX);
            Image childImg = imageData.findByName(ClothDataHelper.GENDER_CHILD);
            Image regularFitTShirt = imageData.findByName("BASIC MEDIUM WEIGHT T-SHIRT.jpg");
            Image straightTaperedJeans = imageData.findByName("STRAIGHT TAPERED JEANS.jpg");
            Image ruffledHemRibTop = imageData.findByName("RUFFLED HEM RIB TOP.jpg");
            Image overSizedSweatshirt = imageData.findByName("OVERSIZED SWEATSHIRT.jpg");
            Image contrastingZipSweatshirt = imageData.findByName("CONTRASTING ZIP SWEATSHIRT.jpg");

            createClothes("BASIC MEDIUM WEIGHT T-SHIRT", "REGULAR FIT T-SHIRT MADE WITH MERCERIZED FABRIC. ROUND RIB NECKLINE AND SHORT SLEEVES.", 17.99f,
                    ClothDataHelper.COLOR_BEIGE, shirt, male, regularFitTShirt);
            createClothes("RUFFLED HEM RIB TOP", "ROUND NECK SLEEVELESS TOP. FLOUNCED HEM.", 17.99f, ClothDataHelper.COLOR_PINK,
                    shirt, female, ruffledHemRibTop);
            createClothes("Progmatic", "Best Academy of the World!", 24.99f, ClothDataHelper.COLOR_WHITE,
                    shirt, unisex, unImg);
            createClothes("OVERSIZED SWEATSHIRT", "FULL CUT SWEATSHIRT WITH ROUND NECK AND LONG SLEEVES. RIBBED TRIM.", 39.99f, ClothDataHelper.COLOR_BLUE,
                    pullover, male, overSizedSweatshirt);
            createClothes("CONTRASTING ZIP SWEATSHIRT", "HIGH COLLAR SWEATSHIRT WITH CONTRAST ZIPPER. LONG SLEEVES WITH ELASTIC CUFFS.", 39.99f, ClothDataHelper.COLOR_BLACK,
                    pullover, female, contrastingZipSweatshirt);
            createClothes("Mommy Little Baby", "For every darling.", 24.99f, ClothDataHelper.COLOR_WHITE,
                    pullover, child, childImg);
            createClothes("STRAIGHT TAPERED JEANS", "STRAIGHT FIT JEANS. FIVE POCKETS. WASHED EFFECT. FRONT ZIP AND BUTTON CLOSURE.", 39.99f,
                    ClothDataHelper.COLOR_WHITE, pants, male, straightTaperedJeans);
            createClothes("Meow", "For ladies only!", 39.99f, ClothDataHelper.COLOR_PINK,
                    pants, female, femaleImg);
            createClothes("Winter Wearer", "Cold days will be no longer cold, if you wear these pants!", 34.99f,
                    ClothDataHelper.COLOR_GRAY, pants, unisex, unImg);
        }
    }

    /**
     * Uploads one {@link Clothes} entity to the database.
     * @param name is the name
     * @param details is a short details
     * @param price is the price
     * @param color is the color comes from {@link ClothDataHelper}
     * @param type is the type comes from the database
     * @param gender is the gender comes from the database
     * @param image is the image comes from the database
     */
    private void createClothes(String name, String details, float price, String color, Type type, Gender gender, Image image) {
        Clothes clothes = new Clothes();
        clothes.setName(name);
        clothes.setDetails(details);
        clothes.setPrice(price);
        clothes.setColor(color);
        clothes.setType(type);
        clothes.setGender(gender);
        clothes.setImage(image);
        clothesData.save(clothes);

        LOG.debug("added new {} to database with name and price: {}, {}",
                clothes.getType().getType(), clothes.getName(), clothes.getPrice());
    }

    /**
     * Uploads {@link Stock} entities to the database from the existing {@link Clothes}.<br>
     *     Every clothes from the database will be presented in the Stock table in the database
     *     with random size (comes from {@link ClothDataHelper}) and in random quantity (between 0 and 4).
     */
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

                LOG.debug("added clothes to the stock (in database), clothId {}, size {}, quantity {}",
                        c.getId(), stock.getSize(), stock.getQuantity());
            }
        }
    }

    /**
     * Uploads an {@link Email} entity to the database with type registration and one with shipping.<br>
     *     These types come from {@link EmailSenderHelper}.
     */
    public void createEmails() {
      long emailNum = emailData.count();

      if (emailNum == 0) {
          createEmail(EmailSenderHelper.REGISTRATION, "Registration verification",
                  "Thank you for your registration! Have a nice day!<br>To confirm your account, " +
                          "please click here:<br>");
          createEmail(EmailSenderHelper.SHOPPING, "Shopping confirmation",
                  "Thank you for your shopping! Have a nice day!<br>");
      }
    }

    /**
     * Uploads an {@link Email} entity to the database.
     * @param messageType is the type of the email (comes from {@link EmailSenderHelper}
     * @param subject is the subject
     * @param messageText is the text of the email
     */
    private void createEmail(String messageType, String subject, String messageText) {
        Email email = new Email();
        email.setMessageType(messageType);
        email.setSubject(subject);
        email.setMessageText(messageText);
        emailData.save(email);
        LOG.debug("added new email with type {}, subject {}",
                email.getMessageType(), email.getSubject());
    }

    /**
     * Uploads an {@link OnlineOrder} entity to the database with {@link PurchasedClothes}.<br>
     *     See {@link DataLoader#putClothesToCart(OnlineOrder)}.
     */
    public void createOrder() {
        long orderNum = onlineOrderData.count();

        if (orderNum == 0) {
            User user = userData.findByUsername("ertekelek@ertek.el");

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

                LOG.debug("online order added to database to user {}, purchased clothes' names are: {}, {}",
                        user.getUsername(), toBuy.get(0).getClothes().getName(), toBuy.get(1).getClothes().getName());
            }

        }
    }

    /**
     * Uploads two {@link PurchasedClothes} entities to the database.
     * @param order is the {@link OnlineOrder} which should contain the purchased clothes
     * @return the list of purchased clothes
     */
    private List<PurchasedClothes> putClothesToCart(OnlineOrder order) {
        Clothes cloth1 = clothesData.findByName("BASIC MEDIUM WEIGHT T-SHIRT");
        Clothes cloth2 = clothesData.findByName("Straight Tapered Jeans");

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

    /**
     * Uploads {@link Image} entities to the database.<br>
     *     Images can be found in src/main/resources/images folder.<br>
     *     This method uses {@link ImageHelper} to get correct content types of images.
     */
    public void createImages() {
        long imgNum = imageData.count();

        if (imgNum == 0) {
            String pngImg = ImageHelper.PNG;
            String jpegImg = ImageHelper.JPG;
            if (addImageToDatabase("src/main/resources/images/children_dress.jpg", ClothDataHelper.GENDER_CHILD, jpegImg)) {
                LOG.debug("added image to database with name {}", ClothDataHelper.GENDER_CHILD);
            }
            if (addImageToDatabase("src/main/resources/images/men_dress.jpeg", ClothDataHelper.GENDER_MALE, jpegImg)) {
                LOG.debug("added image to database with name {}", ClothDataHelper.GENDER_MALE);
            }
            if (addImageToDatabase("src/main/resources/images/women_dress.jpg", ClothDataHelper.GENDER_FEMALE, jpegImg)) {
                LOG.debug("added image to database with name {}", ClothDataHelper.GENDER_FEMALE);
            }
            if (addImageToDatabase("src/main/resources/images/unisex.png", ClothDataHelper.GENDER_UNISEX, pngImg)) {
                LOG.debug("added image to database with name {}", ClothDataHelper.GENDER_UNISEX);
            }
            if (addImageToDatabase("src/main/resources/images/BASIC MEDIUM WEIGHT T-SHIRT.jpg",
                    "BASIC MEDIUM WEIGHT T-SHIRT.jpg", jpegImg)) {
                LOG.debug("added image to database with name {}", "BASIC MEDIUM WEIGHT T-SHIRT");
            }
            if (addImageToDatabase("src/main/resources/images/STRAIGHT TAPERED JEANS.jpg",
                    "STRAIGHT TAPERED JEANS.jpg", jpegImg)) {
                LOG.debug("added image to database with name {}", "StraightTaperedJeans");
            }
            if (addImageToDatabase("src/main/resources/images/RUFFLED HEM RIB TOP.jpg",
                    "RUFFLED HEM RIB TOP.jpg", jpegImg)) {
                LOG.debug("added image to database with name {}", "RUFFLED HEM RIB TOP");
            }
            if (addImageToDatabase("src/main/resources/images/OVERSIZED SWEATSHIRT.jpg",
                    "OVERSIZED SWEATSHIRT.jpg", jpegImg)) {
                LOG.debug("added image to database with name {}", "OVERSIZED SWEATSHIRT.jpg");
            }
            if (addImageToDatabase("src/main/resources/images/CONTRASTING ZIP SWEATSHIRT.jpg",
                    "CONTRASTING ZIP SWEATSHIRT.jpg", jpegImg)) {
                LOG.debug("added image to database with name {}", "CONTRASTING ZIP SWEATSHIRT.jpg");
            }
        }
    }

    /**
     * Uploads an {@link Image} to the database by creating a {@link MockMultipartFile} object.
     * @param filepath is the filepath where the image can be found
     * @param name is the name that will be added to the image (must be unique)
     * @param contentType is the content type of the image (comes from {@link ImageHelper})
     * @return true if the upload was successfully, or false if any exception occurred
     */
    private boolean addImageToDatabase(String filepath, String name, String contentType) {
        try {
            File file = new File(filepath);
            FileInputStream input = new FileInputStream(file);
            MultipartFile multipartFile = new MockMultipartFile("file",
                    name, contentType, IOUtils.toByteArray(input));
            Image img = new Image(multipartFile.getOriginalFilename(), imageService.compressBytes(multipartFile.getBytes()));
            imageData.save(img);
            return true;
        } catch (Exception e) {
            LOG.warn("oops, an error happened during adding image to database: {}",
                    e.getMessage());
            return false;
        }
    }

}
