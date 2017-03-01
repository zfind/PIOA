package hr.fer.zemris.optjava.rng;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class RNG {

    private static IRNGProvider rngProvider;

    static {
        // Stvorite primjerak razreda Properties;
        Properties properties = new Properties();

        // Nad Classloaderom razreda RNG tražite InputStream prema resursu rng-config.properties
        InputStream is = RNG.class.getClassLoader().getResourceAsStream("rng-config.properties");

        // recite stvorenom objektu razreda Properties da se učita podatcima iz tog streama.
        try {
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Dohvatite ime razreda pridruženo ključu "rng-provider"; zatražite Classloader razreda
        // RNG da učita razred takvog imena i nad dobivenim razredom pozovite metodu newInstance()
        // kako biste dobili jedan primjerak tog razreda; castajte ga u IRNGProvider i zapamtite.
        String className = properties.getProperty("rng-provider");
        try {
            rngProvider = (IRNGProvider) RNG.class.getClassLoader().loadClass(className).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static IRNG getRNG() {
        return rngProvider.getRNG();
    }

}