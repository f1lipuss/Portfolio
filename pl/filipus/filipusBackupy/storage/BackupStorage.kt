package pl.filipus.filipusBackupy.storage

import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.inventory.ItemStack
import pl.filipus.filipusBackupy.data.PlayerBackup
import java.io.File
import java.util.*

class BackupStorage(private val plugin: org.bukkit.plugin.java.JavaPlugin) {

    private val backupFolder: File = File(plugin.dataFolder, "backupy").apply {
        if (!exists()) mkdirs()
    }

    fun saveBackup(playerId: UUID, backups: List<PlayerBackup>) {
        val playerFolder = File(backupFolder, playerId.toString()).apply {
            if (!exists()) mkdirs()
        }

        backups.forEachIndexed { index, backup ->
            val backupFile = File(playerFolder, "backup${index + 1}.yml")
            val backupConfig = YamlConfiguration()

            backupConfig.set("timestamp", backup.timestamp)
            backupConfig.set("inventoryContents", serializeItemStackArray(backup.inventoryContents))
            backupConfig.set("armorContents", serializeItemStackArray(backup.armorContents))

            backupConfig.save(backupFile)
        }
    }

    fun loadBackups(): Map<UUID, List<PlayerBackup>> {
        val backupsMap = mutableMapOf<UUID, List<PlayerBackup>>()

        backupFolder.listFiles()?.forEach { playerFolder ->
            if (playerFolder.isDirectory) {
                val playerId = UUID.fromString(playerFolder.name)
                val playerBackups = mutableListOf<PlayerBackup>()

                playerFolder.listFiles()?.forEachIndexed { index, backupFile ->
                    if (backupFile.extension == "yml") {
                        val backupConfig = YamlConfiguration.loadConfiguration(backupFile)
                        val timestamp = backupConfig.getLong("timestamp")
                        val inventoryContents = deserializeItemStackArray(backupConfig.getList("inventoryContents") as? List<Map<String, Any>> ?: emptyList())
                        val armorContents = deserializeItemStackArray(backupConfig.getList("armorContents") as? List<Map<String, Any>> ?: emptyList())

                        val playerBackup = PlayerBackup(timestamp, inventoryContents, armorContents)
                        playerBackups.add(playerBackup)
                    }
                }

                backupsMap[playerId] = playerBackups
            }
        }

        return backupsMap
    }

    private fun serializeItemStackArray(items: Array<ItemStack?>): List<Map<String, Any?>> {
        return items.mapNotNull { item ->
            item?.serialize()
        }
    }

    private fun deserializeItemStackArray(serializedItems: List<Map<String, Any>>): Array<ItemStack?> {
        return serializedItems.mapNotNull { itemMap ->
            ItemStack.deserialize(itemMap)
        }.toTypedArray()
    }
}
