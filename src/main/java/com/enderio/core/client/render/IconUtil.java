package com.enderio.core.client.render;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import org.jetbrains.annotations.NotNull;

import com.enderio.core.EnderCore;

public class IconUtil {

    public interface IIconProvider {

        void registerIcons(@NotNull TextureMap register);
    }

    public static @NotNull IconUtil instance = new IconUtil();

    public static void addIconProvider(@NotNull IIconProvider registrar) {
        instance.iconProviders.add(registrar);
    }

    private final @NotNull ArrayList<IIconProvider> iconProviders = new ArrayList<IIconProvider>();

    public @NotNull TextureAtlasSprite whiteTexture;
    public @NotNull TextureAtlasSprite blankTexture;
    public @NotNull TextureAtlasSprite errorTexture;

    private boolean doneInit = false;

    @SuppressWarnings("null")
    private IconUtil() {}

    public void init() {
        if (doneInit) {
            return;
        }
        doneInit = true;
        MinecraftForge.EVENT_BUS.register(this);
        addIconProvider(register -> {
            whiteTexture = register.registerSprite(new ResourceLocation(EnderCore.MODID, "white"));
            errorTexture = register.registerSprite(new ResourceLocation(EnderCore.MODID, "error"));
            blankTexture = register.registerSprite(new ResourceLocation(EnderCore.MODID, "blank"));
        });
    }

    @SubscribeEvent
    public void onIconLoad(TextureStitchEvent.Pre event) {
        for (IIconProvider reg : iconProviders) {
            final TextureMap map = event.getMap();
            if (map != null) {
                reg.registerIcons(map);
            }
        }
    }

    @SuppressWarnings("null") // don't trust modded models to not do stupid things...
    public static @NotNull TextureAtlasSprite getIconForItem(@NotNull Item item, int meta) {
        final TextureAtlasSprite icon = Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
                .getParticleIcon(item, meta);
        return icon != null ? icon : Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite();
    }
}
