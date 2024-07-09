package me.joda.shuffle;

import org.bukkit.Material;
import org.bukkit.entity.Player;

public class BlockShufflePlayer
{
    Player player;
    int score;
    Material blockToBeFound;
    boolean hasFoundBlock;
    
    public BlockShufflePlayer(final Player player) {
        this.player = player;
        this.score = 0;
        this.blockToBeFound = null;
        this.hasFoundBlock = false;
    }
    
    public String getName() {
        return this.player.getName();
    }
    
    public int getScore() {
        return this.score;
    }
    
    public void setScore(final int score) {
        this.score = score;
    }
    
    public boolean getHasFoundBlock() {
        return this.hasFoundBlock;
    }
    
    public void setHasFoundBlock(final boolean hasFoundBlock) {
        this.hasFoundBlock = hasFoundBlock;
    }
    
    public Material getBlockToBeFound() {
        return this.blockToBeFound;
    }
    
    public void setBlockToBeFound(final Material blockToBeFound) {
        this.blockToBeFound = blockToBeFound;
    }
}