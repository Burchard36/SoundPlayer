package com.burchard36.plugin.gui;

import com.burchard36.plugin.SoundListener;
import com.burchard36.plugin.SoundPlayerPlugin;
import com.burchard36.plugin.musepluse.gui.GuiManager;
import com.burchard36.plugin.musepluse.gui.InventoryGui;
import com.burchard36.plugin.musepluse.gui.PaginatedInventory;
import com.burchard36.plugin.musepluse.gui.buttons.InventoryButton;
import com.burchard36.plugin.musepluse.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

import static com.burchard36.plugin.musepluse.utils.StringUtils.convert;

public class SoundsGui extends PaginatedInventory {
    protected final GuiManager guiManager;
    protected final Player player;
    protected final SoundListener soundListener;
    protected Sound lastSound = null;
    protected ItemStack lastSoundItem = ItemUtils.createItemStack(Material.BARRIER, "&eLast Played Sound", "&f", "&7The most recent sound you played will be displayed here!");
    protected float playingPitch = 1.0F;

    public SoundsGui(SoundPlayerPlugin pluginInstance, final Player openingPlayer) {
        this.player = openingPlayer;
        this.soundListener = new SoundListener(this.player);
        this.guiManager = pluginInstance.getGuiManager();
        final List<Sound> availableSounds = List.of(Sound.values());
        int totalPages = (int) Math.ceil((double) availableSounds.size() / 45D);
        if (totalPages == 0) totalPages = 1;

        AtomicInteger itemsAdded = new AtomicInteger(0);
        for (int currentPage = 0; currentPage < totalPages; currentPage++) {
            final int finalCurrentPage = currentPage;
            int finalTotalPages = totalPages;
            this.addPage(finalCurrentPage, new InventoryGui() {
                @Override
                public Inventory createInventory() {
                    return Bukkit.createInventory(
                            null,
                            54,
                            convert("&3&lSong List Page %s").formatted(finalCurrentPage + 1));
                }

                @Override
                public void onInventoryClick(InventoryClickEvent event) {
                    event.setCancelled(true);
                    super.onInventoryClick(event);
                }

                @Override
                public void fillButtons() {
                    for (int x = 0; x < 45; x++) {
                        if (itemsAdded.get() >= availableSounds.size()) continue;
                        final Sound sound = availableSounds.get(itemsAdded.getAndAdd(1));
                        this.addButton(x, new InventoryButton(getSoundDataDisplayItem(sound)) {
                            @Override
                            public void onClick(InventoryClickEvent clickEvent) {
                                final Player player = ((Player) clickEvent.getWhoClicked());
                                final ClickType clickType = clickEvent.getClick();
                                final Inventory clickedInventory = clickEvent.getClickedInventory();
                                assert clickedInventory != null;
                                if (clickType == ClickType.LEFT) {
                                    lastSound = sound;
                                    lastSoundItem = clickEvent.getCurrentItem();
                                    clickedInventory.setItem(48, lastSoundItem);
                                    player.playSound(player.getLocation(), sound, 1.0f, playingPitch);
                                    player.updateInventory();
                                }
                            }
                        });
                    }

                    this.addButton(46, backgroundItem());
                    this.addButton(47, backgroundItem());
                    this.addButton(50, backgroundItem());
                    this.addButton(51, backgroundItem());
                    this.addButton(52, backgroundItem());
                    this.addButton(53, new InventoryButton(getNextButton()) {
                        @Override
                        public void onClick(InventoryClickEvent clickEvent) {
                            final Player player = (Player) clickEvent.getWhoClicked();
                            int nextPage = finalCurrentPage + 1;
                            if (finalTotalPages == 1 || nextPage >= finalTotalPages) {
                                player.playSound(player, Sound.ENTITY_VILLAGER_NO, 1.0F, 1.0F);
                                player.sendMessage(convert("&cThis is the final page"));
                                return;
                            }

                            player.closeInventory();
                            guiManager.openPaginatedTo(player, nextPage, SoundsGui.this);
                        }
                    });
                    this.addButton(45, new InventoryButton(getPreviousButton()) {
                        @Override
                        public void onClick(InventoryClickEvent clickEvent) {
                            final Player player = (Player) clickEvent.getWhoClicked();
                            int previousPage = finalCurrentPage - 1;
                            if (finalTotalPages == 1 || previousPage < 0) {
                                player.playSound(player, Sound.ENTITY_VILLAGER_NO, 1.0F, 1.0F);
                                player.sendMessage(convert("&cThis is the first page"));
                                return;
                            }

                            player.closeInventory();
                            guiManager.openPaginatedTo(player, previousPage, SoundsGui.this);
                        }
                    });

                    this.addButton(48, new InventoryButton(lastSoundItem) {
                        @Override
                        public void onClick(InventoryClickEvent clickEvent) {
                            if (lastSound != null) {
                                player.playSound(player, lastSound, 1.0f, playingPitch);
                                player.updateInventory();
                            }
                        }
                    });
                    this.addButton(49, new InventoryButton(getIncrementButton()) {
                        @Override
                        public void onClick(InventoryClickEvent clickEvent) {
                            final Inventory clickedInventory = clickEvent.getClickedInventory();
                            assert clickedInventory != null;
                            switch (clickEvent.getClick()) {

                                case SHIFT_LEFT -> {
                                    playingPitch += 0.1f;
                                    if (playingPitch > 2.0F) playingPitch = 2.0f;
                                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.5f, playingPitch);

                                }
                                case LEFT -> {
                                    playingPitch += 0.01f;
                                    if (playingPitch > 2.0F) playingPitch = 2.0f;
                                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.5f, playingPitch);
                                }

                                case SHIFT_RIGHT -> {
                                    playingPitch -= 0.10f;
                                    if (playingPitch < 0.0F) playingPitch = 0.0f;
                                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.75f, playingPitch);
                                }

                                case RIGHT -> {
                                    playingPitch -= 0.01f;
                                    if (playingPitch < 0.0F) playingPitch = 0.0f;
                                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.75f, playingPitch);
                                }

                                case DROP -> {
                                    if (lastSound != null) player.playSound(player.getLocation(), lastSound, 1.0f, playingPitch);
                                }
                            }


                            clickedInventory.setItem(clickEvent.getSlot(), getIncrementButton());
                            player.updateInventory();
                        }
                    });

                    super.fillButtons(); // actually sets them in the inventory
                }

            });
        }
    }

    private InventoryButton backgroundItem() {
        return new InventoryButton(ItemUtils.createItemStack(Material.CYAN_STAINED_GLASS_PANE, "&f ", null)) {
            @Override
            public void onClick(InventoryClickEvent clickEvent) {

            }
        };
    }

    private ItemStack getNextButton() {
        return ItemUtils.createSkull("ff9e19e5f2ce3488c29582b6d2601500626e8db2a88cd18164432fef2e34de6b", "&a&lNEXT PAGE", null);
    }
    private ItemStack getPreviousButton() {
        return ItemUtils.createSkull("f006ec1eca2f2685f70e65411cfe8808a088f7cf08087ad8eece9618361070e3", "&a&lPREVIOUS PAGE", null);
    }

    private ItemStack getIncrementButton() {
        final NumberFormat formatter = new DecimalFormat("0.00");
        return ItemUtils.createSkull("f0a606361ca311961de49a7d0b977108ff33d717ba13dfa8b70ec09cd7512c86", "&b&lCHANGE SOUND PITCH",
                "&f ",
                "&eCurrent Pitch: &7%s".formatted(formatter.format(playingPitch)),
                "&f",
                "&eShift-Left-Click &fadds &b+ &b&l0.1&f to the pitch!",
                "&eLeft-Click &fadds &b+ &b&l0.01&f to the pitch!",
                "&eShift-Right-Click &f subtracts &b- &b&l0.1&f&f to the pitch!",
                "&eRight-Click &f subtracts &b- &b&l0.01&f&f to the pitch!");
    }

    private ItemStack getSoundDataDisplayItem(final Sound sound) {
        final List<Material> discs = List.of(
                Material.MUSIC_DISC_5,
                Material.MUSIC_DISC_11,
                Material.MUSIC_DISC_13,
                Material.MUSIC_DISC_BLOCKS,
                Material.MUSIC_DISC_CAT,
                Material.MUSIC_DISC_CHIRP,
                Material.MUSIC_DISC_FAR,
                Material.MUSIC_DISC_MALL,
                Material.MUSIC_DISC_MELLOHI,
                Material.MUSIC_DISC_OTHERSIDE,
                Material.MUSIC_DISC_STRAD);

        final Material material = discs.get(ThreadLocalRandom.current().nextInt(discs.size() - 1));

        return ItemUtils.createItemStack(material, "&a&l%s".formatted(sound.name()), "&f", "&eLeft Click&7 to play this sound!");
    }
}
