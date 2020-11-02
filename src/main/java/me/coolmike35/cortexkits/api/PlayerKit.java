package me.coolmike35.cortexkits.api;

import org.bukkit.entity.Player;

public class PlayerKit {
    Player player;
    String kit;

    public PlayerKit(Player player, String kit){
        this.player = player;
        this.kit = kit;
    }

    public void giveKit(){
        //gives player kit
    }

    public void previewKit(){
        //Brings gui with selected kit
    }
}
