package com.aczchef.chpex.function;

import com.laytonsmith.PureUtilities.Version;
import com.laytonsmith.core.CHVersion;
import com.laytonsmith.core.exceptions.CRE.CREInvalidPluginException;
import com.laytonsmith.core.exceptions.CRE.CREThrowable;
import com.laytonsmith.core.functions.AbstractFunction;

/**
 * Created by deide on 2016-12-05.
 */
public abstract class PexFunction extends AbstractFunction {
    public Class<? extends CREThrowable>[] thrown() {
        return new Class[]{CREInvalidPluginException.class};
    }

    public boolean isRestricted() {
        return true;
    }

    public Boolean runAsync() {
        return false;
    }

    public Version since() {
        return CHVersion.V3_3_2;
    }
}
