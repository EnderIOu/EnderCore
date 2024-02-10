package com.enderio.core.common.util;

import java.io.File;
import java.io.IOException;
import java.util.List;

import net.minecraftforge.fml.common.FMLCommonHandler;

import org.jetbrains.annotations.NotNull;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;

public class PermanentCache<I> extends WorldCache<I> {

    private static final List<PermanentCache<?>> allCaches = Lists.newArrayList();

    public PermanentCache(String ident) {
        super(ident);
        try {
            loadData(getSaveFile());
        } catch (IOException e) {
            Throwables.throwIfUnchecked(e);
            throw new RuntimeException(e);
        }
    }

    @Override
    protected @NotNull File getSaveFile() {
        if (FMLCommonHandler.instance().getSide().isServer()) {
            return new File(ident + ".dat");
        }
        return new File("saves", ident + ".dat");
    }

    @Override
    protected void blockOldIDs() {
        if (!objToName.isEmpty()) {
            super.blockOldIDs();
        }
    }

    @Override
    protected void mergeNewIDs() {
        if (!objToName.isEmpty()) {
            super.mergeNewIDs();
        }
    }

    @Override
    public void addObject(I object, String name) {
        super.addObject(object, name);
        setID(name);
    }

    public static void saveCaches() {
        for (PermanentCache<?> c : allCaches) {
            try {
                c.saveData(c.getSaveFile());
            } catch (IOException e) {
                Throwables.throwIfUnchecked(e);
                throw new RuntimeException(e);
            }
        }
    }
}
