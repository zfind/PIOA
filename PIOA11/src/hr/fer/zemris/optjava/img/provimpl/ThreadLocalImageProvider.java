package hr.fer.zemris.optjava.img.provimpl;

import hr.fer.zemris.art.GrayScaleImage;
import hr.fer.zemris.optjava.img.IImageProvider;

/**
 * Created by zac on 23.01.17..
 */
public class ThreadLocalImageProvider implements IImageProvider {

    private ThreadLocal<GrayScaleImage> threadLocal;

    public ThreadLocalImageProvider() {
        threadLocal = new ThreadLocal<>();
    }

    public GrayScaleImage getImage(int width, int height) {
        GrayScaleImage img = threadLocal.get();
        if (img != null) {
            return img;
        }
//        System.err.println("DBG: Stvaram novu sliku");
        img = new GrayScaleImage(width, height);
        threadLocal.set(img);
        return img;
    }

}
