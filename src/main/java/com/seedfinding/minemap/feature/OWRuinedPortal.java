package com.seedfinding.minemap.feature;

import com.seedfinding.mcfeature.structure.RuinedPortal;
import com.seedfinding.mccore.state.Dimension;
import com.seedfinding.mccore.version.MCVersion;

public class OWRuinedPortal extends RuinedPortal {

    public OWRuinedPortal(MCVersion version) {
        super(Dimension.OVERWORLD, version);
    }

    public OWRuinedPortal(Config config, MCVersion version) {
        super(Dimension.OVERWORLD, config, version);
    }

    public static String name() {
        return "OW_ruined_portal";
    }

    @Override
    public String getName() {
        return name();
    }

    @Override
    public boolean isValidDimension(Dimension dimension) {
        return dimension == Dimension.OVERWORLD;
    }

}
