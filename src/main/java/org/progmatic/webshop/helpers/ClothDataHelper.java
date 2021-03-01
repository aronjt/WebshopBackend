package org.progmatic.webshop.helpers;

/**
 * A helper class that contains default values for clothes. Should be used for standardization.<br>
 * Here can be found as string:<br>
 *     <ul>
 *         <li>genders' names </li>
 *         <li>types' names</li>
 *         <li>sizes' names</li>
 *         <li>colors</li>
 *     </ul>
 * Any changes in the existing fields may occur problems, if the database already contains data and it is not updated,
 * but new fields can be written anytime.
 */
public class ClothDataHelper {

    public static final String GENDER_UNISEX = "unisex";
    public static final String GENDER_MALE = "male";
    public static final String GENDER_FEMALE = "female";
    public static final String GENDER_CHILD = "child";

    public static final String TYPE_TSHIRT = "t-shirt";
    public static final String TYPE_PULLOVER = "pullover";
    public static final String TYPE_PANTS = "pants";

    public static final String SIZE_S = "S";
    public static final String SIZE_M = "M";
    public static final String SIZE_L = "L";
    public static final String SIZE_XL = "XL";

    public static final String COLOR_BLACK = "BLACK";
    public static final String COLOR_WHITE = "WHITE";
    public static final String COLOR_GRAY = "GREY";
    public static final String COLOR_PINK = "PINK";
    public static final String COLOR_BLUE = "BLUE";
    public static final String COLOR_BEIGE = "BEIGE";

}
