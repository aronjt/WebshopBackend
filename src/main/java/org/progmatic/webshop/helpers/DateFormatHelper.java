package org.progmatic.webshop.helpers;

/**
 * A helper class that contains default values for formatting dates. Should be used for standardization.<br>
 * Here can be found as string:<br>
 *     <ul>
 *         <li>date format</li>
 *         <li>datetime format</li>
 *     </ul>
 * Any changes in the existing fields may occur problems, if the database already contains data and it is not updated,
 * but new fields can be written anytime.
 */
public class DateFormatHelper {

    public static final String DATE_FORMAT = "yyyy.MM.dd";
    public static final String DATE_TIME_FORMAT = "yyyy.MM.dd HH:mm";

}
