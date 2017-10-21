package me.ep.extrakits;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

import me.ep.extrakits.commands.KitCommand;
import me.ep.extrakits.cooldown.Cooldown;
import me.ep.extrakits.kit.KitsManager;
import me.ep.extrakits.language.LanguageManager;
import me.ep.extrakits.listeners.JoinListener;

public class ExtraKits extends JavaPlugin implements Listener{
	
	public ExtraConfig msg;
	public KitsManager manager;	
	public Cooldown c;
	public ExtraConfig kits, pt_br, en;
	public LanguageManager language;
	
	public void onEnable(){
		
		new KitCommand(this);
		new JoinListener(this);
		Bukkit.getPluginManager().registerEvents(this, this);
					
		manager = new KitsManager(this);
		kits = new ExtraConfig("kits.yml", this);
		pt_br = new ExtraConfig("language_Pt-Br.yml", this);
		en = new ExtraConfig("language_En.yml", this);		
		
		saveDefaultConfig();		
		
		language = new LanguageManager(getConfig().getString("language") ,this);		
		language.setLanguage();
		
		if (language.isBr()){
			getLogger().info("Habilitado Com Sucesso.");
			getLogger().info("Versao(" + getDescription().getVersion() + ")");
		}else {
			getLogger().info("Successfully Enabled(" + getDescription().getVersion() + ")");
			getLogger().info("Version(" + getDescription().getVersion() + ")");
		}
		
		File f = new File(getDataFolder(), "cooldown.yml");
		if (!f.exists()){
			try {
				f.createNewFile();
			} catch (IOException e) {}
		}
		FileConfiguration config = YamlConfiguration.loadConfiguration(f);
		c = new Cooldown(config, f);										
	}
	
	public void onDisable() {

	}
	
	void reloadConfigs(){
		kits.reloadConfig();
		pt_br.reloadConfig();
		en.reloadConfig();
		reloadConfig();
	}
	
	@EventHandler
	public void comando(PlayerCommandPreprocessEvent e){
		if (e.getMessage().equalsIgnoreCase("/extrakits reload") || e.getMessage().equalsIgnoreCase("/extrakits rl")){
			reloadConfigs();
			language.setLanguage();
			if (language.isBr()){
				e.getPlayer().sendMessage("§8[§bExtra§3Kits§8] §aTodos os Arquivos Foram Recarregados.");
				e.setCancelled(true);
			}else {
				e.getPlayer().sendMessage("§8[§bExtra§3Kits§8] §aAll Files have been reloaded.");
				e.setCancelled(true);
			}
		}
	}
	
	
}
