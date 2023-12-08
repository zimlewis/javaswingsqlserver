package com.zimlewis;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ByteToImage {
    public static Image convertBytesToImage(byte[] imageData) {
        Image image = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(imageData);
            BufferedImage bufferedImage = ImageIO.read(bis);
            image = bufferedImage;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }
}
