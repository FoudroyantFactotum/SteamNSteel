/*
 * Copyright (C) 2014  Kihira
 *
 *     This program is free software; you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation; either version 2 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 */

package mod.steamnsteel.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.util.BlockPos;

import java.util.List;

/**
 * The command for spawning entities that has support for mod added entities
 */
public class CommandSpawnEntity extends CommandBase {

    @Override
    public String getCommandName()
    {
        return "spawnentity";
    }

    @Override
    public String getCommandUsage(ICommandSender var1)
    {
        return "/spawnentity <entityname> [amount]";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args)
    {
        if (args.length > 0)
        {
            final String entityName = args[0];

            if (args.length == 2)
            {
                for (int i = 0; i < Integer.parseInt(args[1]); i++)
                {
                    final Entity entity = EntityList.createEntityByName(entityName, sender.getEntityWorld());
                    entity.setPosition(sender.getPosition().getX(), sender.getPosition().getY(), sender.getPosition().getZ());

                    sender.getEntityWorld().spawnEntityInWorld(entity);
                }
            }
            else
            {
                final Entity entity = EntityList.createEntityByName(entityName, sender.getEntityWorld());
                entity.setPosition(sender.getPosition().getX(), sender.getPosition().getY(), sender.getPosition().getZ());

                sender.getEntityWorld().spawnEntityInWorld(entity);
            }
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
    {
        //Gather a list of the entity names
        final String[] entityNameList =
                EntityList.stringToClassMapping.keySet().toArray(new String[EntityList.stringToClassMapping.size()]);

        return args.length > 0 ?
                getListOfStringsMatchingLastWord(args, entityNameList) :
                null;
    }

    /*public int compareTo(Object par1Obj) {
        return this.compareTo((ICommand)par1Obj);
    }*/
}
