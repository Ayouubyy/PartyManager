package me.serenia.partymanager;


import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.type.Door;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Utils {
    public static String getString(String s, PartyManager plugin){ return chat(plugin.getConfig().getString(s));}
    public static String chat (String s){
        return ChatColor.translateAlternateColorCodes('&', s);
    }
    public static void  setModel(ItemStack item, int model){ ItemMeta meta = item.getItemMeta();if (meta != null) meta.setCustomModelData(model);item.setItemMeta(meta); }
    public static void damageMeta(ItemStack item, int i){
        if (item.getItemMeta() == null) return;
        if (!(item.getItemMeta() instanceof Damageable)) return;
        Damageable meta = (Damageable) item.getItemMeta();
        meta.setDamage(meta.getDamage() + i);
        if (meta.getDamage() == 238) item.setType(Material.AIR);
        item.setItemMeta((ItemMeta) meta);
    }
    public static void  dropItem(ItemStack item, int amount, Location loc){
        if  (loc.getWorld() == null) return;
        if (amount == 0)return;
        ItemStack clone = item.clone();
        clone.setAmount(amount);
        loc.getWorld().dropItem(loc ,clone);
    }
    public static void RenderDoor(World w, int xWorld, int yWorld, int zWorld, Material eDoorType, BlockFace eFace)
    {
        Block bottom = w.getBlockAt(xWorld, yWorld, zWorld);
        Block top = bottom.getRelative(BlockFace.UP);
        bottom.setType(eDoorType, false);
        top.setType(eDoorType, false);
        Door d1 = (Door) bottom.getBlockData();
        Door d2 = (Door) top.getBlockData();
        d1.setHalf(Bisected.Half.BOTTOM);
        d2.setHalf(Bisected.Half.TOP);
        d1.setFacing(eFace);
        d2.setFacing(eFace);

        bottom.setBlockData(d1);
        top.setBlockData(d2);
    }
    public static String getTime(int secondsx) {
        int days = (int) TimeUnit.SECONDS.toDays(secondsx);
        int hours = (int) (TimeUnit.SECONDS.toHours(secondsx) - TimeUnit.DAYS.toHours(days));
        int minutes = (int) (TimeUnit.SECONDS.toMinutes(secondsx) - TimeUnit.HOURS.toMinutes(hours)
                - TimeUnit.DAYS.toMinutes(days));
        int seconds = (int) (TimeUnit.SECONDS.toSeconds(secondsx) - TimeUnit.MINUTES.toSeconds(minutes)
                - TimeUnit.HOURS.toSeconds(hours) - TimeUnit.DAYS.toSeconds(days));
        String secs = String.valueOf(seconds);
        if (seconds < 10) secs = "0" + secs;
        if (seconds == 0) secs = "00";
        String mins = String.valueOf(minutes);
        if (minutes < 10) mins = "0" + mins;
        mins =  mins+ ":";
        if (minutes == 0) mins = "00:";
        String hs = String.valueOf(hours);
        if (hours < 10) hs = "0" + hs;
        hs =  hs+ ":";
        if (hours == 0) hs = "";
        String ds = String.valueOf(days);
        if (days < 10) ds = "0" + ds;
        ds =  ds+ ":";
        if (days == 0) ds = "";
        return ds+ hs +mins +secs;
    }
    public static void destroyFrame(Block block){
        for (Entity nearbyEntity : block.getWorld().getNearbyEntities(block.getLocation(), 1, 1, 1)) {
            if (nearbyEntity.getType() != EntityType.ITEM_FRAME) {
                continue;
            }

            ItemFrame shulker = (ItemFrame) nearbyEntity;

            Location frameLocation = shulker.getLocation();
            Location blockLocation = block.getLocation();
            if (frameLocation.getBlockX() != blockLocation.getX() || frameLocation.getBlockZ() != blockLocation.getZ() || frameLocation.getBlockY() != blockLocation.getY()) {
                continue;
            }
            if (shulker.isFixed()){
                shulker.remove();
            }
        }
    }
    public static void addLore(ItemStack item, String s){
         List<String> list = new ArrayList<>();
         if (item.getItemMeta().hasLore()) list.addAll(item.getItemMeta().getLore());
            list.add(Utils.chat(s));
               ItemMeta meta = item.getItemMeta();
           meta.setLore(list);
           meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
           item.setItemMeta(meta);
    }
    public static   void spawnRandomInRange(Player p, EntityType type, int range){
        Location loc = p.getLocation();
        Random rand = new Random();

        int pos2 = loc.getBlockX() - range;
        int pos4 = loc.getBlockZ() - range;
        int newpos1 = pos2 + rand.nextInt(range * 2);
        int newpos2 = pos4 + rand.nextInt(range * 2);
        Location loc1 = loc.clone();
        loc1.setX(newpos1);
        loc1.setZ(newpos2);
        for (int i = loc.getBlockY(); i < 257; i++) {
            Location loc2 = loc1.clone();
            loc2.setY(i + 1);
            if (loc2.getBlock().getType().isAir()){
                loc2.setY(i);
                if (loc2.getBlock().getType().isAir()) {
                    loc1.setY(i);
                    p.getWorld().spawnEntity(loc1, type);
                    break;
                }
            }
        }
    }
    public static void clearChat(Player p){
        for (int i = 0; i < 200; i++) {
            p.sendMessage("");
        }
    }
    public static void deleteDirectoryStream(Path path) throws IOException {
        Files.walk(path)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
    }
    public static void buildChar(Location loc, World w, Material mat, BlockFace direction, String... layers){
        if (layers.length != 5) return;
        if (!mat.isBlock()) return;
        int x = loc.getBlockX();
        int y = loc.getBlockY();
        int z = loc.getBlockZ();
        int tx = loc.getBlockX();
        int tz = loc.getBlockZ();
        int additionX;
        int additionZ;
        if (direction == BlockFace.EAST){
            additionX = 1;
            additionZ = 0;
        } else  if(direction == BlockFace.WEST){
            additionX = -1;
            additionZ = 0;
        } else if(direction == BlockFace.NORTH){
            additionX = 0;
            additionZ = -1;
        } else if(direction == BlockFace.SOUTH){
            additionX = 0;
            additionZ = 1;
        } else {
            return;
        }
        for (String s : layers){
            if (s.length() != 5) return;
            for (Character character : s.toCharArray()){
                if (character == 'A'){
                    w.getBlockAt(tx, y, tz).setType(mat);
                    tx = tx + additionX;
                    tz = tz + additionZ;
                } else if ( character == ' '){
                    w.getBlockAt(tx, y, tz).setType(Material.AIR);
                    tx = tx + additionX;
                    tz = tz + additionZ;
                }
            }
            tx = x;
            tz = z;
            y = y+1;
        }
    }
    public static void fill(Location l1, Location l2, Material material, Material ... exception){
        if (l1.getWorld() == null) return;
        int x1 = l1.getBlockX();
        int y1 = l1.getBlockY();
        int z1 = l1.getBlockZ();
        int x2 = l2.getBlockX();
        int y2 = l2.getBlockY();
        int z2 = l2.getBlockZ();
        int biggerx;
        int biggery;
        int biggerz;
        int smallerx;
        int smallery;
        int smallerz;
        if (x1 >= x2){ biggerx = x1;smallerx = x2; } else {smallerx = x1;biggerx = x2; }
        if (y1 >= y2){ biggery = y1;smallery = y2; } else {smallery = y1;biggery = y2; }
        if (z1 >= z2){ biggerz = z1;smallerz = z2; } else {smallerz = z1;biggerz = z2; }
        for (int i = smallerx; i < biggerx + 1; i++) {
            for (int j = smallery; j < biggery + 1; j++) {
                OUTTER:
                for (int k = smallerz; k < biggerz + 1; k++) {
                    Block b = l1.getWorld().getBlockAt(i, j, k);
                    for (Material mat : exception){
                        if (mat == b.getType()) continue OUTTER;
                    }
                    b.setType(material);
                }
            }
        }
    }
    public static Location center(Block block){
        Location loc = block.getLocation().clone();
        loc.add(new Vector(0.5,0.5,0.5));
        return loc;
    }
    public static ArrayList<Entity> getEntitiesInBlock(Block b){
        ArrayList<Entity> list = new ArrayList<>();
        for (Entity entity : b.getWorld().getNearbyEntities(b.getLocation(), 2, 2,2)){
            Location l = entity.getLocation();
            if (l.getBlockX() == b.getX() && l.getBlockZ() == b.getZ() && (l.getBlockY() == b.getY())){
                list.add(entity);
            }
        }
        return list;
    }
    public static ArrayList<Location> blockBetweenPlayers(Player p, Player ps){
        Location loc = p.getEyeLocation().clone();
        Location loc1 = ps.getEyeLocation().clone();
        World world = loc.getWorld();
        if (world == null) return null;
        double distance = loc.distance(loc1);
        Vector p1 = loc.toVector();
        Vector p2 = loc1.toVector();
        Vector vector = p2.clone().subtract(p1).normalize().multiply(1);
        double length = 0;
        ArrayList<Location> locs = new ArrayList<>();
        for (; length < distance; p1.add(vector)) {
            Block b = world.getBlockAt(p1.getBlockX(), p1.getBlockY(), p1.getBlockZ());
            locs.add(p1.toLocation(p.getWorld()));
            length += 1;
        }
        return locs;
    }
    public static void buildString(Location loc, World w, Material mat, BlockFace direction, String s){
        s.toUpperCase(Locale.ROOT);
        if (!mat.isBlock()) return;
        int x = loc.getBlockX();
        int y = loc.getBlockY();
        int z = loc.getBlockZ();
        int additionX;
        int additionZ;
        if (direction == BlockFace.EAST){
            additionX = 5;
            additionZ = 0;
        } else  if(direction == BlockFace.WEST){
            additionX = -5;
            additionZ = 0;
        } else if(direction == BlockFace.NORTH){
            additionX = 0;
            additionZ = -5;
        } else if(direction == BlockFace.SOUTH){
            additionX = 0;
            additionZ = 5;
        } else {
            return;
        }
        for (Character cha : s.toCharArray()){
            Location l = new Location(w,x,y,z, 0, 0);
            if (cha == 'A'){
            buildChar(l, w, mat, direction, "A  A ", "A  A ", "AAAA ", "A  A ", "AAAA ");
            } else if (cha == 'B'){
                buildChar(l, w, mat, direction, "AAAA ", "A  A ", "AAAA ", "A A  ", "AAA  ");
            } else if (cha == 'C'){
                buildChar(l, w, mat, direction, "AAAA ", "A    ", "A    ", "A    ", "AAAA ");
            } else if (cha == 'D'){
                buildChar(l, w, mat, direction, "AAA  ", "A  A ", "A  A ", "A  A ", "AAA  ");
            }else if (cha == 'E'){
                buildChar(l, w, mat, direction, "AAAA ", "A    ", "AAA  ", "A    ", "AAAA ");
            }else if (cha == 'F'){
                buildChar(l, w, mat, direction, "A    ", "A    ", "AAA  ", "A    ", "AAAA ");
            }else if (cha == 'G'){
                buildChar(l, w, mat, direction, "AAAA ", "A  A ", "A AA ", "A    ", "AAAA ");
            }else if (cha == 'H'){
                buildChar(l, w, mat, direction, "A  A ", "A  A ", "AAAA ", "A  A ", "A  A ");
            }else if (cha == 'I'){
                buildChar(l, w, mat, direction, "AAA  ", " A   ", " A   ", " A   ", "AAA  ");
                if(additionX < 0 || additionZ < 0){
                    if (additionX != 0) x = x + additionX + 1;
                    if (additionZ != 0) z = z + additionZ + 1;
                } else {
                    if (additionX != 0) x = x + additionX - 1;
                    if (additionZ != 0) z = z + additionZ - 1;
                }
                continue;
            }else if (cha == 'J'){
                buildChar(l, w, mat, direction, "AAA  ", "A A  ", "A    ", "A    ", "A    ");
                if(additionX < 0 || additionZ < 0){
                    if (additionX != 0) x = x + additionX + 1;
                    if (additionZ != 0) z = z + additionZ + 1;
                } else {
                    if (additionX != 0) x = x + additionX - 1;
                    if (additionZ != 0) z = z + additionZ - 1;
                }
                continue;
            }else if (cha == 'K'){
                buildChar(l, w, mat, direction, "A  A ", "A A  ", "AA   ", "A A  ", "A  A ");
            }else if (cha == 'L'){
                buildChar(l, w, mat, direction, "AAAA ", "A    ", "A    ", "A    ", "A    ");
            }else if (cha == 'M'){
                buildChar(l, w, mat, direction, "A   A", "A   A", "A A A", "AA AA", "A   A");
                if(additionX < 0 || additionZ < 0){
                    if (additionX != 0) x = x + additionX - 1;
                    if (additionZ != 0) z = z + additionZ - 1;
                } else {
                    if (additionX != 0) x = x + additionX + 1;
                    if (additionZ != 0) z = z + additionZ + 1;
                }
                continue;
            }else if (cha == 'N'){
                buildChar(l, w, mat, direction, "A  A ", "A AA ", "AA A ", "A  A ", "A  A ");
            }else if (cha == 'O'){
                buildChar(l, w, mat, direction, " AA  ", "A  A ", "A  A ", "A  A ", " AA  ");
            }else if (cha == 'P'){
                buildChar(l, w, mat, direction, "A    ", "A    ", "AAAA ", "A  A ", "AAAA ");
            }else if (cha == 'Q'){
                buildChar(l, w, mat, direction, "AAA A", "A  A ", "A  A ", "A  A ", " AAA ");
                if(additionX < 0 || additionZ < 0){
                    if (additionX != 0) x = x + additionX - 1;
                    if (additionZ != 0) z = z + additionZ - 1;
                } else {
                    if (additionX != 0) x = x + additionX + 1;
                    if (additionZ != 0) z = z + additionZ + 1;
                }
                continue;
            }else if (cha == 'R'){
                buildChar(l, w, mat, direction, "A A  ", "AA   ", "AAA  ", "A A  ", "AAA  ");
                if(additionX < 0 || additionZ < 0){
                    if (additionX != 0) x = x + additionX + 1;
                    if (additionZ != 0) z = z + additionZ + 1;
                } else {
                    if (additionX != 0) x = x + additionX - 1;
                    if (additionZ != 0) z = z + additionZ - 1;
                }
                continue;
            }else if (cha == 'S'){
                buildChar(l, w, mat, direction, "AAA  ", "  A  ", "AAA  ", "A    ", "AAA  ");
                if(additionX < 0 || additionZ < 0){
                    if (additionX != 0) x = x + additionX + 1;
                    if (additionZ != 0) z = z + additionZ + 1;
                } else {
                    if (additionX != 0) x = x + additionX - 1;
                    if (additionZ != 0) z = z + additionZ - 1;
                }
                continue;
            }else if (cha == 'T'){
                buildChar(l, w, mat, direction, " A   ", " A   ", " A   ", " A   ", "AAAA ");
            }else if (cha == 'U'){
                buildChar(l, w, mat, direction, "AAAA ", "A  A ", "A  A ", "A  A ", "A  A ");
            }else if (cha == 'V'){
                buildChar(l, w, mat, direction, " A   ", "A A  ", "A A  ", "A A  ", "A A  ");
                if(additionX < 0 || additionZ < 0){
                    if (additionX != 0) x = x + additionX + 1;
                    if (additionZ != 0) z = z + additionZ + 1;
                } else {
                    if (additionX != 0) x = x + additionX - 1;
                    if (additionZ != 0) z = z + additionZ - 1;
                }
                continue;
            }else if (cha == 'W'){
                buildChar(l, w, mat, direction, " A A ", "A A A", "A A A", "A A A", "A A A");
                if(additionX < 0 || additionZ < 0){
                    if (additionX != 0) x = x + additionX - 1;
                    if (additionZ != 0) z = z + additionZ - 1;
                } else {
                    if (additionX != 0) x = x + additionX + 1;
                    if (additionZ != 0) z = z + additionZ + 1;
                }
                continue;
            }else if (cha == 'X'){
                buildChar(l, w, mat, direction, "A   A", " A A ", "  A  ", " A A ", "A   A");
            }else if (cha == 'Y'){
                buildChar(l, w, mat, direction, " A   ", " A   ", " A   ", "A A  ", "A A  ");
                if(additionX < 0 || additionZ < 0){
                    if (additionX != 0) x = x + additionX + 1;
                    if (additionZ != 0) z = z + additionZ + 1;
                } else {
                    if (additionX != 0) x = x + additionX - 1;
                    if (additionZ != 0) z = z + additionZ - 1;
                }
                continue;
            }else if (cha == 'Z'){
                buildChar(l, w, mat, direction, "AAAAA", "   A ", "  A  ", " A   ", "AAAAA");
                if(additionX < 0 || additionZ < 0){
                    if (additionX != 0) x = x + additionX - 1;
                    if (additionZ != 0) z = z + additionZ - 1;
                } else {
                    if (additionX != 0) x = x + additionX + 1;
                    if (additionZ != 0) z = z + additionZ + 1;
                }
                continue;
            }else if (cha == '1'){
                buildChar(l, w, mat, direction, "AAA  ", " A   ", " A   ", "AA   ", " A   ");
                if(additionX < 0 || additionZ < 0){
                    if (additionX != 0) x = x + additionX + 1;
                    if (additionZ != 0) z = z + additionZ + 1;
                } else {
                    if (additionX != 0) x = x + additionX - 1;
                    if (additionZ != 0) z = z + additionZ - 1;
                }
                continue;
            }else if (cha == '2'){
                buildChar(l, w, mat, direction, "AAAA ", " A   ", "  A  ", "A  A ", " AA  ");
            }else if (cha == '3'){
                buildChar(l, w, mat, direction, "AAA  ", "  A  ", " AA  ", "  A  ", "AAA  ");
                if(additionX < 0 || additionZ < 0){
                    if (additionX != 0) x = x + additionX + 1;
                    if (additionZ != 0) z = z + additionZ + 1;
                } else {
                    if (additionX != 0) x = x + additionX - 1;
                    if (additionZ != 0) z = z + additionZ - 1;
                }
                continue;
            }else if (cha == '4'){
                buildChar(l, w, mat, direction, "   A ", "   A ", "AAAA ", "A  A ", "A  A ");
            }else if (cha == '5'){
                buildChar(l, w, mat, direction, "AAAA ", "   A ", "AAAA ", "A    ", "AAAA ");
            }else if (cha == '6'){
                buildChar(l, w, mat, direction, "AAAA ", "A  A ", "AAAA ", "A    ", "AAAA ");
            }else if (cha == '7'){
                buildChar(l, w, mat, direction, "   A ", "   A ", " AAA ", "   A ", "AAAA ");
            }else if (cha == '8'){
                buildChar(l, w, mat, direction, " AA  ", "A  A ", " AA  ", "A  A ", " AA  ");
            }else if (cha == '9'){
                buildChar(l, w, mat, direction, "AAAA ", "   A ", "AAAA ", "A  A ", "AAAA ");
            } else if (cha == '0'){
                buildChar(l, w, mat, direction, "AAAA ", "A  A ", "A  A ", "A  A ", "AAAA ");
            }else if (cha == '.'){
                buildChar(l, w, mat, direction, "A    ", "     ", "     ", "     ", "     ");
                if(additionX < 0 || additionZ < 0){
                    if (additionX != 0) x = x + additionX + 3;
                    if (additionZ != 0) z = z + additionZ + 3;
                } else {
                    if (additionX != 0) x = x + additionX - 3;
                    if (additionZ != 0) z = z + additionZ - 3;
                }
                continue;
            }else if (cha == ':'){
                buildChar(l, w, mat, direction, "A    ", "     ", "     ", "A    ", "     ");
                if(additionX < 0 || additionZ < 0){
                    if (additionX != 0) x = x + additionX + 3;
                    if (additionZ != 0) z = z + additionZ + 3;
                } else {
                    if (additionX != 0) x = x + additionX - 3;
                    if (additionZ != 0) z = z + additionZ - 3;
                }
                continue;
            } else if (cha == ';'){
                buildChar(l, w, mat, direction, "A   ", "A    ", "     ", "A    ", "     ");
                if(additionX < 0 || additionZ < 0){
                    if (additionX != 0) x = x + additionX + 3;
                    if (additionZ != 0) z = z + additionZ + 3;
                } else {
                    if (additionX != 0) x = x + additionX - 3;
                    if (additionZ != 0) z = z + additionZ - 3;
                }
                continue;
            }else if (cha == '+'){
                buildChar(l, w, mat, direction, " A   ", "AAA  ", " A   ", "     ", "     ");
                if(additionX < 0 || additionZ < 0){
                    if (additionX != 0) x = x + additionX + 1;
                    if (additionZ != 0) z = z + additionZ + 1;
                } else {
                    if (additionX != 0) x = x + additionX - 1;
                    if (additionZ != 0) z = z + additionZ - 1;
                }
                continue;
            }else if (cha == '-'){
                buildChar(l, w, mat, direction, "     ", "AAA  ", "     ", "     ", "     ");
                if(additionX < 0 || additionZ < 0){
                    if (additionX != 0) x = x + additionX + 1;
                    if (additionZ != 0) z = z + additionZ + 1;
                } else {
                    if (additionX != 0) x = x + additionX - 1;
                    if (additionZ != 0) z = z + additionZ - 1;
                }
                continue;
            } else if (cha == '/'){
                buildChar(l, w, mat, direction, "A    ", " A   ", "  A  ", "   A ", "    A");
                if(additionX < 0 || additionZ < 0){
                    if (additionX != 0) x = x + additionX - 1;
                    if (additionZ != 0) z = z + additionZ - 1;
                } else {
                    if (additionX != 0) x = x + additionX + 1;
                    if (additionZ != 0) z = z + additionZ + 1;
                }
                continue;
            }else if (cha == ','){
                buildChar(l, w, mat, direction, "A    ", "A    ", "     ", "     ", "     ");
                if(additionX < 0 || additionZ < 0){
                    if (additionX != 0) x = x + additionX + 3;
                    if (additionZ != 0) z = z + additionZ + 3;
                } else {
                    if (additionX != 0) x = x + additionX - 3;
                    if (additionZ != 0) z = z + additionZ - 3;
                }
                continue;
            }else if (cha == '*'){
                buildChar(l, w, mat, direction, "     ", "     ", "A A  ", " A   ", "A A  ");
                if(additionX < 0 || additionZ < 0){
                    if (additionX != 0) x = x + additionX + 1;
                    if (additionZ != 0) z = z + additionZ + 1;
                } else {
                    if (additionX != 0) x = x + additionX - 1;
                    if (additionZ != 0) z = z + additionZ - 1;
                }
                continue;
            } else if (cha == '_'){
                buildChar(l, w, mat, direction, "AAAA ", "     ", "     ", "     ", "     ");
            }else if (cha == '!'){
                buildChar(l, w, mat, direction, "A    ", "     ", "A    ", "A    ", "A    ");
                if(additionX < 0 || additionZ < 0){
                    if (additionX != 0) x = x + additionX + 3;
                    if (additionZ != 0) z = z + additionZ + 3;
                } else {
                    if (additionX != 0) x = x + additionX - 3;
                    if (additionZ != 0) z = z + additionZ - 3;
                }
                continue;
            }else if (cha == '?'){
                buildChar(l, w, mat, direction, " A   ", "     ", " A   ", "  A  ", "AAA  ");
                if(additionX < 0 || additionZ < 0){
                    if (additionX != 0) x = x + additionX + 1;
                    if (additionZ != 0) z = z + additionZ + 1;
                } else {
                    if (additionX != 0) x = x + additionX - 1;
                    if (additionZ != 0) z = z + additionZ - 1;
                }
                continue;
            } else if (cha == '='){
                buildChar(l, w, mat, direction, "AAA  ", "     ", "AAA  ", "     ", "     ");
                if(additionX < 0 || additionZ < 0){
                    if (additionX != 0) x = x + additionX + 1;
                    if (additionZ != 0) z = z + additionZ + 1;
                } else {
                    if (additionX != 0) x = x + additionX - 1;
                    if (additionZ != 0) z = z + additionZ - 1;
                }
                continue;
            }   else {
                buildChar(l, w, mat, direction, "     ", "     ", "     ", "     ", "     ");
            }

            x = x + additionX;
            z = z + additionZ;
        }
    }


}
