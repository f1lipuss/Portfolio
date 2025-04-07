package pl.filipus.filipusBackupy.listener

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import pl.filipus.filipusBackupy.data.PlayerBackup
import pl.filipus.filipusBackupy.manager.BackupManager

class DeathListener(private val backupManager: BackupManager) : Listener {

    @EventHandler
    fun onPlayerDeath(event: PlayerDeathEvent) {
        val player = event.entity
        val inventory = player.inventory.contents
        val armor = player.inventory.armorContents

        val backup = PlayerBackup(
            timestamp = System.currentTimeMillis(),
            inventoryContents = inventory,
            armorContents = armor
        )

        backupManager.addBackup(player.uniqueId, backup)
    }
}
