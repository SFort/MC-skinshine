package sf.ssf.sfort;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.nio.file.Files;
import java.util.*;

public class SkinShine implements ClientModInitializer {
    public static Logger LOGGER = LogManager.getLogger();

    private static float showAt = 0.0F;
    public static boolean keepSelfHidden = false;

    public static boolean shouldHide(float health){
        return health> showAt;
    }
    @Override
    public void onInitializeClient() {
        // Configs
        File confFolder = FabricLoader.getInstance().getConfigDirectory();
        File oldFile = new File(
                confFolder,
                "SkinShine-ShowArmor.int"
        );
        File confFile = oldFile.exists()? oldFile: new File(
                confFolder,
                "SkinShine.conf"
        );
        try {
            confFile.createNewFile();
            List<String> la = Files.readAllLines(confFile.toPath());
            List<String> defaultDesc = Arrays.asList(
                    "^-Health value under which armor is visable [0.0] 0.0 - 20.0",
                    "^-Should your armor always be invis? [false] true | false"
            );
            String[] init =new String[Math.max(la.size(), defaultDesc.size() * 2)|1];
            String[] ls = la.toArray(init);
            for (int i = 0; i<defaultDesc.size();++i)
                ls[i*2+1]= defaultDesc.get(i);

            try{ showAt =Float.parseFloat(ls[0]);}catch (Exception ignored){};
            ls[0] = String.valueOf(showAt);

            try{ keepSelfHidden = ls[2].contains("true");}catch (Exception ignored){}
            ls[2] = String.valueOf(keepSelfHidden);

            Files.write(confFile.toPath(), Arrays.asList(ls));
            LOGGER.log(Level.INFO,"tf.ssf.sfort.skinshine successfully loaded config file");
        } catch(Exception e) {
            LOGGER.log(Level.ERROR,"tf.ssf.sfort.skinshine failed to load config file, using defaults\n"+e);
        }
    }
}
