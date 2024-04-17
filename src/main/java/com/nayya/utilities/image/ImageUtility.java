package com.nayya.utilities.image;

public class ImageUtility {
    private final String testImagePath = System.getProperty("user.dir") + "/src/main/resources/images/testImage.jpg";

    public String createImage() {

        return testImagePath;
    }
}