package hr.fer.zemris.generic.ga;

import hr.fer.zemris.art.GrayScaleImage;
import hr.fer.zemris.optjava.img.ImageProvider;

public class ImageFitnessCalculator implements IGAEvaluator<int[]> {
    private GrayScaleImage template;

    public ImageFitnessCalculator(GrayScaleImage template) {
        super();
        this.template = template;
    }

    public GrayScaleImage draw(GASolution<int[]> p) {

        GrayScaleImage image = ImageProvider.getImage(
                template.getWidth(),
                template.getHeight()
        );

        int[] pdata = p.getData();

        byte bgcol = (byte) pdata[0];

        image.clear(bgcol);

        int n = (pdata.length - 1) / 5;
        int index = 1;

        for (int i = 0; i < n; i++) {
            image.rectangle(
                    pdata[index],
                    pdata[index + 1],
                    pdata[index + 2],
                    pdata[index + 3],
                    (byte) pdata[index + 4]
            );
            index += 5;
        }
        return image;
    }

    @Override
    public void evaluate(GASolution<int[]> p) {
        // Ovo nije višedretveno sigurno!
//        if (im == null) {
//            im = new GrayScaleImage(template.getWidth(), template.getHeight());
//        }

        // ovo je visedretveno sigurno

        byte[] data = draw(p).getData();
        byte[] tdata = template.getData();

        int w = template.getWidth();
        int h = template.getHeight();

        double error = 0;
        int index2 = 0;
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                error += Math.abs(((int) data[index2] & 0xFF) - ((int) tdata[index2] & 0xFF));
                index2++;
            }
        }

        p.updateValue(error);
    }

}