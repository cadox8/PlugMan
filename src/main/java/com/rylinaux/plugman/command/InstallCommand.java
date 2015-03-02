package com.rylinaux.plugman.command;

/*
 * #%L
 * PlugMan
 * %%
 * Copyright (C) 2010 - 2015 PlugMan
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */


import com.rylinaux.plugman.PlugMan;
import com.rylinaux.plugman.util.BukgetUtil;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import org.json.JSONObject;

/**
 * Command that installs plugins from DBO.
 *
 * @author rylinaux
 */
public class InstallCommand extends AbstractCommand {

    /**
     * The name of the command.
     */
    public static final String NAME = "Install";

    /**
     * The description of the command.
     */
    public static final String DESCRIPTION = "Install a plugin from DBO.";

    /**
     * The main permission of the command.
     */
    public static final String PERMISSION = "plugman.install";

    /**
     * The proper usage of the command.
     */
    public static final String USAGE = "/plugman install [plugin]";

    /**
     * The sub permissions of the command.
     */
    public static final String[] SUB_PERMISSIONS = {""};

    /**
     * Construct out object.
     *
     * @param sender the command sender
     */
    public InstallCommand(CommandSender sender) {
        super(sender, NAME, DESCRIPTION, PERMISSION, SUB_PERMISSIONS, USAGE);
    }

    /**
     * Execute the command
     *
     * @param sender  the sender of the command
     * @param command the command being done
     * @param label   the name of the command
     * @param args    the arguments supplied
     */
    @Override
    public void execute(CommandSender sender, Command command, String label, String[] args) {

        if (!hasPermission()) {
            sender.sendMessage(PlugMan.getInstance().getMessenger().format("error.no-permission"));
            return;
        }

        if (args.length < 2) {
            sender.sendMessage(PlugMan.getInstance().getMessenger().format("error.specify-plugin"));
            sendUsage();
            return;
        }

        String slug = args[1];
        String version = "latest";

        if (args.length > 2)
            version = args[2];

        JSONObject json = BukgetUtil.getPluginData(slug, version);

        if (json.has("error")) {
            sender.sendMessage(PlugMan.getInstance().getMessenger().format("error.invalid-slug"));
            return;
        }

        String pluginName = json.getString("plugin_name");
        String downloadUrl = json.getJSONArray("versions").getJSONObject(0).getString("download");

        BukgetUtil.downloadPlugin(pluginName, downloadUrl);

    }

}
