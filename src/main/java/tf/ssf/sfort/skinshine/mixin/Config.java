package tf.ssf.sfort.skinshine.mixin;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.EquipmentSlot;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static java.util.Collections.EMPTY_LIST;

public class Config implements IMixinConfigPlugin {
    private static final String mod = "tf.ssf.sfort.skinshine";
    public static Logger LOGGER = LogManager.getLogger();

    private static float showAt = 0.0F;
    public static float alpha = 1.0F;
    public static boolean keepSelfHidden = false;
    public static boolean hideBlocks = false;
    public static List<EquipmentSlot> listSlot = EMPTY_LIST;

    public static boolean shouldHide(float health){
        return health > showAt;
    }

    @Override
    public void onLoad(String mixinPackage) {
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
                    "^-Should all your armor always be invis? [false] true | false",
                    "^-Specify slots that should always be invis? [ ] head;chest;legs;feet",
                    "^-Apply to pumpkins/mob heads/..other head blocks? [false] true | false",
                    "^-Armor Translucency [1.0] 0.0 - 1.0"
                    );
            String[] init =new String[Math.max(la.size(), defaultDesc.size() * 2)|1];
            String[] ls = la.toArray(init);
            for (int i = 0; i<defaultDesc.size();++i)
                ls[i*2+1]= defaultDesc.get(i);

            try{ showAt =Float.parseFloat(ls[0]);}catch (Exception e){ LOGGER.log(Level.WARN,mod+" #0 "+e); };
            ls[0] = String.valueOf(showAt);

            try{ keepSelfHidden = ls[2].contains("true");}catch (Exception e){ LOGGER.log(Level.WARN,mod+" #2 "+e); };
            ls[2] = String.valueOf(keepSelfHidden);
            try{
                String[] in = ls[4].split("\\s*;\\s*");
                EquipmentSlot[] out = new EquipmentSlot[in.length];
                for (int i =0; i<in.length;++i) {
                    out[i] =EquipmentSlot.byName(in[i]);
                }
                listSlot= Arrays.asList(out);
            }catch (Exception e){ if (!e.toString().endsWith("Invalid slot ''")) LOGGER.log(Level.WARN,mod+" #4 "+e); };
            try{ hideBlocks = ls[6].contains("true");}catch (Exception e){ LOGGER.log(Level.WARN,mod+" #6 "+e); };
            ls[6] = String.valueOf(hideBlocks);
            String out ="";
            for (EquipmentSlot s : listSlot)
                out+=s.getName()+";";
            ls[4] = String.join(";", out);
            try{ alpha = Float.parseFloat(ls[8]);}catch (Exception e){ LOGGER.log(Level.WARN,mod+" #8 "+e); };
            ls[8] = String.valueOf(alpha);

            Files.write(confFile.toPath(), Arrays.asList(ls));
            LOGGER.log(Level.INFO,mod+" successfully loaded config file");
        } catch(Exception e) {
            LOGGER.log(Level.ERROR,mod+" failed to load config file, using defaults\n"+e);
        }
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        switch (mixinClassName){
            case mod+".mixin.Head":return hideBlocks;
            case mod+".mixin.GetContext":
            case mod+".mixin.Transparent": return alpha != 1.0F;
            default:return true;
        }
    }
    @Override public String getRefMapperConfig() { return null; }
    @Override public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) { }
    @Override public List<String> getMixins() { return null; }
    @Override public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) { }
    @Override public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) { }
}
