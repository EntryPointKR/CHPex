package com.aczchef.chpex.exception;

import com.laytonsmith.PureUtilities.Version;
import com.laytonsmith.annotations.typeof;
import com.laytonsmith.core.CHVersion;
import com.laytonsmith.core.constructs.Target;
import com.laytonsmith.core.exceptions.CRE.CREException;

/**
 * Created by deide on 2016-12-05.
 */
@typeof("NotPlayerException")
public class CRENotPlayerException extends CREException {
    public CRENotPlayerException(String msg, Target t) {
        super(msg, t);
    }

    public String docs() {
        return "Thrown if not player.";
    }

    public Version since() {
        return CHVersion.V3_3_2;
    }
}
