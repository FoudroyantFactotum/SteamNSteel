package mod.steamnsteel.api.crafting;

import mod.steamnsteel.api.crafting.ingredient.IIngredient;
import mod.steamnsteel.api.crafting.ingredient.IIngredientSerializer;
import net.minecraft.item.ItemStack;
import java.util.Collection;

/**
 * Created by codew on 26/01/2016.
 */
public interface ICraftingManager
{
    ICraftingManager registerInventorySerializer(Class<? extends IIngredient> inventoryClass, IIngredientSerializer serializer);

    ICraftingManager addProjectTableRecipe(ItemStack output, IIngredient... input);
    ICraftingManager addProjectTableRecipe(ItemStack output, Collection<IIngredient> input);
    ICraftingManager addProjectTableRecipe(Collection<ItemStack> output, String displayName, Collection<IIngredient> input);
    ICraftingManager addProjectTableVanillaRecipe(ItemStack output, ItemStack... input);
    ICraftingManager addProjectTableVanillaRecipe(ItemStack output, Collection<ItemStack> input);
    ICraftingManager addProjectTableVanillaRecipe(Collection<ItemStack> output, String displayName, Collection<ItemStack> input);
}
