package hr.fer.zemris.optjava.img;

import hr.fer.zemris.art.GrayScaleImage;

/**
 * Created by zac on 23.01.17..
 */
public interface IImageProvider {

    public GrayScaleImage getImage(int width, int height);

}
