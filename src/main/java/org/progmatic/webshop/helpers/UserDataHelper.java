package org.progmatic.webshop.helpers;

/**
 * A helper class that contains default values for authentication. Should be used for standardization.<br>
 * Here can be found as string:<br>
 *     <ul>
 *         <li>roles for users</li>
 *     </ul>
 * Any changes in the existing fields may occur problems, if the database already contains data and it is not updated,
 * but new fields can be written anytime.
 */
public class UserDataHelper {

    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_USER = "ROLE_USER";

}
