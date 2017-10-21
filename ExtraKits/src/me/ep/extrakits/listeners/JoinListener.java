package me.ep.extrakits.listeners;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.ep.extrakits.ExtraKits;

public class JoinListener implements Listener{

	ExtraKits pl;
	String[] update;
	String version;
	public JoinListener(ExtraKits pl) {
		this.pl = pl;
		Bukkit.getPluginManager().registerEvents(this, pl);
		version = pl.getDescription().getVersion();
	}
	
	@EventHandler
	public void aoEntrar(PlayerJoinEvent e) throws MalformedURLException{			
		Player p = e.getPlayer();	
		if (pl.getConfig().getBoolean("verify_updates")){
			if (atualizar(version)){
				String atualVersion = pl.getDescription().getVersion();
				String newVersion = update[1];
				if (pl.language.isBr()){				
					if (p.hasPermission("extrakits.update") || p.isOp()){
						p.sendMessage("");
						p.sendMessage("§b[Extra§3Kits] §eHa uma atualiçao disponivel!");
						p.sendMessage("§b[Extra§3Kits] §eVersao Atual: §6" + atualVersion);
						p.sendMessage("§b[Extra§3Kits] §eNova Versao: §6" + newVersion);
						p.sendMessage("§b[Extra§3Kits] §eBaixe em: §6https://www.spigotmc.org/resources/extrakits-easy-to-create-a-kit.31158/");
						p.sendMessage("");
					}
				}else {
					if (p.hasPermission("extrakits.update") || p.isOp()){
						p.sendMessage("");
						p.sendMessage("§b[Extra§3Kits] §eUpdate Found!");
						p.sendMessage("§b[Extra§3Kits] §eCurrent Version: §6" + atualVersion);
						p.sendMessage("§b[Extra§3Kits] §eNew Version: §6" + newVersion);
						p.sendMessage("§b[Extra§3Kits] §eDownload in: §6https://www.spigotmc.org/resources/extrakits-easy-to-create-a-kit.31158/");
						p.sendMessage("");
					}
				}
			}
		}
	}
	
	private boolean atualizar(String version){		
		try {
			URL site = new URL("http://extraplays.esy.es/plugins/atualizar.php?plugin=ExtraKits&version=" + version);			
			InputStreamReader isr = new InputStreamReader(site.openStream());
			BufferedReader reader = new BufferedReader(isr);			
			update = reader.readLine().split(";");
			if (update[0].equalsIgnoreCase("true")){
				return true;
			}else if (update[0].equalsIgnoreCase("false")){
				return false;
			}
		} catch (IOException e1) {}	
		return false;
	}

}
