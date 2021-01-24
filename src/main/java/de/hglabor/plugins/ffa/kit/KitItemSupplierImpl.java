package de.hglabor.plugins.ffa.kit;

import de.hglabor.plugins.kitapi.kit.AbstractKit;
import de.hglabor.plugins.kitapi.kit.KitItemSupplier;
import de.hglabor.plugins.kitapi.player.KitPlayer;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class KitItemSupplierImpl implements KitItemSupplier {
    public final static KitItemSupplierImpl INSTANCE = new KitItemSupplierImpl();

    private KitItemSupplierImpl() {
    }

    @Override
    public void giveKitItems(KitPlayer kitPlayer, AbstractKit abstractKit) {

    }

    @Override
    public void giveItems(KitPlayer kitPlayer, List<ItemStack> list) {

    }
}
