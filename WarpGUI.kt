package pl.filipus.warpywgui.gui

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.Listener
import org.bukkit.event.EventHandler
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import pl.filipus.warpywgui.config.WarpConfig
import pl.filipus.warpywgui.model.WarpItem
import pl.filipus.warpywgui.warpyplugin

object WarpGUI : Listener {

    private val guiTitle get() = WarpConfig.guiTitle
    private val guiSize get() = WarpConfig.guiSize

    private val yellowSlots = listOf(0, 8, 36, 44)
    private val orangeSlots = listOf(1, 7, 9, 17, 27, 35, 37, 43)
    private val whiteSlots = listOf(2, 3, 5, 6, 18, 26, 38, 39, 41, 42)

    fun open(player: Player) {
        val inv: Inventory = Bukkit.createInventory(null, guiSize, guiTitle)

        for (slot in yellowSlots) {
            if (slot < guiSize) inv.setItem(slot, createGlassPane(Material.YELLOW_STAINED_GLASS_PANE))
        }
        for (slot in orangeSlots) {
            if (slot < guiSize) inv.setItem(slot, createGlassPane(Material.ORANGE_STAINED_GLASS_PANE))
        }
        for (slot in whiteSlots) {
            if (slot < guiSize) inv.setItem(slot, createGlassPane(Material.WHITE_STAINED_GLASS_PANE))
        }

        for (warp in WarpConfig.warps) {
            if (warp.slot in 0 until guiSize) {
                val item = warp.toItemStack()
                inv.setItem(warp.slot, item)
            }
        }

        player.openInventory(inv)
    }

    private fun createGlassPane(material: Material): ItemStack {
        return ItemStack(material).apply {
            val meta = itemMeta
            meta?.setDisplayName(" ")
            itemMeta = meta
        }
    }

    init {
        Bukkit.getPluginManager().registerEvents(this, warpyplugin.instance)
    }

    @EventHandler
    fun onClick(e: InventoryClickEvent) {
        val player = e.whoClicked as? Player ?: return
        if (e.view.title != guiTitle) return

        e.isCancelled = true
        val clickedItem = e.currentItem ?: return

        val warp = WarpConfig.warps.find { it.slot == e.slot } ?: return

        player.closeInventory()
        player.performCommand(warp.command)
    }
}
