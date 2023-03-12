package com.rrayy.skill.ability;

import java.util.Map;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import com.rrayy.skill.skill;

import xyz.xenondevs.particle.ParticleEffect;

public class wizard implements Listener {
    private int loop = 0;
    private skill main;
    private JavaPlugin plugin;
    private Map<UUID, String> abilitymap;
    private Map<UUID, Long> selectskill;

    public wizard(skill skill) {
       plugin = main = skill;
       abilitymap = main.ability;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) { // 플레이어가 상호작용을 할 때 실행
        Player player = e.getPlayer(); // 상호작용 한 플레이어
        if (!abilitymap.containsKey(player.getUniqueId())) return; // 상호작용 한 플레이어가 마법사가 아니면 멈추기
        Action action = e.getAction(); // 플레이어가 한 상호작용
        ItemStack item = e.getItem(); // 플레이어가 상호작용할 때 손에 들고 있던 아이템
        if (action.equals(Action.LEFT_CLICK_AIR) || action.equals(Action.LEFT_CLICK_BLOCK)) { // 플레이어가 좌클릭을 눌렀을 때
            if (item == main.ability_dye()) {
                Long skill;
                if (!selectskill.containsKey(player.getUniqueId())) selectskill.put(player.getUniqueId(), (long) 0);
                if (selectskill.get(player.getUniqueId())==1) selectskill.replace(player.getUniqueId(), (long) 0);
                skill = selectskill.get(player.getUniqueId()) + 1;
                if (skill == 0) magic_bullet(player);
                else protectoin(player);
            }
        }
    }

    public void bewizard(Player p){
        abilitymap = main.ability;
        if (abilitymap.get(p.getUniqueId()) == "wizard") {
            plugin.getLogger().warning("플레이어 "+p.getDisplayName()+"는 이미 마법사 입니다.");
            p.sendMessage(ChatColor.RED+"당신은 이미 마법사입니다!");
            return;
        } else {
            abilitymap.put(p.getUniqueId(), "wizard");
            ItemStack stick = new ItemStack(Material.BLAZE_ROD);
            ItemMeta stickmeta = stick.getItemMeta();
            stickmeta.setDisplayName(ChatColor.AQUA+"마법사의 지팡이");
            stick.setItemMeta(stickmeta);
            ItemStack magicbook = new ItemStack(Material.ENCHANTED_BOOK);
            EnchantmentStorageMeta bookmeta = (EnchantmentStorageMeta) magicbook.getItemMeta();
            bookmeta.setDisplayName(ChatColor.AQUA+"고대의 마법서");
            bookmeta.addStoredEnchant(Enchantment.MENDING, 1, true);
            magicbook.setItemMeta(bookmeta);
            p.getInventory().setItemInMainHand(stick);
            p.getInventory().setLeggings(new ItemStack(Material.CHAINMAIL_LEGGINGS));
            p.getInventory().setChestplate(magicbook);
            p.getInventory().addItem(main.ability_dye());
        }
        storeability();
    }

    private void storeability(){
        main.storeability(abilitymap);
        abilitymap = main.ability;
    }

    private void magic_bullet(Player player) {
        ArmorStand as = player.getWorld().spawn(player.getLocation(), ArmorStand.class);
        as.setVisible(false);
        as.getEquipment().setHelmet(new ItemStack(Material.CYAN_WOOL));
        as.setCanPickupItems(false);
        as.setGravity(false);
        as.setVelocity(player.getLocation().getDirection());
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                if (loop >= 40){
                    as.remove();// 마 탄환 없애기
                    return;
                }
                as.teleport(as.getLocation().add(as.getVelocity().multiply(0.1)));
                ParticleEffect.FLAME.display(as.getLocation());
                ++loop;
                for (Player p1 : plugin.getServer().getOnlinePlayers()) {
                    if (as.getLocation() == p1.getLocation() && !(p1.getUniqueId() == player.getUniqueId())) { // 마 탄환이 다른 얘랑 부딫치면
                        p1.setHealth(p1.getHealth() - 8); // 5피(2.5칸) 깎기
                        as.remove();// 마 탄환 없애기
                        return;
                    }
                }
            }
        }, 0L, 1L);
    }

    private void protectoin(Player player){
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                if (loop >= 600) return;
                else{
                    ParticleEffect.NOTE.display(player.getLocation());
                }
                
                ++loop;
            }
        }, 0L,1L);
    }

    @EventHandler
    public void wizard_damage(EntityDamageEvent e){
        if (e.getEntity() instanceof Player){
            Player p = (Player) e.getEntity();
            if (!abilitymap.containsKey(p.getUniqueId())) return;
        }else return;

    }
}
