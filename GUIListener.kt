package pl.filipus.zarzadzaniechatem.listeners

import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import pl.filipus.zarzadzaniechatem.zarzadzaniechatem

class GUIListener : Listener {

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        val player = event.whoClicked
        if (player is Player) {
            if (event.view.title == "Zarzadzaj chatem") {
                event.isCancelled = true

                if (!player.hasPermission("chat.manage.cmd")) {
                    player.sendMessage("§6§lCHAT§8 » §fNie masz uprawnien do uzycia tego!")
                    return
                }

                when (event.slot) {
                    11 -> {
                        if (zarzadzaniechatem.chatEnabled) {
                            player.sendMessage("§6§lCHAT§8 » §fChat jest juz wlaczony!")
                        } else {
                            zarzadzaniechatem.chatEnabled = true
                            player.sendMessage("§6§lCHAT§8 » §fChat został wlaczony!")
                            player.playSound(player.location, Sound.ENTITY_FIREWORK_ROCKET_BLAST, 1f, 1f)
                        }
                    }
                    13 -> {
                        if (!zarzadzaniechatem.chatEnabled) {
                            player.sendMessage("§6§lCHAT§8 » §fChat jest juz wylaczony!")
                        } else {
                            zarzadzaniechatem.chatEnabled = false
                            player.sendMessage("§6§lCHAT§8 » §fChat zostal wylaczony!")
                            player.playSound(player.location, Sound.ENTITY_FIREWORK_ROCKET_BLAST, 1f, 1f)
                        }
                    }
                    15 -> {
                        for (i in 0..700) {
                            player.server.broadcastMessage("")
                        }
                        player.sendMessage("§6§lCHAT§8 » §fChat zostal wyczyszczony!")
                        player.playSound(player.location, Sound.ENTITY_FIREWORK_ROCKET_BLAST, 1f, 1f)
                    }
                }
            }
        }
    }
}
