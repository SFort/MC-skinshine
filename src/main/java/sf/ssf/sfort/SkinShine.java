package sf.ssf.sfort;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.nio.file.Files;
import java.util.*;
import java.util.logging.Logger;

public class SkinShine implements ClientModInitializer {

    private static float showAt = 0.0F;
    private static boolean keepSelfHidden = false;

    public static boolean shouldHide(float health){
        return health> showAt;
    }
    public static boolean shouldSelfHide(){ return keepSelfHidden; }
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
            String[] ls = la.toArray(new String[la.size()>defaultDesc.size()*2?la.size():defaultDesc.size()*2]);
            for (int i = 0; i<defaultDesc.size();++i)
                ls[i*2+1]= defaultDesc.get(i);

            try{ showAt =Float.parseFloat(ls[0]);}catch (NumberFormatException ignored){};
            ls[0] = String.valueOf(showAt);

            keepSelfHidden = ls[2].contains("true");
            ls[2] = String.valueOf(keepSelfHidden);

            if(ls.length>defaultDesc.size()*2){
                for (int i = (defaultDesc.size()*2)+1; i<=ls.length;i+=2){
                    ls[i] = "!#Unknown value / config from the future";
                }
            }
            Files.write(confFile.toPath(), Arrays.asList(ls));
            System.out.println("tf.ssf.sfort.skinshine successfully loaded config file");
        } catch(Exception e) {
            System.out.println("tf.ssf.sfort.skinshine failed to load config file, using defaults\n"+e);
        }
    }
}