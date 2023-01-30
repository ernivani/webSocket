package fr.ernicani;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public final class websocket extends JavaPlugin implements PluginMessageListener {

    private ServerSocket serverSocket;
    private Thread listenThread;


    @Override
    public void onEnable() {
        try {
            checkIfBungee();
            Bukkit.getConsoleSender().sendMessage("§b[WebSocket] Plugin activé !");
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage("§c[WebSocket] Erreur survenue lors de l'activation du plugin : " + e);
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage("§c[WebSocket] Plugin désactivé !");
        try {
            serverSocket.close();
            listenThread.interrupt();
        } catch (IOException e) {
            Bukkit.getConsoleSender().sendMessage("§c[WebSocket] Erreur survenue lors de la fermeture du serveur de socket : " + e);
            e.printStackTrace();
        }
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
            Bukkit.getConsoleSender().sendMessage("§b[WebSocket] Message reçu : " + data);

        }
    }

    // we check like that if the specified server is BungeeCord.
    private void checkIfBungee()
    {
        if ( !getServer().spigot().getConfig().getConfigurationSection("settings").getBoolean( "settings.bungeecord" ) )
        {
            getLogger().severe( "This server is not BungeeCord." );
            getLogger().severe( "If the server is already hooked to BungeeCord, please enable it into your spigot.yml aswell." );
            getLogger().severe( "Plugin disabled!" );
            getServer().getPluginManager().disablePlugin( this );
        }
    }


}
