package org.progmatic.webshop.helpers;

/**
 * A helper class that contains default values for handling emails. Should be used for standardization.<br>
 * Here can be found as string:<br>
 *     <ul>
 *         <li>types of emails</li>
 *         <li>email address of the sender</li>
 *     </ul>
 * Any changes in the existing fields may occur problems, if the database already contains data and it is not updated,
 * but new fields can be written anytime.
 */
public class EmailSenderHelper {
    public static final String REGISTRATION = "registration";
    public static final String SHOPPING = "shopping";
    public static final String PASSWORD = "new password";

    public static final String ADMIN_EMAIL_ADDRESS = "webshopmnager@gmail.com";
    public static final int ID = 1;

}
