package org.progmatic.webshop;


import org.dozer.DozerBeanMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.progmatic.webshop.dto.UserDto;
import org.progmatic.webshop.model.User;


public class DTOConversionTests {

    DozerBeanMapper mapper;

    @BeforeEach
    public void before() throws Exception {
        mapper = new DozerBeanMapper();
    }


    @Test
    public void givenSourceObjectAndDestClass_whenMapsSameNameFieldsCorrectly_thenCorrect() {
        User user = new User();
        user.setId(3);
        user.setUsername("test");
        user.setEmail("test@test.hu");
        user.setUserRole("ROLE_USER");
        UserDto dto = mapper.map(user, UserDto.class);

        Assertions.assertEquals(dto.getId(), 3);
        Assertions.assertEquals(dto.getUsername(), "test");
        Assertions.assertEquals(dto.getUserRole(), "ROLE_USER");
        Assertions.assertEquals(dto.getEmail(), "test@test.hu");
    }

}
