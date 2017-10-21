package me.ep.extrakits.commands;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import me.ep.extrakits.ExtraKits;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class KitCommand implements CommandExecutor{
		
	ExtraKits plugin;
	public KitCommand(ExtraKits plugin){				
		this.plugin = plugin;
		Bukkit.getPluginCommand("criarkit").setExecutor(this);
		Bukkit.getPluginCommand("kit").setExecutor(this);
		Bukkit.getPluginCommand("apagarkit").setExecutor(this);	
	}
		
	ArrayList<String> pkits;
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			String prefixo = "";	
			FileConfiguration msg = plugin.language.getLanguageConfig();
		if (plugin.language.isBr()){
			prefixo = msg.getString("Kits.Prefixo").replace("&", "§");			
		}else {
			prefixo = msg.getString("Kits.Prefix").replace("&", "§");
		}
		if (cmd.getName().equalsIgnoreCase("criarkit")){
			Player p = (Player)sender;
			if (args.length < 2){
				if (p.hasPermission("extrakits.admin")){
					p.sendMessage(prefixo + "§c/criarkit <nome> <cooldown>");
					return true;
				}else {
					p.sendMessage(prefixo + "§cSem Permissao");
					return true;
				}
			}else if (args.length >2){
				if (p.hasPermission("extrakits.admin")){
					p.sendMessage(prefixo + "§c/criarkit <nome> <cooldown> ");
					return true;
				}else {
					p.sendMessage(prefixo + "§cSem Permissao");
					return true;
				}
			}else if (args.length == 2){
				if (p.hasPermission("extrakits.admin")){
					String nome = args[0];
					int cooldown = Integer.parseInt(args[1]);
					if (plugin.manager.kitExiste(nome)){
						if (plugin.language.isBr()){
							p.sendMessage(prefixo + msg.getString("Kits.Ja_Existe")
									.replace("{kit}", nome).replace("&", "§"));
							return true;
						}else {
							p.sendMessage(prefixo + msg.getString("Kits.Already_exists")
								.replace("{kit}", nome).replace("&", "§"));						
							return true;
						}
					}else {
						plugin.manager.criarKit(p, nome, cooldown);
						if (plugin.language.isBr()){
							p.sendMessage(prefixo + msg.getString("Kits.Criado")
									.replace("{kit}", nome).replace("&", "§"));
							return true;
						}else {
							p.sendMessage(prefixo + msg.getString("Kits.Kit_Created")
								.replace("{kit}", nome).replace("&", "§"));
							return true;
						}
					}
				}else {
					p.sendMessage(prefixo + "§cSem Permissao");
					return true;
				}
			}
		}else if (cmd.getName().equalsIgnoreCase("kit")){
			Player p = (Player)sender;
			if (args.length == 0){
				if (plugin.kits.config().getConfigurationSection("Kits") != null){
					pkits = new ArrayList<String>();
					for(String kit : plugin.kits.config().getConfigurationSection("Kits").getKeys(false)){
						if (p.hasPermission("extrakits.kit." + kit)){						
							pkits.add(kit);
						}
					}				
					if (plugin.language.isBr()){
						p.sendMessage(prefixo + "§eSeus Kits: §6" + pkits.toString().replace("[", "").replace("]", ""));
						return true;
					}else {
						p.sendMessage(prefixo + "§eYour Kits: §6" + pkits.toString().replace("[", "").replace("]", ""));
						return true;
					}
				}else {
					if (plugin.language.isBr()){
						p.sendMessage(prefixo + "§eSeus Kits: §6Nenhum...");
						return true;
					}else {
						p.sendMessage(prefixo + "§eYour Kits: §6None...");
						return true;
					}
				}
			}else if (args.length == 1){
				String nome = args[0];				
				int delay = plugin.kits.config().getInt("Kits." + nome + ".Cooldown");
				if (plugin.manager.kitExiste(nome)){
					if (p.hasPermission("extrakits.kit." + nome)){
						if (!plugin.c.temDelay(p.getName() + "." + nome)) {							
							if (plugin.getConfig().getBoolean("empty_inventory")){
								if (plugin.manager.inventoryIsEmpty(p.getInventory())){
									plugin.c.addDelay(p.getName() + "." + nome);
									
									if (plugin.language.isBr()){
										p.sendMessage(prefixo + msg.getString("Kits.Pegou")
											.replace("{kit}", nome).replace("&", "§"));
										plugin.manager.darKit(p, nome);
										return true;
									}else {
										p.sendMessage(prefixo + msg.getString("Kits.Kit_Received")
											.replace("{kit}", nome).replace("&", "§"));
										plugin.manager.darKit(p, nome);
										return true;
									}
								}else {
									if(plugin.language.isBr()){
										p.sendMessage(prefixo + msg.getString("Kits.Esvazie_O_Inventario")
											.replace("{kit}", nome).replace("&", "§"));
										return true;
									}else {
										p.sendMessage(prefixo + msg.getString("Kits.Empty_Your_Inventory")
											.replace("{kit}", nome).replace("&", "§"));
										return true;
									}
								}
							}else {
								plugin.c.addDelay(p.getName() + "." + nome);
								if (plugin.language.isBr()){
									p.sendMessage(prefixo + msg.getString("Kits.Pegou")
											.replace("{kit}", nome).replace("&", "§"));
									plugin.manager.darKit(p, nome);
									return true;
								}else {
									p.sendMessage(prefixo + msg.getString("Kits.Kit_Received")
										.replace("{kit}", nome).replace("&", "§"));
									plugin.manager.darKit(p, nome);
									return true;
								}
							}							
						} else {
							if (plugin.c.acabouDelay(p.getName() + "." + nome, TimeUnit.SECONDS.toMillis(delay))) {								
								plugin.c.removeDelay(p.getName() + "." + nome);
								plugin.c.addDelay(p.getName() + "." + nome);
								plugin.manager.darKit(p, nome);
								if (plugin.language.isBr()){
									p.sendMessage(prefixo + msg.getString("Kits.Pegou")
										.replace("{kit}", nome).replace("&", "§"));													
									return true;								
								}else {
									p.sendMessage(prefixo + msg.getString("Kits.Kit_Received")
										.replace("{kit}", nome).replace("&", "§"));									
									return true;
								}
							} else {
								if (plugin.language.isBr()){
									p.sendMessage(prefixo + msg.getString("Kits.Em_Delay")
											.replace("{delay}", plugin.c.getDelayString(p.getName() + "." + nome, 
													TimeUnit.SECONDS.toMillis(delay))).replace("&", "§"));							
									return true;
								}else {
									p.sendMessage(prefixo + msg.getString("Kits.Delay_Not_Finished")
										.replace("{delay}", plugin.c.getDelayString(p.getName() + "." + nome, 
											TimeUnit.SECONDS.toMillis(delay))).replace("&", "§"));							
									return true;									
								}
							}							
						}
					}else {
						if (plugin.language.isBr()){
							p.sendMessage(prefixo + msg.getString("Kits.Sem_Permissao")
								.replace("{kit}", nome).replace("&", "§"));
								return true;
						}else {
							p.sendMessage(prefixo + msg.getString("Kits.Without_permission")
								.replace("{kit}", nome).replace("&", "§"));
							return true;
						}
					}
				}else {
					if (plugin.language.isBr()){
						p.sendMessage(prefixo + msg.getString("Kits.Nao_Existe")
							.replace("{kit}", nome).replace("&", "§"));
						return true;
					}else {
						p.sendMessage(prefixo + msg.getString("Kits.Kit_Not_Exist")
							.replace("{kit}", nome).replace("&", "§"));
						return true;
					}
				}
			}
		}else if (cmd.getName().equalsIgnoreCase("apagarkit")){
			Player p = (Player)sender;
			if (p.hasPermission("extrakits.admin")){
				if (args.length == 1){
					String nome = args[0];
					if (plugin.manager.kitExiste(nome)){
						plugin.manager.apagarKit(nome);
						if (plugin.language.isBr()){
							p.sendMessage(prefixo + msg.getString("Kits.Apagou")
								.replace("{kit}", nome).replace("&", "§"));
							return true;
						}else {
							p.sendMessage(prefixo + msg.getString("Kits.Kit_Deleted")
								.replace("{kit}", nome).replace("&", "§"));
							return true;
						}
					}else {
						if (plugin.language.isBr()){
							p.sendMessage(prefixo + msg.getString("Kits.Nao_Existe")
								.replace("{kit}", nome).replace("&", "§"));
							return true;
						}else {
							p.sendMessage(prefixo + msg.getString("Kits.Kit_Not_Exist")
								.replace("{kit}", nome).replace("&", "§"));
							return true;
						}
					}
				}
			}else {
				p.sendMessage(prefixo + "§cSem Permissao");
			}
		}
		return false;
	}

}