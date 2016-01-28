package com.aczchef.chpex;

import com.laytonsmith.PureUtilities.SimpleVersion;
import com.laytonsmith.PureUtilities.Version;
import com.laytonsmith.core.Static;
import com.laytonsmith.core.constructs.Target;
import com.laytonsmith.core.extensions.AbstractExtension;
import com.laytonsmith.core.extensions.MSExtension;

@MSExtension("CHPex")
public class LifeCycle extends AbstractExtension {

    @Override
    public void onStartup() {
	try {
	    Static.checkPlugin("PermissionsEx", Target.UNKNOWN);
	} catch (Exception e) {
	    System.out.println("[CommandHelper] CHPex Could not find PermissionsEx please make sure you have it installed.");
	}
	System.out.println("[CommandHelper] CHPex Initialized - ACzChef");
    }

    @Override
    public void onShutdown() {
	System.out.println("[CommandHelper] CHPex De-Initialized - ACzChef");
    }
    
    

    public Version getVersion() {
	return new SimpleVersion(1, 1, 1);
    }
}
