package org.progmatic.webshop.services;

import org.progmatic.webshop.dto.RegisterUserDto;
import org.progmatic.webshop.dto.UserDto;
import org.progmatic.webshop.model.User;
import org.progmatic.webshop.returnmodel.Feedback;
import org.progmatic.webshop.returnmodel.ListResult;
import org.progmatic.webshop.returnmodel.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.UUID;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private static final Logger LOG = LoggerFactory.getLogger(MyUserDetailsService.class);

    @PersistenceContext
    EntityManager em;

    PasswordEncoder passwordEncoder;

    @Autowired
    public MyUserDetailsService(@Qualifier("passwordEncoder") PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LOG.info("User loaded by username");
        return em.createQuery("select u from User u where u.username = :username", User.class)
                .setParameter("username", username)
                .getSingleResult();
    }

    public boolean userExists(String username) {
        try {
            User user = em.createQuery("select u from User u where u.username = :username", User.class)
                    .setParameter("username", username)
                    .getSingleResult();
            LOG.info("User already exists");
            return true;
        }
        catch (NoResultException ex){
            LOG.info("User not exists");
            return false;
        }
    }

    @Transactional
    public ListResult<UserDto> listAllUsers() {
        List<User> users = em.createQuery("SELECT u FROM User u", User.class).getResultList();
        ListResult<UserDto> usersDto = new ListResult<>();
        for (User user : users) {
            usersDto.getList().add(new UserDto(user));
        }
        LOG.info("all users found: {} users in list", usersDto.getList().size());
        return usersDto;
    }

    @Transactional
    public void createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        em.persist(user);
        LOG.info("User created, email: {}", user.getUsername());
    }

    @Transactional
    public void createFakeUser(RegisterUserDto user) {
        String randomString = UUID.randomUUID().toString();
        int index = randomString.indexOf('-');
        user.setPassword(randomString.substring(0, index));
        em.persist(user);
        LOG.info("Fake user created");
    }

    public User getLoggedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            Object principal = auth.getPrincipal();
            if (principal instanceof User) {
                LOG.info("User is logged in");
                return (User) principal;
            }
        }
        LOG.info("User not logged in");
        return null;
    }

    @Transactional
    public User getUser(long id) {
        return em.find(User.class, id);
    }

    @Transactional
    public Feedback editUser(RegisterUserDto userDto) {
        User loggedInUser = em.find(User.class, getLoggedInUser().getId());
        loggedInUser.setFirstName(userDto.getFirstName());
        loggedInUser.setLastName(userDto.getLastName());
        loggedInUser.setCountry(userDto.getCountry());
        loggedInUser.setCity(userDto.getCity());
        loggedInUser.setAddress(userDto.getAddress());
        loggedInUser.setZipcode(userDto.getZipcode());

        return new Message(true, "Successfully changed");
    }
}
