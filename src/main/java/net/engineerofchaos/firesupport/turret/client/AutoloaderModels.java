package net.engineerofchaos.firesupport.turret.client;

import com.google.common.collect.Maps;
import net.engineerofchaos.firesupport.FireSupport;
import net.engineerofchaos.firesupport.entity.render.ModModelLayers;
import net.engineerofchaos.firesupport.turret.Autoloader;
import net.engineerofchaos.firesupport.turret.Autoloaders;
import net.engineerofchaos.firesupport.turret.client.model.ModelFactory;
import net.engineerofchaos.firesupport.turret.client.model.autoloader.AC20MShortRecoilModel;
import net.engineerofchaos.firesupport.turret.client.model.autoloader.AutoloaderModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayer;

import java.util.HashMap;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class AutoloaderModels {
    private static final Map<Autoloader, EntityModelLayer> AUTOLOADER_MODEL_LAYER_MAP = Maps.newHashMap();
    private static final Map<Autoloader, ModelFactory> AUTOLOADER_MODEL_MAP = Maps.newHashMap();

    private static <T extends Autoloader> void register(T autoloader, EntityModelLayer modelLayer, ModelFactory model) {
        AUTOLOADER_MODEL_LAYER_MAP.put(autoloader, modelLayer);
        AUTOLOADER_MODEL_MAP.put(autoloader, model);
    }

    public static EntityModelLayer retrieveModelLayer(Autoloader autoloader) {
        return AUTOLOADER_MODEL_LAYER_MAP.get(autoloader);
    }

    public static void initAutoloaderModels() {
        FireSupport.LOGGER.info("initialising autoloader models");
    }

    static {
        register(Autoloaders.AC_20M_SHORT_RECOIL, ModModelLayers.AC_20M_SHORT_RECOIL, AC20MShortRecoilModel::new);
    }

    public static void loadAllModels(HashMap<Autoloader, AutoloaderModel> modelMap, EntityRendererFactory.Context ctx) {
        for (var entry : AUTOLOADER_MODEL_MAP.entrySet()) {
            modelMap.put(entry.getKey(), (AutoloaderModel) entry.getValue().create(ctx.getPart(retrieveModelLayer(entry.getKey()))));
        }
    }

}
