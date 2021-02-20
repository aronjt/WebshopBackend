package org.progmatic.webshop.config;

import org.progmatic.webshop.configurators.DataLoader;
import org.progmatic.webshop.jpareps.*;
import org.progmatic.webshop.services.ImageService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class TestDataLoader extends DataLoader {

    public TestDataLoader(PasswordEncoder encoder, UserData userData, TypeData typeData,
                          GenderData genderData, ClothesData clothesData, StockData stockData,
                          EmailData emailData, OnlineOrderData onlineOrderData, PurchasedClothData pcData,
                          ImageData imageData, ImageService imageservice) {
        super(encoder, userData, typeData, genderData, clothesData, stockData, emailData, onlineOrderData,
                pcData, imageData, imageservice);
    }

}
