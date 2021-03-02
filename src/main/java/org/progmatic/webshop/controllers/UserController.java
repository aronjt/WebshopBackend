package org.progmatic.webshop.controllers;

import org.progmatic.webshop.dto.RegisterUserDto;
import org.progmatic.webshop.dto.UserDto;
import org.progmatic.webshop.model.User;
import org.progmatic.webshop.returnmodel.Feedback;
import org.progmatic.webshop.returnmodel.ListResult;
import org.progmatic.webshop.returnmodel.Message;
import org.progmatic.webshop.returnmodel.Result;
import org.progmatic.webshop.services.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controls actions related to users.<br>
 *     Containing endpoints:
 *     <ul>
 *         <li>/users, get</li>
 *         <li>/users/{id}, get</li>
 *         <li>/user, get</li>
 *         <li>/user, put</li>
 *         <li>/user/order, get</li>
 *         <li>/login/success, get</li>
 *     </ul>
 */
@RestController
public class UserController {

    private final MyUserDetailsService userService;

    @Autowired
    public UserController(MyUserDetailsService userService) {
        this.userService = userService;
    }

    /**
     * Endpoint for getting all registered users.<br>
     *     See {@link MyUserDetailsService#listAllUsers()} for more information.
     * @return a {@link ListResult} that contains all users
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/users")
    public Feedback listAllUser(){
        return userService.listAllUsers();
    }

    /**
     * Endpoint for getting one user with the id.<br>
     *     See {@link MyUserDetailsService#getUser(long)} for more information.
     * @param id is the user's id (in the database)
     * @return a {@link ListResult} that contains the single user
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/users/{id}")
    public Feedback getUser(@PathVariable("id") long id) {
        ListResult<UserDto> userDtoList = new ListResult<>();
        userDtoList.getList().add(new UserDto(userService.getUser(id)));
        return userDtoList;
    }

    /**
     * Endpoint for getting the user who is logged in.<br>
     *     See {@link MyUserDetailsService#getLoggedInUser()} for more information.
     * @return a {@link Result} that contains the logged-in user, or a {@link Message} if no user is logged in
     */
    @GetMapping("/user")
    public Feedback getLoggedInUser() {
        RegisterUserDto registerUserDto = userService.getLoggedInRegisterDto();
        if (registerUserDto != null) {
            Result<RegisterUserDto> loggedInUser = new Result<>(registerUserDto);
            loggedInUser.setSuccess(true);
            return loggedInUser;
        }
        return new Message(false, "No user logged in");
    }

    /**
     * See {@link UserController#getLoggedInUser()}.
     * @return a {@link ListResult} that contains the logged-in user, or a {@link Message} if no user is logged in
     */
    @GetMapping("/login/success")
    public Feedback loginSuccess() {
        ListResult<UserDto> loggedInUser = new ListResult<>();
        User user = userService.getLoggedInUser();
        if (user != null) {
            loggedInUser.getList().add(new UserDto(user));
            loggedInUser.setSuccess(true);
            return loggedInUser;
        }
        return new Message(false, "No user logged in");
    }

    /**
     * Endpoint for getting all orders from the logged-in user.<br>
     *     See {@link MyUserDetailsService#getUserOrders()} for more information.
     * @return a {@link ListResult} that contains the user's orders, or a {@link Message} if no orders have been found
     */
    @GetMapping("/user/order")
    public Feedback getUsersOrders() {
        return userService.getUserOrders();

    }

    /**
     * Endpoint for changing a logged-in user's data.<br>
     *     See {@link MyUserDetailsService#editUser(RegisterUserDto)} for more information.
     * @param userDto is the user's data that should be changed
     * @return a confirm {@link Message} about the changing process
     */
    @PutMapping("/user")
    public Feedback editUser(@RequestBody RegisterUserDto userDto) {
        return userService.editUser(userDto);
    }

}
