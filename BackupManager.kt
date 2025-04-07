package pl.filipus.filipusBackupy.manager

import pl.filipus.filipusBackupy.data.PlayerBackup
import pl.filipus.filipusBackupy.storage.BackupStorage
import java.util.*

class BackupManager(private val backupStorage: BackupStorage) {
    private val backups = mutableMapOf<UUID, MutableList<PlayerBackup>>()
    private val pendingBackups = mutableMapOf<UUID, PlayerBackup>()
    
    fun addBackup(playerId: UUID, backup: PlayerBackup) {
        val list = backups.computeIfAbsent(playerId) { mutableListOf() }
        
        list.add(0, backup)
        
        if (list.size > 54) list.removeLast()


        backupStorage.saveBackup(playerId, list)
    }


    fun getBackups(playerId: UUID): List<PlayerBackup> {
        return backups[playerId] ?: emptyList()
    }

    fun setPendingBackup(playerId: UUID, backup: PlayerBackup) {
        pendingBackups[playerId] = backup
    }

    fun getPendingBackup(playerId: UUID): PlayerBackup? {
        return pendingBackups[playerId]
    }

    fun clearPendingBackup(playerId: UUID) {
        pendingBackups.remove(playerId)
    }

    fun loadBackupsFromStorage() {
        backupStorage.loadBackups().forEach { (playerId, backupsList) ->
            backups[playerId] = backupsList.toMutableList()
        }
    }
}
