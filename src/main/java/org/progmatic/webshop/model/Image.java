package org.progmatic.webshop.model;

import javax.persistence.*;

@Entity
@Table
public class Image {
    @Id
    @GeneratedValue
    private long id;
    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private byte[] Data;
    @Column()
    private String name;

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

    public long getId() {
        return id;
    }
}

