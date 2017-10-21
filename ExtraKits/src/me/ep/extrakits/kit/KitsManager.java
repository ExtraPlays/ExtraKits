package me.ep.extrakits.kit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import me.ep.extrakits.ExtraKits;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

public class KitsManager {
	
	private ExtraKits plugin;	

	public KitsManager(ExtraKits plugin){
		this.plugin = plugin;
	}
	
	public ExtraKits getPlugin(){
		return plugin;
	}
	
	public boolean kitExiste(String nome){
		FileConfiguration config = getPlugin().kits.config();
		if (config.getConfigurationSection("Kits." + nome) != null){
			return true;
		}
		return false;
	}
	
	public void criarKit(Player p, String nome, int cooldown){		
		PlayerInventory inv = p.getInventory();
		String path = "Kits." + nome + ".";
		FileConfiguration config = getPlugin().kits.config();
		config.createSection("Kits." + nome);
		
		for (int i = 0; i < 36; i++){
            ItemStack is = inv.getItem(i);
            
            if (is == null || is.getType() == Material.AIR)
            	continue;
            
            String slot = path + "Items." + i;
            config.set(slot + ".Tipo", is.getType().toString().toLowerCase());
            config.set(slot + ".Data", is.getDurability());
            config.set(slot + ".Quantidade", is.getAmount());
            
            if (!is.hasItemMeta())
            	continue;            
            if (is.getItemMeta().hasDisplayName())
            	config.set(slot + ".Nome", is.getItemMeta().getDisplayName());            
            if (is.getItemMeta().hasLore())
            	config.set(slot + ".Lore", is.getItemMeta().getLore());            
            if (is.getItemMeta().hasEnchants()){
            	Map<Enchantment, Integer> enchants = is.getEnchantments();
            	List<String> enchantList = new ArrayList<String>();
            	for (Enchantment e : is.getEnchantments().keySet()){
            		int level = enchants.get(e);
            		enchantList.add(e.getName().toLowerCase() + ":" + level);
            	}
            	config.set(slot + ".Encantamentos", enchantList);
            }
            continue;            
		}
		
		config.set(path + "Armadura.Capacete", inv.getHelmet() != null ? inv
				.getHelmet().getType().toString().toLowerCase() : "air");
		config.set(path + "Armadura.Peitoral", inv.getChestplate() != null ? inv
				.getChestplate().getType().toString().toLowerCase() : "air");
		config.set(path + "Armadura.Calca", inv.getLeggings() != null ? inv
				.getLeggings().getType().toString().toLowerCase() : "air");
		config.set(path + "Armadura.Bota", inv.getBoots() != null ? inv
				.getBoots().getType().toString().toLowerCase() : "air");
		config.set(path + "Cooldown", cooldown);
		
		getPlugin().kits.save();
		
	}
	
	public void darKit(Player p, String nome){
		FileConfiguration config = getPlugin().kits.config();
		String path = "Kits." + nome + ".";
		ConfigurationSection s = config.getConfigurationSection(path + ".Items");
		for (String str : s.getKeys(false)){
			int slot = Integer.parseInt(str);
			if (0 > slot && slot > 36)
				return;
			
			String string = path + "Items." + slot + ".";
			String tipo = config.getString(string + "Tipo");
			short data = (short) config.getInt(path + "Data");
			String name = config.getString(string + "Nome");
			List<String> lore = config.getStringList(string + "Lore");
			List<String> enchants = config.getStringList(string + "Encantamentos");
			int quantidade = config.getInt(string + "Quantidade");
						
			ItemStack is = new ItemStack(Material.matchMaterial(tipo.toUpperCase()), quantidade, (byte)data);			
			ItemMeta im = is.getItemMeta();
			
			if (im == null)
				continue;			
			if (name != null)
				im.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));			
			if (lore != null)
				im.setLore(Arrays.asList(ChatColor.translateAlternateColorCodes('&', lore.toString())));
			if (enchants != null){
				for (String s1 : enchants){
					String[] indiEnchants = s1.split(":");
					im.addEnchant(Enchantment.getByName(indiEnchants[0].toUpperCase()), Integer.parseInt(indiEnchants[1]), true);
				}
			}
			
			is.setItemMeta(im);
			p.getInventory().addItem(is);			
		}
		
		String capacete = config.getString(path + "Armadura.Capacete").toUpperCase();
		String peitoral = config.getString(path + "Armadura.Peitoral").toUpperCase();
		String calca = config.getString(path + "Armadura.Calca").toUpperCase();
		String bota = config.getString(path + "Armadura.Bota").toUpperCase();
		
		p.getInventory().addItem(new ItemStack(capacete != null ? Material.matchMaterial(capacete) : Material.AIR));
		p.getInventory().addItem(new ItemStack(peitoral != null ? Material.matchMaterial(peitoral) : Material.AIR));
		p.getInventory().addItem(new ItemStack(calca != null ? Material.matchMaterial(calca) : Material.AIR));
		p.getInventory().addItem(new ItemStack(bota != null ? Material.matchMaterial(bota) : Material.AIR));
		
		p.updateInventory();
		
		
		
	}
	
	public void apagarKit(String nome){
		FileConfiguration config = getPlugin().kits.config();
		ConfigurationSection kits = config.getConfigurationSection("Kits");
		kits.set(nome, null);
		getPlugin().kits.save();
	}
	
	
	public boolean inventoryIsEmpty(PlayerInventory inv){
		for (ItemStack item : inv.getContents()){
			if (item != null)
				return false;						
		}
		return true;
	}
	
	
}
