package pl.filipus.filipusBackupy

import org.bukkit.plugin.java.JavaPlugin
import pl.filipus.filipusBackupy.command.OdbierzBackupCMD
import pl.filipus.filipusBackupy.command.NadajBackupCMD
import pl.filipus.filipusBackupy.listener.DeathListener
import pl.filipus.filipusBackupy.manager.BackupManager
import pl.filipus.filipusBackupy.storage.BackupStorage

class FilipusBackupy : JavaPlugin() {

    private lateinit var backupManager: BackupManager
    private lateinit var backupStorage: BackupStorage

    companion object {
        lateinit var instance: FilipusBackupy
            private set
    }

    override fun onEnable() {
        instance = this

        backupStorage = BackupStorage(this)
        backupManager = BackupManager(backupStorage)
        backupManager.loadBackupsFromStorage()

        getCommand("nadajbackup")?.setExecutor(NadajBackupCMD(backupManager))
        getCommand("odbierzbackup")?.setExecutor(OdbierzBackupCMD(backupManager))

        server.pluginManager.registerEvents(DeathListener(backupManager), this)


        logger.info("FilipusBackupy wlaczony")
    }

    override fun onDisable() {
        logger.info("FilipusBackupy wylaczony")
    }
}
