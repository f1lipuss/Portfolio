package pl.filipus.filipusBackupy.command

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import pl.filipus.filipusBackupy.manager.BackupManager
import java.text.SimpleDateFormat
import java.util.*

class OdbierzBackupCMD(private val backupManager: BackupManager) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) return true

        val backup = backupManager.getPendingBackup(sender.uniqueId)
        if (backup == null) {
            sender.sendMessage("§cNie masz zadnego aktywnego backupa do odebrania.")
            return true
        }

        sender.inventory.contents = backup.inventoryContents
        sender.inventory.armorContents = backup.armorContents
        backupManager.clearPendingBackup(sender.uniqueId)

        val dateFormat = SimpleDateFormat("dd.MM.yyyy")
        val timeFormat = SimpleDateFormat("HH:mm:ss")
        val formattedDate = dateFormat.format(Date(backup.timestamp))
        val formattedTime = timeFormat.format(Date(backup.timestamp))

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
            "&aGratulacje! Odebrales ekwipunek sprzed smierci&8 | (&f$formattedDate&8) (&f$formattedTime&8)"
        ))

        return true
    }
}
