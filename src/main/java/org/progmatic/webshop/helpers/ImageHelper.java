package org.progmatic.webshop.helpers;

/**
 * A helper class that contains default values for images. Should be used for standardization.<br>
 * Here can be found as string:<br>
 *     <ul>
 *         <li>content types</li>
 *     </ul>
 * Any changes in the existing fields may occur problems, if the database already contains data and it is not updated,
 * but new fields can be written anytime.
 */
public class ImageHelper {

    public static final String PNG = "image/png";
    public static final String JPG = "image/jpg";
    public static final String GIF = "image/gif";

}
