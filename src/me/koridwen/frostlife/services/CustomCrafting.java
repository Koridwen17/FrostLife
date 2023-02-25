package me.koridwen.frostlife.services;

import me.koridwen.frostlife.FrostLife;
import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

public class CustomCrafting {
    private static boolean isNametagTweaked = true;
    private static boolean isTNTTweaked = true;
    private static boolean isSaddleTweaked = true;
    private static boolean isSporeTweaked = false;

    public CustomCrafting() {
    }


    public static void enableTweakedRecipes() {
        registerRecipes();
        removeRecipesFor(Material.ENCHANTING_TABLE);
        removeRecipesFor(Material.BOOKSHELF);
    }

    public static void disableTweakedRecipes() {
        Bukkit.getServer().removeRecipe(new NamespacedKey(FrostLife.getInstance(), "nametag"));
        Bukkit.removeRecipe(new NamespacedKey(FrostLife.getInstance(), "tnt"));
        Bukkit.removeRecipe(new NamespacedKey(FrostLife.getInstance(), "saddle"));
        if (Bukkit.getServer().getVersion().contains("1.17") || Bukkit.getServer().getVersion().contains("1.18") || Bukkit.getServer().getVersion().contains("1.19")) {
            Bukkit.removeRecipe(new NamespacedKey(FrostLife.getInstance(), "spore"));
        }

    }

    public static void reloadCustomRecipes() {
        try {
            Bukkit.removeRecipe(new NamespacedKey(FrostLife.getInstance(), "life"));
            disableTweakedRecipes();
            enableTweakedRecipes();
        } catch (IllegalStateException var1) {
        }

    }

    private static void registerRecipes() {
        Bukkit.getServer().addRecipe(getSnowRecipe());
        if (isNametagTweaked) {
            Bukkit.getServer().addRecipe(getNameTagRecipe());
            Bukkit.getServer().addRecipe(getNameTagRecipe2());
            Bukkit.getServer().addRecipe(getNameTagRecipe3());
            Bukkit.getServer().addRecipe(getNameTagRecipe4());
            Bukkit.getServer().addRecipe(getNameTagRecipe5());
            Bukkit.getServer().addRecipe(getNameTagRecipe6());
        }

        if (isTNTTweaked) {
            Bukkit.getServer().addRecipe(getTNTRecipe());
        }

        if (isSaddleTweaked) {
            Bukkit.getServer().addRecipe(getSaddleRecipe());
            Bukkit.getServer().addRecipe(getSaddleRecipe2());
        }

        if ((Bukkit.getServer().getVersion().contains("1.17") || Bukkit.getServer().getVersion().contains("1.18") || Bukkit.getServer().getVersion().contains("1.19")) && isSporeTweaked) {
            Bukkit.getServer().addRecipe(getSporeBlossomRecipe());
        }

    }

    private static void removeRecipesFor(Material m) {
        Iterator<Recipe> it = Bukkit.getServer().recipeIterator();

        while(it.hasNext()) {
            Recipe recipe = it.next();
            if (recipe != null && recipe.getResult().getType() == m) {
                it.remove();
            }
        }

    }

    private static ShapedRecipe getNameTagRecipe() {
        NamespacedKey key = new NamespacedKey(FrostLife.getInstance(), "nametag");
        ItemStack item = new ItemStack(Material.NAME_TAG);
        ShapedRecipe recipe = new ShapedRecipe(key, item);
        recipe.shape("   ", " S ", " P ");
        recipe.setIngredient('S', Material.STRING);
        recipe.setIngredient('P', Material.PAPER);
        return recipe;
    }
    private static ShapedRecipe getNameTagRecipe2() {
        NamespacedKey key = new NamespacedKey(FrostLife.getInstance(), "nametag2");
        ItemStack item = new ItemStack(Material.NAME_TAG);
        ShapedRecipe recipe = new ShapedRecipe(key, item);
        recipe.shape(" S ", " P ", "   ");
        recipe.setIngredient('S', Material.STRING);
        recipe.setIngredient('P', Material.PAPER);
        return recipe;
    }
    private static ShapedRecipe getNameTagRecipe3() {
        NamespacedKey key = new NamespacedKey(FrostLife.getInstance(), "nametag3");
        ItemStack item = new ItemStack(Material.NAME_TAG);
        ShapedRecipe recipe = new ShapedRecipe(key, item);
        recipe.shape("   ", "S  ", "P  ");
        recipe.setIngredient('S', Material.STRING);
        recipe.setIngredient('P', Material.PAPER);
        return recipe;
    }
    private static ShapedRecipe getNameTagRecipe4() {
        NamespacedKey key = new NamespacedKey(FrostLife.getInstance(), "nametag4");
        ItemStack item = new ItemStack(Material.NAME_TAG);
        ShapedRecipe recipe = new ShapedRecipe(key, item);
        recipe.shape("S  ", "P  ", "   ");
        recipe.setIngredient('S', Material.STRING);
        recipe.setIngredient('P', Material.PAPER);
        return recipe;
    }
    private static ShapedRecipe getNameTagRecipe5() {
        NamespacedKey key = new NamespacedKey(FrostLife.getInstance(), "nametag5");
        ItemStack item = new ItemStack(Material.NAME_TAG);
        ShapedRecipe recipe = new ShapedRecipe(key, item);
        recipe.shape("   ", "  S", "  P");
        recipe.setIngredient('S', Material.STRING);
        recipe.setIngredient('P', Material.PAPER);
        return recipe;
    }
    private static ShapedRecipe getNameTagRecipe6() {
        NamespacedKey key = new NamespacedKey(FrostLife.getInstance(), "nametag6");
        ItemStack item = new ItemStack(Material.NAME_TAG);
        ShapedRecipe recipe = new ShapedRecipe(key, item);
        recipe.shape("  S", "  P", "   ");
        recipe.setIngredient('S', Material.STRING);
        recipe.setIngredient('P', Material.PAPER);
        return recipe;
    }

    private static ShapedRecipe getTNTRecipe() {
        NamespacedKey key = new NamespacedKey(FrostLife.getInstance(), "tnt");
        ItemStack item = new ItemStack(Material.TNT);
        ShapedRecipe recipe = new ShapedRecipe(key, item);
        recipe.shape(new String[]{"PSP", "SGS", "PSP"});
        recipe.setIngredient('P', Material.PAPER);
        recipe.setIngredient('S', Material.SAND);
        recipe.setIngredient('G', Material.GUNPOWDER);
        return recipe;
    }

    private static ShapedRecipe getSporeBlossomRecipe() {
        NamespacedKey key = new NamespacedKey(FrostLife.getInstance(), "spore");
        ItemStack item = new ItemStack(Material.SPORE_BLOSSOM);
        ShapedRecipe recipe = new ShapedRecipe(key, item);
        recipe.shape("  M", " L ", "   ");
        recipe.setIngredient('M', Material.MOSS_BLOCK);
        recipe.setIngredient('L', Material.LILAC);
        return recipe;
    }

    private static ShapedRecipe getSnowRecipe() {
        NamespacedKey key = new NamespacedKey(FrostLife.getInstance(), "snow");
        ItemStack item = new ItemStack(Material.SNOW,8);
        ShapedRecipe recipe = new ShapedRecipe(key, item);
        recipe.shape("   ", "   ", "SS ");
        recipe.setIngredient('S', Material.SNOW_BLOCK);
        return recipe;
    }

    private static ShapedRecipe getSaddleRecipe() {
        NamespacedKey key = new NamespacedKey(FrostLife.getInstance(), "saddle");
        ItemStack item = new ItemStack(Material.SADDLE);
        ShapedRecipe recipe = new ShapedRecipe(key, item);
        recipe.shape("   ", " L ", "L L");
        recipe.setIngredient('L', Material.LEATHER);
        return recipe;
    }
    private static ShapedRecipe getSaddleRecipe2() {
        NamespacedKey key = new NamespacedKey(FrostLife.getInstance(), "saddle2");
        ItemStack item = new ItemStack(Material.SADDLE);
        ShapedRecipe recipe = new ShapedRecipe(key, item);
        recipe.shape(" L ", "L L", "   ");
        recipe.setIngredient('L', Material.LEATHER);
        return recipe;
    }
}
