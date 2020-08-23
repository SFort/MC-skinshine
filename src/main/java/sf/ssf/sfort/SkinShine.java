package sf.ssf.sfort;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.util.Scanner;

public class SkinShine implements ClientModInitializer {

    private static float showat =0;

    public static boolean shouldHide(float health){
        return health>showat;
    }
    @Override
    public void onInitializeClient() {

        // Configuration
        File configFile = new File(
                FabricLoader.getInstance().getConfigDirectory(),
                "SkinShine-ShowArmor.int"
        );
        try {
            if (!configFile.createNewFile()) {
                showat = new Scanner(configFile).nextFloat();
            }
        } catch(Exception e) {
            System.out.println("tf.ssf.sfort.skinshine failed to load config file using defaults");
        }
    }
}