package org.mskcc.data.builder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ImageProcess {
    private static final Logger LOGGER = LogManager.getLogger(ImageProcess.class);

    public void saveImage(BufferedImage imageToSave, String filePath){
        try{
            File outputFile = new File(filePath);
            ImageIO.write(imageToSave, "jpg", outputFile);
        } catch (IOException e){
            LOGGER.info("Unable to save image");
            throw new GenomicRequestException(e);
        }
    }

    public BufferedImage composeImage(String... imageFilePaths){
        int maxWidth = 0;
        int maxHeight = 0;
        List<BufferedImage> images = new LinkedList<>();
        for(String path : imageFilePaths) {
            try {
                BufferedImage image = ImageIO.read(new File(path));
                maxWidth = Math.max(maxWidth, image.getWidth());
                maxHeight = Math.max(maxHeight, image.getHeight());
                images.add(image);
            } catch (IOException e){
                LOGGER.info("Unable to buffer image");
            }
        }
        System.out.println("max height " + maxHeight + " max width " + maxWidth);
        int newWidth = maxWidth;
        int newHeight = maxHeight * images.size();
        BufferedImage composedImage = new BufferedImage(newWidth, newHeight,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = composedImage.createGraphics();
        g2.setPaint(Color.WHITE);
        g2.fillRect(0, 0, newWidth, newHeight );
        //draw image
        int currentY = 0;
        for(BufferedImage currentImage : images){
            System.out.println(currentY);
            g2.drawImage(currentImage, null, 0, currentY);
            currentY += maxHeight;

        }

        //g2.dispose();
        return composedImage;

    }

    public static void main(String[] args){
        ImageProcess ip = new ImageProcess();
        ip.saveImage(ip.composeImage("GoodDogge1.jpg", "GoodDogge2.jpg"),
                "AllGoodDogs.jpg");
    }
}
