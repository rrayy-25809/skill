package com.rrayy.skill;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import com.rrayy.skill.ability.knight;
import com.rrayy.skill.ability.wizard;

public class skill extends JavaPlugin implements CommandExecutor{
    public Map<UUID, String> ability = new HashMap<UUID, String>();
    private knight knight = new knight(this); 
    private wizard wizard = new wizard(this);
    
    @Override
    public void onEnable() {
        getLogger().info("skill plugin이 활성화 되었습니다");
        getServer().getPluginManager().registerEvents(knight, this);
        getServer().getPluginManager().registerEvents(wizard, this);
        getCommand("ability").setExecutor(this);
    }

    public void storeability(Map<UUID, String> m){
        ability = m;
    }

    public ItemStack ability_dye(){
        List<String> Lore = new ArrayList<String>();
        Lore.add(ChatColor.YELLOW + "아이템을 들고 좌클릭 하면 능력을 사용한다.");//설명 1
        Lore.add(ChatColor.YELLOW + "능력은 플레이어의 직업에 따라 다르다.");//설명 2
        ItemStack dye = new ItemStack(Material.RED_DYE);
        ItemMeta meta = dye.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA+"능력의..?");//작명 어렵네...
        meta.setLore(Lore);
        dye.setItemMeta(meta);
        return dye;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player sendPlayer = (Player) sender;
        if (ability.containsKey(sendPlayer.getUniqueId())) return false; //이미 능력이 있으면 취소
        if (args[0].equals("knight")) knight.beknight(sendPlayer); //기사 만들어 주기
        if (args[0].equals("wizard")) wizard.bewizard(sendPlayer);; //마법사 만들어 주기
        //if (args[0].equals("knight")) knight.beknight(sendPlayer); //기사 만들어 주기
        return false;
    }
}
/* 기사: 기본적으로 철 흉갑과 방패, 돌검을 들고 시작한다.--끝
    스킬1: 15초 마다 스피드(1) 이펙트를 10초간 준다.
    스킬2: 검을 던져 플레이어를 맞추어 5의 데미지를 준다.--끝
*/
/* 마법사: 기본적으로 사슬 각반, 마법서(수선책), 블레이즈 막대기을 들고 시작한다.
    스킬1: 쿨타임5초 플레이어가 보는 방향으로 마 탄환이 날아간다. 그 마 탄환에 맞은 플레이어는 8의 데미지를 입는다.
    스킬2: 쿨타임 5초 마법사가 30초 간 데미지의 75%를 방어한다.
    특이점: 스킬을 블레이즈 막대기로 사용하며, 사용할 스킬을 능력의..?으로 바꾸는 특이한 방식이다
*/
/*광전사: 기본적으로 철 흉갑과 철 도끼를 지급받는다
    스킬1: 빠르게 돌진한다.
    스킬2: 30초 마다 스피드(2)와 저항(1) 이펙트를 10초간 준다
 */