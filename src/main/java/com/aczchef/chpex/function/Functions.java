/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aczchef.chpex.function;

import com.aczchef.chpex.exception.CRENotPlayerException;
import com.laytonsmith.abstraction.MCCommandSender;
import com.laytonsmith.abstraction.MCPlayer;
import com.laytonsmith.annotations.api;
import com.laytonsmith.core.Static;
import com.laytonsmith.core.constructs.CArray;
import com.laytonsmith.core.constructs.CBoolean;
import com.laytonsmith.core.constructs.CInt;
import com.laytonsmith.core.constructs.CString;
import com.laytonsmith.core.constructs.CVoid;
import com.laytonsmith.core.constructs.Construct;
import com.laytonsmith.core.constructs.Target;
import com.laytonsmith.core.environments.CommandHelperEnvironment;
import com.laytonsmith.core.environments.Environment;
import com.laytonsmith.core.exceptions.CRE.CREInvalidPluginException;
import com.laytonsmith.core.exceptions.CRE.CRENotFoundException;
import com.laytonsmith.core.exceptions.CRE.CREThrowable;
import com.laytonsmith.core.exceptions.ConfigRuntimeException;
import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.util.Collections;
import java.util.Set;

/**
 * @author cgallarno
 */
public class Functions {

    @api
    public static class pex_get_group_users extends PexFunction {

        public String getName() {
            return "pex_get_group_users";

        }

        public Integer[] numArgs() {
            return new Integer[]{1};
        }

        public Construct exec(Target t, Environment env, Construct... args) throws ConfigRuntimeException {
            Static.checkPlugin("PermissionsEx", t);
            PermissionManager pex = PermissionsEx.getPermissionManager();
            String g;
            CArray cusers = new CArray(t);
            if (args[0] instanceof CString) {
                g = args[0].val();
            } else {
                return CVoid.VOID;
            }
            Set<PermissionUser> users = pex.getUsers(g);

            for (PermissionUser user : users) {
                cusers.push(new CString(user.getName(), t), t);
            }
            return cusers;
        }

        public String docs() {
            return "array {group} returns an array of all users in a group.";
        }
    }

    @api
    public static class pex_get_groups extends PexFunction {

        public Construct exec(Target t, Environment env, Construct... args) throws ConfigRuntimeException {
            Static.checkPlugin("PermissionsEx", t);
            PermissionManager pex = PermissionsEx.getPermissionManager();
            CArray ret = CArray.GetAssociativeArray(t);
            for (PermissionGroup g : pex.getGroups()) {
                CArray details = CArray.GetAssociativeArray(t);
                details.set("rank", new CInt(g.getRank(), t), t);
                details.set("ladder", new CString(g.getRankLadder(), t), t);
                details.set("prefix", new CString(g.getOwnPrefix(), t), t);
                details.set("suffix", new CString(g.getOwnSuffix(), t), t);
                ret.set(g.getName(), details, t);
            }
            return ret;
        }

        public String docs() {
            return "array {} Returns all groups with their rank, rank-ladder, prefix, and suffix.";
        }

        public String getName() {
            return "pex_get_groups";
        }

        public Integer[] numArgs() {
            return new Integer[]{0};
        }

    }

    @api
    public static class pex_get_user_info extends PexFunction {

        public Construct exec(Target t, Environment environment, Construct... args) throws ConfigRuntimeException {
            Static.checkPlugin("PermissionsEx", t);
            PermissionManager pex = PermissionsEx.getPermissionManager();
            CArray ret = CArray.GetAssociativeArray(t);
            PermissionUser pexuser;
            boolean groupPrefix = false;
            if (args.length == 1) {
                if (args[0] instanceof CBoolean) {
                    pexuser = pex.getUser(environment.getEnv(CommandHelperEnvironment.class).GetPlayer().getName());
                    groupPrefix = ((CBoolean) args[0]).getBoolean();
                } else {
                    pexuser = pex.getUser(args[0].val());
                }
            } else if (args.length == 2) {
                pexuser = pex.getUser(args[0].val());
                groupPrefix = ((CBoolean) args[1]).getBoolean();
            } else {
                pexuser = pex.getUser(environment.getEnv(CommandHelperEnvironment.class).GetPlayer().getName());
            }
            if (groupPrefix) {
                ret.set("prefix", new CString(pexuser.getPrefix(), t), t);
                ret.set("suffix", new CString(pexuser.getSuffix(), t), t);
            } else {
                ret.set("prefix", new CString(pexuser.getOwnPrefix(), t), t);
                ret.set("suffix", new CString(pexuser.getOwnSuffix(), t), t);
            }
            return ret;
        }

        public String getName() {
            return "pex_get_user_info";
        }

        public Integer[] numArgs() {
            return new Integer[]{0, 1, 2};
        }

        public String docs() {
            return "array {[player], [group prefix]} Returns player info. Such as prefix, and suffix. Group prefix is a boolean that will determine wether to check groups for the prefix, True means it will, defaults to false.";
        }
    }

    @api
    public static class pex_set_group extends PexFunction {
        public Construct exec(Target t, Environment environment, Construct... args) throws ConfigRuntimeException {
            String playerStr;
            String groupStr;
            if (args.length > 1) {
                playerStr = args[0].val();
                groupStr = args[1].val();
            } else {
                MCCommandSender sender = environment.getEnv(CommandHelperEnvironment.class).GetCommandSender();
                if (!(sender instanceof MCPlayer)) {
                    throw new CRENotPlayerException(sender.getName() + " is not Player.", t);
                }
                playerStr = environment.getEnv(CommandHelperEnvironment.class).GetCommandSender().getName();
                groupStr = args[0].val();
            }
            PermissionUser permUser = PermissionsEx.getUser(playerStr);
            permUser.setParents(Collections.singletonList(
                    PermissionsEx.getPermissionManager().getGroup(groupStr)));
            return CVoid.VOID;
        }

        public String getName() {
            return "pex_set_group";
        }

        public Integer[] numArgs() {
            return new Integer[]{1, 2};
        }

        public String docs() {
            return "void {[player], group} Set player's group";
        }

        public Class<? extends CREThrowable>[] thrown() {
            return new Class[]{CREInvalidPluginException.class, CRENotFoundException.class};
        }
    }
}
