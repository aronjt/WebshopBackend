package org.progmatic.webshop.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;
@Service
public class ImageService {
    private static final Logger LOG = LoggerFactory.getLogger(ImageService.class);

    @PersistenceContext
    EntityManager em;

    /* unused method
    @Transactional
    public List<Image> getAllImages() {
        List<Image> images = em.createQuery("SELECT i FROM Image i", Image.class).getResultList();
        return images;
    }
     */

    /**
     * compress the image bytes before storing it in the database
     * @param data the image's data to be compressed
     * @return the compressed data
     */
    public byte[] compressBytes(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        try {
            outputStream.close();
        } catch (IOException e) {
        }
        LOG.info("compressed image byte size is {}", outputStream.toByteArray().length);

        return outputStream.toByteArray();
    }

    /**
     * uncompress the image bytes before returning it to the angular application
     * @param data is the image's data to be decompressed
     * @return the decompressed data
     */
    public  byte[] decompressBytes(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
        } catch (IOException | DataFormatException i) {
        }
        return outputStream.toByteArray();
    }
}
