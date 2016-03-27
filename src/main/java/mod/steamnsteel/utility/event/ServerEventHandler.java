package mod.steamnsteel.utility.event;

import mod.steamnsteel.entity.SwarmManager;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ServerEventHandler
{
    public static final String SWARM = "swarms";

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event)
    {
        if (!event.world.isRemote)
        {
            SwarmManager swarmManager = (SwarmManager) event.world.getPerWorldStorage().loadData(SwarmManager.class, SWARM);

            if (swarmManager == null)
            {
                swarmManager = new SwarmManager(event.world);
                event.world.getPerWorldStorage().setData(SWARM, swarmManager);
            }
            else
            {
                swarmManager.setWorld(event.world);
            }

            SwarmManager.swarmManagers.put(event.world, swarmManager);
        }
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event)
    {
        if (!event.world.isRemote)
        {
            SwarmManager sm = SwarmManager.swarmManagers.remove(event.world);

            event.world.getPerWorldStorage().setData(SWARM, sm);
        }
    }

    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event)
    {
        if (event.side.isServer() && event.phase == TickEvent.Phase.END && SwarmManager.swarmManagers.containsKey(event.world))
        {
            SwarmManager.swarmManagers.get(event.world).tick();
        }
    }
}
