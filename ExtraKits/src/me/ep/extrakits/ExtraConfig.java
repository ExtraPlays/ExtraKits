package me.ep.extrakits;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ExtraConfig {
	
	private File arq;
	public ExtraKits m;
	private String configName;
	private FileConfiguration fileconfig;
	/* Cria a Config */
	public ExtraConfig(String file, ExtraKits m){
		this.m = m;
		this.arq = new File(m.getDataFolder(), file);
		this.fileconfig = YamlConfiguration.loadConfiguration(this.arq);				
		this.configName = file;
		if (m.getResource(arq.getName()) != null){
			 m.saveResource(arq.getName(), false);			 
		}else {
			try {
				arq.createNewFile();
			} catch (Exception e) {
				e.printStackTrace();
			}
		 }
		
	}	
	/* Pega a config */
	public FileConfiguration config(){
		this.fileconfig = YamlConfiguration.loadConfiguration(this.arq);
		return this.fileconfig;		
	}	
	/* Salva a config */
	public void save(){
		try {			
			fileconfig.save(this.arq);
		} catch (Exception e) {
			Bukkit.getConsoleSender().sendMessage("[ExtraConfig] Nao foi Possivel Salvar a " + this.configName);
			e.printStackTrace();
		}
	}

	/* Recarrega a config
	 * Sem Bugs Para Recarregar 
	 * Recarrega de Primeira */
	public void reloadConfig(){
		this.arq = new File(m.getDataFolder(), this.configName);
		try {
			this.fileconfig.load(this.arq);
		} catch (FileNotFoundException e) {
			System.out.println("[ExtraConfig] Config nao Encontrada");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			System.out.println("[ExtraConfig] Configuracao invalida");
			e.printStackTrace();
		}
	}
}
