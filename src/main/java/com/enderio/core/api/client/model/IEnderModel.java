package com.enderio.core.api.client.model;

import net.minecraft.client.model.ModelRenderer;

/**
 * To be used on techne-style box models
 */
@Deprecated
public interface IEnderModel {

    /**
     * Renders all parts of the model
     *
     * @param size
     *             - Size of the model (usually 0.0625f)
     */
    void render(float size);

    /**
     * Rotates the model part in the given directions
     *
     * @param model
     *              - part of the model to rotate
     * @param x
     * @param y
     * @param z
     */
    void setRotation(ModelRenderer model, float x, float y, float z);
}
