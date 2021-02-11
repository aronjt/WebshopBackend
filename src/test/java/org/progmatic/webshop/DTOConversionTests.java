package org.progmatic.webshop;


import org.dozer.DozerBeanMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.progmatic.webshop.controllers.ClothesController;
import org.progmatic.webshop.dto.ClothDto;
import org.progmatic.webshop.dto.UserDto;
import org.progmatic.webshop.helpers.ClothDataHelper;
import org.progmatic.webshop.model.Clothes;
import org.progmatic.webshop.model.Gender;
import org.progmatic.webshop.model.Type;
import org.progmatic.webshop.model.User;


public class DTOConversionTests {

    DozerBeanMapper mapper;

    @BeforeEach
    public void before() throws Exception {
        mapper = new DozerBeanMapper();
    }


    @Test
    public void givenUserConvertToUserDTO_thenCorrect() {
        User user = new User();
        user.setId(3);
       // user.setUsername("test");
        user.setFirstName("test");
        user.setLastName("test");
        user.setEmail("test@test.hu");
        user.setUserRole("ROLE_USER");
        UserDto dto = mapper.map(user, UserDto.class);

        Assertions.assertEquals(dto.getId(), 3);
        Assertions.assertEquals(dto.getUsername(), "test");
        Assertions.assertEquals(dto.getUserRole(), "ROLE_USER");
        Assertions.assertEquals(dto.getEmail(), "test@test.hu");
    }

    @Test
    public void givenUserDTOConvertToUser_thenCorrect() {
        UserDto dto = new UserDto();
        dto.setId(3);
        dto.setUsername("test");
        dto.setEmail("test@test.hu");
        dto.setUserRole("ROLE_USER");
        User user = mapper.map(dto, User.class);

        Assertions.assertEquals(user.getId(), 3);
        Assertions.assertEquals(user.getUsername(), "test");
        Assertions.assertEquals(user.getUserRole(), "ROLE_USER");
        Assertions.assertEquals(user.getUsername(), "test@test.hu");
    }

    @Test
    public void givenClothObject_whenConvertToClothDTO_thenCorrect() {
        Clothes cloth = new Clothes();
        cloth.setId(3);
        cloth.setName("Leather Pegged Pants");
        cloth.setDetails("Model is 5'9\" wearing Size XS TallAnd without further ado, we give you our finest Shopify Theme FOXic! " +
                "It is a subtle, complex and yet an extremely easy to use template for anyone, who wants to create own website in ANY area of expertise.");
        cloth.setPrice(180.00f);
        cloth.setColor("red");
        ClothDto dto = mapper.map(cloth, ClothDto.class);

        Assertions.assertEquals(dto.getId(), 3);
        Assertions.assertEquals(dto.getName(), "Leather Pegged Pants");
        Assertions.assertEquals(dto.getDetails(), "Model is 5'9\" wearing Size XS TallAnd without further ado, we give you our finest Shopify Theme FOXic! " +
                "It is a subtle, complex and yet an extremely easy to use template for anyone, who wants to create own website in ANY area of expertise.");
        Assertions.assertEquals(dto.getPrice(), 180.00f);
        Assertions.assertEquals(dto.getColor(), "red");
    }


    @Test
    public void givenClothDTOObject_whenConvertToCloth_thenCorrect() {
        ClothDto dto = new ClothDto();
        dto.setId(3);
        dto.setName("Leather Pegged Pants");
        dto.setDetails("Model is 5'9\" wearing Size XS TallAnd without further ado, we give you our finest Shopify Theme FOXic! " +
                "It is a subtle, complex and yet an extremely easy to use template for anyone, who wants to create own website in ANY area of expertise.");
        dto.setPrice(180.00f);
        dto.setColor("red");
        Clothes cloth = mapper.map(dto, Clothes.class);

        Assertions.assertEquals(cloth.getId(), 3);
        Assertions.assertEquals(cloth.getName(), "Leather Pegged Pants");
        Assertions.assertEquals(cloth.getDetails(), "Model is 5'9\" wearing Size XS TallAnd without further ado, we give you our finest Shopify Theme FOXic! " +
                "It is a subtle, complex and yet an extremely easy to use template for anyone, who wants to create own website in ANY area of expertise.");
        Assertions.assertEquals(cloth.getPrice(), 180.00f);
        Assertions.assertEquals(cloth.getColor(), "red");
    }


}
