package me.koridwen.frostlife.listeners;

import me.koridwen.frostlife.FrostLife;
import me.koridwen.frostlife.services.FrostbiteManager;
import me.koridwen.frostlife.services.LifeManager;
import me.koridwen.frostlife.services.Messages;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class FrostbiteListener implements Listener {
    public FrostbiteListener() { 
    }

    @EventHandler
    public void onSnowballThrowEvent(ProjectileHitEvent event) {
        Projectile proj = event.getEntity();

        if (proj instanceof Snowball && event.getHitEntity() != null && proj.getShooter() instanceof Player launcher && event.getHitEntity() instanceof Player receiver) {

            if (launcher.getUniqueId() == FrostLife.frostbitten && launcher.getUniqueId()!=receiver.getUniqueId()) {
                //blue life case
                if (FrostLife.lives.get(receiver.getUniqueId()) <= 1) {
                    FrostbiteManager.infection(launcher,receiver);
                    launcher.sendMessage(Messages.INFECTION_ONE_LIFE.message().replace("%player%", LifeManager.getLifeColor(FrostLife.lives.get(receiver.getUniqueId())) + receiver.getName()));
                }
                //immune player case
                else if (FrostbiteManager.frostbiteQueue.containsPlayer(receiver.getUniqueId())) {
                    launcher.sendMessage(Messages.INFECTION_ALREADY_INFECTED.message().replace("%player%", LifeManager.getLifeColor(FrostLife.lives.get(receiver.getUniqueId())) + receiver.getName()));
                    (new PotionEffect(PotionEffectType.GLOWING, 40, 0, false, false, true)).apply(launcher);
                }
                else if (!FrostbiteManager.waitingToStrike) {
                    FrostbiteManager.infection(launcher,receiver);
                    launcher.sendMessage(Messages.FROSTBITE_INFECTION.message());
                }
            }
        }
    }

    @EventHandler
    public void onMilkDrinkEvent(PlayerItemConsumeEvent event) {
        if (event.getItem().getType() == Material.MILK_BUCKET && event.getPlayer().getUniqueId() == FrostLife.frostbitten) {
            Player cursed = event.getPlayer();
            for (PotionEffect effect : cursed.getActivePotionEffects())
                cursed.removePotionEffect(effect.getType());
            FrostbiteManager.putBackEffects(cursed);
            if (cursed.getInventory().getItemInMainHand().getType() == Material.MILK_BUCKET) {
                cursed.getInventory().setItemInMainHand(new ItemStack(Material.BUCKET));
            }
            else if (cursed.getInventory().getItemInOffHand().getType() == Material.MILK_BUCKET) {
                cursed.getInventory().setItemInOffHand(new ItemStack(Material.BUCKET));
            }
            event.setCancelled(true);
        }
    }
}