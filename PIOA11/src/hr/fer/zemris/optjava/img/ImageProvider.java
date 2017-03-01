package hr.fer.zemris.optjava.img;

import hr.fer.zemris.art.GrayScaleImage;
import hr.fer.zemris.optjava.img.provimpl.ThreadLocalImageProvider;

/**
 * Created by zac on 23.01.17..
 */
public class ImageProvider {

    private static IImageProvider imageProvider;

    static {
        imageProvider = (IImageProvider) new ThreadLocalImageProvider();
    }

    public static GrayScaleImage getImage(int width, int height) {
        return imageProvider.getImage(width, height);
    }
}
