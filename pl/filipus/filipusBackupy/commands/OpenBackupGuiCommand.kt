package pl.filipus.filipusBackupy.command

import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import pl.filipus.filipusBackupy.gui.BackupGui
import pl.filipus.filipusBackupy.manager.BackupManager

class OpenBackupGuiCommand(private val backupManager: BackupManager) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) return true
        if (args.isEmpty()) {
            sender.sendMessage("§c /nadajbackup <gracz>")
            return true
        }

        val target = Bukkit.getPlayer(args[0])
        if (target == null) {
            sender.sendMessage("§cNie znaleziono gracza ${args[0]}")
            return true
        }

        val backups = backupManager.getBackups(target.uniqueId)
        if (backups.isEmpty()) {
            sender.sendMessage("§cGracz ${target.name} nie ma zadnych backapow.")
            return true
        }

        val gui = BackupGui(target, backups, backupManager)
        gui.open(sender)

        return true
    }
}
