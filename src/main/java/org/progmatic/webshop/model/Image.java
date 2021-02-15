package org.progmatic.webshop.model;

import javax.persistence.*;

@Entity
@Table
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private byte[] Data;
    @Column()
    private String name;

    //image bytes can have large lengths so we specify a value
    //which is more than the default length for picByte column
//    @Column(length = 1000)
//    private byte[] picByte;
    public Image() {
    }

    public Image(String name, byte[] Data) {
        this.name = name;
        this.Data = Data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getData() {
        return Data;
    }

    public void setData(byte[] data) {
        Data = data;
    }
}

