package pl.filipus.filipusBackupy.data

import org.bukkit.inventory.ItemStack

data class PlayerBackup(
    val timestamp: Long,
    val inventoryContents: Array<ItemStack?>,
    val armorContents: Array<ItemStack?>
)
