package pl.filipus.filipusBackupy.gui

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import pl.filipus.filipusBackupy.data.PlayerBackup
import pl.filipus.filipusBackupy.manager.BackupManager
import java.text.SimpleDateFormat
import java.util.*

class BackupGui(
    private val target: Player,
    private val backups: List<PlayerBackup>,
    private val manager: BackupManager
) : Listener {

    private val guiTitle = "Backupy gracza ${target.name}"

    fun open(viewer: Player) {
        val inventory: Inventory = Bukkit.createInventory(null, 54, guiTitle)
        val dateFormat = SimpleDateFormat("dd.MM.yyyy")
        val timeFormat = SimpleDateFormat("HH:mm:ss")

        for ((index, backup) in backups.withIndex()) {
            if (index >= 54) break

            val dateStr = dateFormat.format(Date(backup.timestamp))
            val timeStr = timeFormat.format(Date(backup.timestamp))

            val item = ItemStack(Material.CHEST)
            val meta = item.itemMeta!!
            meta.setDisplayName(
                ChatColor.translateAlternateColorCodes('&',
                    "&6Zgon z dnia:&8 (&f${dateStr}&8) &8(&f${timeStr}&8)"
                )
            )
            meta.lore = listOf(
                ChatColor.translateAlternateColorCodes('&',
                    "&7Nacisnij, aby nadac backup"
                )
            )
            item.itemMeta = meta

            inventory.setItem(index, item)
        }

        viewer.openInventory(inventory)

        val plugin = pl.filipus.filipusBackupy.FilipusBackupy.instance

        val guiListener = object : Listener {
            @EventHandler
            fun onInventoryClick(e: InventoryClickEvent) {
                if (e.whoClicked.uniqueId != viewer.uniqueId) return
                if (e.view.title != guiTitle) return

                e.isCancelled = true

                val slot = e.rawSlot
                if (slot < 0 || slot >= backups.size) return

                val selectedBackup = backups[slot]
                manager.setPendingBackup(target.uniqueId, selectedBackup)

                viewer.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&aPoprawnie nadano backup graczowi &f${target.name}"
                ))

                target.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&aPomyslnie nadano Ci backupa!&f /odbierzbackup"
                ))

                viewer.closeInventory()

                InventoryClickEvent.getHandlerList().unregister(this)
            }
        }

        Bukkit.getPluginManager().registerEvents(guiListener, plugin)
    }
}
