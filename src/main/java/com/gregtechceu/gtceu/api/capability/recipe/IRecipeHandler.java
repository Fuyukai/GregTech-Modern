package com.gregtechceu.gtceu.api.capability.recipe;

import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import org.jetbrains.annotations.Nullable;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author KilaBash
 * @date 2023/2/20
 * @implNote IRecipeHandler
 */
public interface IRecipeHandler<K> {

    /**
     * matching or handling the given recipe.
     *
     * @param io       the IO type of this recipe. always be one of the {@link IO#IN} or {@link IO#OUT}
     * @param recipe   recipe.
     * @param left     left contents for to be handled.
     * @param slotName specific slot name.
     * @param simulate simulate.
     * @return left contents for continue handling by other proxies.
     * <br>
     * null - nothing left. handling successful/finish. you should always return null as a handling-done mark.
     */
    List<K> handleRecipeInner(IO io, GTRecipe recipe, List<K> left, @Nullable String slotName, boolean simulate);

    /**
     * Slot name, it makes sense if recipe contents specify a slot name.
     */
    @Nullable
    default Set<String> getSlotNames() {
        return null;
    }

    /**
     * container size, if it has one. otherwise -1.
     */
    default int getSize() {
        return -1;
    }

    List<Object> getContents();

    /**
     * Whether the content of same capability  can only be handled distinct.
     */
    default boolean isDistinct() {
        return false;
    }

    RecipeCapability<K> getCapability();

    @SuppressWarnings("unchecked")
    default K copyContent(Object content) {
        return getCapability().copyInner((K)content);
    }

    default List<K> handleRecipe(IO io, GTRecipe recipe, List<?> left, @Nullable String slotName, boolean simulate) {
        List<K> contents = new ObjectArrayList<>(left.size());
        for (Object leftObj : left) {
            contents.add(copyContent(leftObj));
        }
        return handleRecipeInner(io, recipe, contents, slotName, simulate);
    }

    default void preWorking(IRecipeCapabilityHolder holder, IO io, GTRecipe recipe) {
    }

    default void postWorking(IRecipeCapabilityHolder holder, IO io, GTRecipe recipe) {
    }


}
