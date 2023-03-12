package com.rrayy.skill.ability;

import java.util.Map;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.rrayy.skill.skill;

public class knight implements Listener {
    private int loop = 0;
    private skill main;
    private JavaPlugin plugin;
    private Map<UUID, String> abilitymap;

    public knight(skill skill) {
       plugin = main = skill;
       abilitymap = main.ability;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) { // 플레이어가 상호작용을 할 때 실행
        Player player = e.getPlayer(); // 상호작용 한 플레이어
        if (!abilitymap.containsKey(player.getUniqueId())) return; // 상호작용 한 플레이어가 기사가 아니면 멈추기
        Action action = e.getAction(); // 플레이어가 한 상호작용
        ItemStack item = e.getItem(); // 플레이어가 상호작용할 때 손에 들고 있던 아이템
        if (action.equals(Action.LEFT_CLICK_AIR) || action.equals(Action.LEFT_CLICK_BLOCK)) { // 플레이어가 좌클릭을 눌렀을 때
            if (item != null && item.getType() == Material.IRON_SWORD) throwsword(player, item); // 철 검 날리기
            if (item != null && item.getType() == Material.STONE_SWORD) throwsword(player, item); // 돌 검 날리기
            if (item != null && item.getType() == Material.DIAMOND_SWORD) throwsword(player, item); // 다이아 검 날리기
            // 이하 생략(누군가 만들겠지)
            if (item == main.ability_dye()) {
                
                //player.getWorld().spawnEntity(player.getLocation(), EntityType.HORSE);
            }
        }
    }

    private void throwsword(Player player, ItemStack item) {
        ArmorStand as = player.getWorld().spawn(player.getLocation(), ArmorStand.class);
        as.getEquipment().setItemInMainHand(item);
        as.setVisible(false);
        as.setCanPickupItems(false);
        as.setGravity(false);
        as.setVelocity(player.getLocation().getDirection());
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                if (loop >= 40){
                    as.remove();// 검 없애기
                    return;
                }
                as.teleport(as.getLocation().add(as.getVelocity().multiply(0.1)));
                ++loop;
                for (Player p1 : plugin.getServer().getOnlinePlayers()) {
                    if (as.getLocation() == p1.getLocation() && !(p1.getUniqueId() == player.getUniqueId())) { // 날라가는 검이 다른 얘랑 부딫치면
                        p1.setHealth(p1.getHealth() - 5); // 5피(2.5칸) 깎기
                        as.remove();// 검 없애기
                        return;
                    }
                }
            }
        }, 0L, 1L);
    }

    public void beknight(Player p){
        abilitymap = main.ability;
        if (abilitymap.containsKey(p.getUniqueId())) {
            plugin.getLogger().warning("플레이어 "+p.getDisplayName()+"는 이미 기사 입니다.");
            p.sendMessage(ChatColor.RED+"당신은 이미 기사입니다!");
            return;
        } else {
            abilitymap.put(p.getUniqueId(), "knight");
            p.getInventory().setItemInMainHand(new ItemStack(Material.STONE_SWORD));
            p.getInventory().setItemInOffHand(new ItemStack(Material.SHIELD));
            p.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
            p.getInventory().addItem(main.ability_dye());
            storeability();
        }
    }

    private void storeability(){
        main.storeability(abilitymap);
        abilitymap = main.ability;
    }
}