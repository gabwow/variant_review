package org.mskcc.data.builder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ImageProcess {
    protected static class Point{
        private int x;
        private int y;
        String id = "";
        protected Point(int x, int y){
            this.x = x;
            this.y = y;
        }
        protected Point(int x, int y, String id){
            this.x = x;
            this.y = y;
            this.id = id;

        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }

    protected static class Layout{
        private int width;
        private int height;
        private List<List<Point>> layoutPositions = new ArrayList<>();

        protected Layout(int width, int height){
            this.width = width;
            this.height = height;
        }

        protected void addColumn(List<Point> imageColumn){
            layoutPositions.add(imageColumn);
        }
        //For testing
        protected int getWidth(){
            return width;
        }

        protected int getHeight(){
            return height;
        }

        protected Point getPosition(int x, int y) {
            try {
                return layoutPositions.get(x).get(y);
            } catch (IndexOutOfBoundsException e) {
                throw new GenomicRequestException("The cell is out of bounds for index " +
                        String.valueOf(x) + ", " + String.valueOf(y));
            }
        }
    }

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

    protected Point squareColumnOfIdenticalShapes(int x, int y, int count){
        if(count < 2){
            return new Point(1, count);
        }
        List<Integer> itemsInColumn = new ArrayList<>();
        for(int i = 0; i <= count; i++){
            itemsInColumn.add(0);
        }
        int columnNumber = 1;
        int maxHeight = 1;
        itemsInColumn.set(0, 1);
        for(int i = 1; i <= count; i++){
            int minColumn = 0;
            int potentialHeight = maxHeight;
            for(int j = 0; j < columnNumber; j++) {
                if(itemsInColumn.get(j) <= itemsInColumn.get(minColumn) ) {
                    minColumn = j;
                }
            }
            if(itemsInColumn.get(minColumn) + 1 > maxHeight){
                potentialHeight = itemsInColumn.get(minColumn) +1  ;
            }
            double balanceScoreWithColumnAdd = (double) (x  * (columnNumber + 1))/ (y * maxHeight);
            balanceScoreWithColumnAdd = Math.max(balanceScoreWithColumnAdd, 1/balanceScoreWithColumnAdd);
            double balanceScoreWithRowAdd = (double) (x  * (columnNumber))/ (y * potentialHeight);
            balanceScoreWithRowAdd =  Math.max(balanceScoreWithRowAdd, 1/balanceScoreWithRowAdd);
            if(balanceScoreWithColumnAdd < balanceScoreWithRowAdd){
                itemsInColumn.set(columnNumber, 1);
                columnNumber++;
            } else{
                itemsInColumn.set(minColumn, itemsInColumn.get(minColumn) + 1);
                maxHeight = potentialHeight;
            }
        }
        return new Point(columnNumber, maxHeight);
    }

    protected Layout buildLayout(List<Point> imageSizes) {
        Map<Integer, List<Point>>  imageColumnsByWidth = new HashMap<>();
        int layoutWidth = 0;
        Map<Integer, Integer> columnHeights = new HashMap<>();
        for(Point imageSize : imageSizes){
            if(!imageColumnsByWidth.containsKey(imageSize.getX())){
                imageColumnsByWidth.put(imageSize.getX(), new ArrayList<>());
                layoutWidth += imageSize.getX();
                columnHeights.put(imageSize.getX(), 0);
            }
            imageColumnsByWidth.get(imageSize.getX()).add(imageSize);
            columnHeights.computeIfPresent(imageSize.getX(), (k, v) -> v + imageSize.getY());
        }

        List<Integer> sortedWidths = new ArrayList<>(imageColumnsByWidth.keySet());
        Collections.sort(sortedWidths);
        if(sortedWidths.size() == 1) {
            Point dims =  squareColumnOfIdenticalShapes ( imageSizes.get(0).getX(),
                            imageSizes.get(0).getY(), imageSizes.size());
            int squaredCols = dims.getX();
            int squaredHeight = dims.getY();
            layoutWidth = squaredCols * imageSizes.get(0).getX();
            columnHeights = new HashMap<>();
            columnHeights.put(0, squaredHeight *  imageSizes.get(0).getY());
            sortedWidths = new ArrayList<>();
            imageColumnsByWidth = new HashMap<>();
            int key = 0;
            sortedWidths.add(key);
            imageColumnsByWidth.put(key, new ArrayList<Point>());
            for(Point image : imageSizes){
                if(imageColumnsByWidth.get(key).size() == squaredHeight){
                    key++;
                    sortedWidths.add(key);
                    imageColumnsByWidth.put(key, new ArrayList<Point>());
                }
                imageColumnsByWidth.get(key).add(image);
            }
        }
        int currentX = 0;
        int currentY = 0;
        Layout layedOutImage = new Layout(layoutWidth, Collections.max(columnHeights.values()));
        List<Point> imageAnchors = new ArrayList<>();
        for(Integer columnIndex : sortedWidths){
            for(Point currentSize : imageColumnsByWidth.get(columnIndex)){
                imageAnchors.add(new Point(currentX, currentY, currentSize.id));
                currentY += currentSize.getY();
            }
            layedOutImage.addColumn(imageAnchors);
            imageAnchors = new ArrayList<>();
            currentY = 0;
            currentX += imageColumnsByWidth.get(columnIndex).get(0).getX();
        }
        return layedOutImage;
    }

    public BufferedImage composeImage(String... imageFilePaths){
        List<Point> imageSizes = new ArrayList<>();
        Map<String, BufferedImage> fileIdToImage = new HashMap<>();
        for(String path : imageFilePaths) {
            try {
                BufferedImage image = ImageIO.read(new File(path));
                fileIdToImage.put(path, image);
                imageSizes.add(new Point(image.getWidth(), image.getHeight(), path));
            } catch (IOException e){
                LOGGER.info("Unable to buffer image");
            }
        }
        Layout composedLayout = buildLayout(imageSizes);
        BufferedImage composedImage = new BufferedImage(composedLayout.width, composedLayout.height,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = composedImage.createGraphics();
        g2.setPaint(Color.WHITE);
        g2.fillRect(0, 0, composedLayout.width, composedLayout.height);
        //draw image
        for(List<Point> imageColumn : composedLayout.layoutPositions){
            for(Point imageAnchor : imageColumn){
                g2.drawImage(fileIdToImage.get(imageAnchor.id), null, imageAnchor.getX(), imageAnchor.getY());
            }
        }

        //g2.dispose();
        return composedImage;

    }

    public static void main(String[] args){
        ImageProcess ip = new ImageProcess();

        ip.saveImage(ip.composeImage("testData/GoodDogge2.jpg",
                "testData/GoodDogge3.jpg", "testData/GoodDogge4.jpg",
                "testData/GoodDogge6.jpg", "testData/GoodDogge7.jpg"),
                "AllGoodDogs.jpg");
    }
}
