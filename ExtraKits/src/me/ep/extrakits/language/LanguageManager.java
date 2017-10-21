package me.ep.extrakits.language;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import me.ep.extrakits.ExtraKits;

public class LanguageManager {	
	
	String language;
	FileConfiguration languageConfig;
	
	ExtraKits pl;
	public LanguageManager(String language, ExtraKits pl) {
		this.pl = pl;	
		this.language = language;
	}
	
	public boolean setLanguage(){
		if (getLanguage().equalsIgnoreCase("pt-br")){
			Bukkit.getLogger().info("[ExtraKits] Linguagem definida para: " + getLanguage());
			this.languageConfig = pl.pt_br.config();
			return true;
		}else if (getLanguage().equalsIgnoreCase("en")){
			Bukkit.getLogger().info("[ExtraKits] Language is set to: " + getLanguage());
			this.languageConfig = pl.en.config();
			return true;
		}else {
			Bukkit.getLogger().severe("[ExtraKits] Language not found, Plugin Disabled");
			Bukkit.getLogger().severe("[ExtraKits] Available Languages: 'pt-br'; 'en'");
			Bukkit.getPluginManager().disablePlugin(pl);
			return false;
		}		
	}
	
	public String getLanguage(){
		return pl.getConfig().getString("language");
	}
	
	public FileConfiguration getLanguageConfig() {
		return languageConfig;
	}
	
	public boolean isBr(){
		if (getLanguage().equalsIgnoreCase("pt-br")){
			return true;
		}else{
			return false;
		}			
	}

}
