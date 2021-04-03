package kaptainwutax.minemap.ui.menubar;

import kaptainwutax.minemap.MineMap;
import kaptainwutax.minemap.feature.SpawnPoint;
import kaptainwutax.minemap.init.KeyShortcuts;
import kaptainwutax.minemap.listener.Events;
import kaptainwutax.minemap.ui.dialog.CoordHopperDialog;
import kaptainwutax.minemap.ui.dialog.SaltDialog;
import kaptainwutax.minemap.ui.dialog.StructureHopperDialog;
import kaptainwutax.minemap.ui.map.MapPanel;
import kaptainwutax.minemap.ui.map.icon.IconRenderer;
import kaptainwutax.minemap.ui.map.icon.SpawnIcon;
import kaptainwutax.seedutils.mc.Dimension;
import kaptainwutax.seedutils.mc.pos.BPos;
import kaptainwutax.seedutils.mc.seed.WorldSeed;

import javax.swing.*;
import java.util.Collections;

import static kaptainwutax.minemap.config.KeyboardsConfig.getKeyComboString;

public class WorldMenu extends Menu {
    private final JMenuItem goToCoords;
    private final JMenuItem goToSpawn;
    private final JMenuItem loadShadowSeed;
    private final JMenuItem goToStructure;
    private final JMenuItem changeSalts;

    public WorldMenu() {
        this.menu = new JMenu("World");

        this.goToCoords = new JMenuItem("Go to Coordinates");
        this.goToCoords.addMouseListener(Events.Mouse.onPressed(e -> SwingUtilities.invokeLater(goToCoords()))); // this can wait

        this.goToSpawn = new JMenuItem("Go to Spawn");
        this.goToSpawn.addMouseListener(Events.Mouse.onPressed(e -> SwingUtilities.invokeLater(goToSpawn()))); // this can wait

        this.goToStructure = new JMenuItem("Go to Structure");
        this.goToStructure.addMouseListener(Events.Mouse.onPressed(e -> SwingUtilities.invokeLater(goToStructure()))); // this can wait

        this.loadShadowSeed = new JMenuItem("Load Shadow Seed");
        this.loadShadowSeed.addMouseListener(Events.Mouse.onPressed(e -> loadShadowSeed().run())); // this needs to run immediately

        this.goToCoords.setEnabled(false);
        this.goToSpawn.setEnabled(false);
        this.loadShadowSeed.setEnabled(false);
        this.goToStructure.setEnabled(false);

        this.menu.addMenuListener(Events.Menu.onSelected(e -> {
            MapPanel map = MineMap.INSTANCE.worldTabs.getSelectedMapPanel();
            this.goToCoords.setEnabled(map != null);
            this.goToSpawn.setEnabled(map != null && this.getActiveSpawn() != null);
            this.loadShadowSeed.setEnabled(map != null && map.getContext().dimension == Dimension.OVERWORLD);
            this.goToStructure.setEnabled(map != null);
        }));

        this.changeSalts = new JMenuItem("Change Salts");
        this.changeSalts.addMouseListener(Events.Mouse.onPressed(e -> SwingUtilities.invokeLater(changeSalts())));

        this.menu.add(goToCoords);
        this.menu.add(goToSpawn);
        this.menu.add(goToStructure);
        this.menu.add(loadShadowSeed);
        this.menu.add(changeSalts);
    }

    public Runnable goToCoords() {
        return () -> {
            if (!this.goToCoords.isEnabled()) return;
            this.activate.run();
            JDialog jumpDialogue = new CoordHopperDialog(this.deactivate);
            jumpDialogue.setVisible(true);
        };
    }

    public Runnable goToSpawn() {
        return () -> {
            if (!this.goToSpawn.isEnabled()) return;
            BPos pos = this.getActiveSpawn();
            if (pos != null) {
                MineMap.INSTANCE.worldTabs.getSelectedMapPanel().getManager().setCenterPos(pos.getX(), pos.getZ());
            }
        };
    }

    public Runnable loadShadowSeed() {
        return () -> {
            if (!this.loadShadowSeed.isEnabled()) return;
            MapPanel map = MineMap.INSTANCE.worldTabs.getSelectedMapPanel();
            MineMap.INSTANCE.worldTabs.load(
                    map.getContext().version,
                    String.valueOf(WorldSeed.getShadowSeed(map.getContext().worldSeed)),
                    map.threadCount, Collections.singletonList(map.getContext().dimension));
        };
    }

    public Runnable goToStructure() {
        return () -> {
            if (!this.goToStructure.isEnabled()) return;
            this.activate.run();
            JDialog jumpDialogue = new StructureHopperDialog(this.deactivate);
            jumpDialogue.setVisible(true);
        };
    }

    public Runnable changeSalts() {
        return () -> {
            SaltDialog dialog;
            try {
                this.activate.run();
                dialog = new SaltDialog(this.deactivate);
                dialog.setVisible(true);
            } catch (Exception exception) {
                this.deactivate.run();
                exception.printStackTrace();
            }
        };
    }

    private BPos getActiveSpawn() {
        MapPanel map = MineMap.INSTANCE.worldTabs.getSelectedMapPanel();
        IconRenderer icon = map.getContext().getIconManager().getFor(SpawnPoint.class);
        return icon instanceof SpawnIcon ? ((SpawnIcon) icon).getPos() : null;
    }

    @Override
    public void doDelayedLabels() {
        this.goToCoords.setText(String.format("Go to Coordinates (%s)", getKeyComboString(KeyShortcuts.ShortcutAction.GO_TO_COORDS)));
        this.goToSpawn.setText(String.format("Go to Spawn (%s)", getKeyComboString(KeyShortcuts.ShortcutAction.GO_TO_SPAWN)));
        this.goToStructure.setText(String.format("Go to Structure (%s)", getKeyComboString(KeyShortcuts.ShortcutAction.GO_TO_STRUCTURE)));
        this.loadShadowSeed.setText(String.format("Load Shadow Seed (%s)", getKeyComboString(KeyShortcuts.ShortcutAction.LOAD_SHADOW_SEED)));
        this.changeSalts.setText(String.format("Change Salts (%s)", getKeyComboString(KeyShortcuts.ShortcutAction.CHANGE_SALTS)));
    }
}