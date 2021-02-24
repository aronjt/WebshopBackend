package org.progmatic.webshop.oauth;

import org.progmatic.webshop.helpers.UserDataHelper;
import org.progmatic.webshop.model.User;
import org.progmatic.webshop.services.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.UUID;

@Component
//@Service
public class Oauth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
@PersistenceContext
    EntityManager em;
//    @Autowired
//        MyUserDetailsService myUserDetailsService;
    //    public Oauth2LoginSuccessHandler(MyUserDetailsService myUserDetailsService) {
//    }
//@Autowired
//private CustomerServices customerService;
@Transactional
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
     CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
String email = oAuth2User.getEmail();
        System.out.println("Customers Email  " +email);
       String name = oAuth2User.getFullName();
       String firstname = oAuth2User.getName();
        System.out.println(name);
        System.out.println(firstname);

//        TODO if létezik vagy nem
//    if (!myUserDetailsService.userExists(oAuth2User.getEmail())) {
//        createFakeUser(oAuth2User);
//    }
        super.onAuthenticationSuccess(request, response, authentication);
    }
    @Transactional
    public void createFakeUser(CustomOAuth2User oAuth2User) {
        User user = new User();
        String[] s = oAuth2User.getFullName().split(" ", 2);
        user.setFirstName(s[0]);
        user.setLastName(s[1]);
        user.setUsername(oAuth2User.getEmail());  // email
        user.setUserRole(UserDataHelper.ROLE_USER);
user.setAddress("fill it please");
user.setCity("fill it please");
user.setCountry("fill it please");
user.setPhoneNumber("fill it please");
user.setZipcode(0000);
user.setEnabled(true);
        String randomString = UUID.randomUUID().toString();
        int index = randomString.indexOf('-');
        user.setPassword(randomString.substring(0, index));
        em.persist(user);
//        LOG.info("Fake user created");
        System.out.println("Fake user created");
    }
}
