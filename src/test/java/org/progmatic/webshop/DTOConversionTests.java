package org.progmatic.webshop;


import org.dozer.DozerBeanMapper;
import org.junit.jupiter.api.BeforeEach;
import org.progmatic.webshop.model.*;


public class DTOConversionTests {

    DozerBeanMapper mapper;
    private User testUser;


    @BeforeEach
    public void before() throws Exception {
        mapper = new DozerBeanMapper();
        testUser = new org.progmatic.webshop.model.User();
        testUser.setId(3);
        testUser.setFirstName("testFName");
        testUser.setLastName("testLName");
        testUser.setUsername("test@test.hu");
        testUser.setUserRole("ROLE_USER");
        //testUser.setCreationTime(LocalDateTime.now());

    }


    /*@Test
    public void givenUserConvertToUserDTO_thenCorrect() {

        UserDto dto = mapper.map(testUser, UserDto.class);

        Assertions.assertEquals(dto.getId(), 3);
        Assertions.assertEquals(dto.getFirstName(), "testFName");
        Assertions.assertEquals(dto.getLastName(), "testLName");
        Assertions.assertEquals(dto.getUserRole(), "ROLE_USER");
        Assertions.assertEquals(dto.getEmail(), "test@test.hu");
    }

    @Test
    public void givenUserDTOConvertToUser_thenCorrect() {
        UserDto dto = new UserDto();
        dto.setId(3);
        dto.setEmail("test@test.hu");
        dto.setUserRole("ROLE_USER");
        User user = mapper.map(dto, User.class);

        Assertions.assertEquals(user.getId(), 3);
        Assertions.assertEquals(user.getUsername(), "test@test.hu");
        Assertions.assertEquals(user.getUserRole(), "ROLE_USER");
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

    @Test
    public void givenOrderObject_whenConvertToOrderDTO_thenCorrect() {
        OnlineOrder order = new OnlineOrder();
        order.setId(3);
        order.setTotalPrice(10000);
        order.setUser(testUser);
        order.setFinish(false);
        //order.setCreationTime(LocalDateTime.of(2000, 1, 1, 11, 11));
        OrderDto dto = mapper.map(order, OrderDto.class);

        Assertions.assertEquals(dto.getId(), 3);
        Assertions.assertEquals(dto.isFinish(), false);
        Assertions.assertEquals(dto.getTotalPrice(), 10000);
        Assertions.assertEquals(dto.getUser(), testUser);
    }


    @Test
    public void givenOrderDTOObject_whenConvertToOrder_thenCorrect() {
       OrderDto dto = new OrderDto();
        dto.setId(3);
        dto.setTotalPrice(10000);
        dto.setUser(testUser);
        dto.setFinish(false);
        OnlineOrder order = mapper.map(dto, OnlineOrder.class);

        Assertions.assertEquals(order.getId(), 3);
        Assertions.assertEquals(order.isFinish(), false);
        Assertions.assertEquals(order.getTotalPrice(), 10000);
        Assertions.assertEquals(order.getUser(), testUser);
    }*/


}
