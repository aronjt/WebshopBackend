package org.progmatic.webshop.dto;

/**
 * DTO for {@link org.progmatic.webshop.model.Stock} entity.<br>
 *     Contains:
 *     <ul>
 *         <li>int sizeS</li>
 *         <li>int sizeM</li>
 *         <li>int sizeL</li>
 *         <li>int sizeXl</li>
 *     </ul>
 */
public class StockDto {

    private int sizeS;
    private int sizeM;
    private int sizeL;
    private int sizeXl;

    public StockDto() {
    }

    public int getSizeS() {
        return sizeS;
    }

    public void setSizeS(int sizeS) {
        this.sizeS = sizeS;
    }

    public int getSizeM() {
        return sizeM;
    }

    public void setSizeM(int sizeM) {
        this.sizeM = sizeM;
    }

    public int getSizeL() {
        return sizeL;
    }

    public void setSizeL(int sizeL) {
        this.sizeL = sizeL;
    }

    public int getSizeXl() {
        return sizeXl;
    }

    public void setSizeXl(int sizeXl) {
        this.sizeXl = sizeXl;
    }
}
