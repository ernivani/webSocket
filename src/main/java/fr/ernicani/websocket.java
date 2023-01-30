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


            String[] params = data.split("&");

            for (String param : params) {
                Bukkit.getConsoleSender().sendMessage("§b[WebSocket] Paramètre reçu : " + param);
                if (param.startsWith("player=")) {
                    param = param.replace("player=", "");
                    params[1] = param;
                }
                else if (param.startsWith("command=")) {
                    param = param.replace("command=", "");
                    params[0] = param;
                }
            }
            String cmd = params[0];
            String playerName = params[1];
            Player p;

            p = Bukkit.getPlayer(playerName);

            if (p == null) {
                Bukkit.getConsoleSender().sendMessage("§c[WebSocket] Le joueur "+ playerName +" n'est pas connecter !");
                // TODO: Stcoket dans un fichier yaml et
                //  verifier a chaque connection d'un joueur si il a des commandes en attentes dans le fichier yaml
                return;
            }

            Bukkit.getConsoleSender().sendMessage("§b[WebSocket] Commande reçu : " + cmd);
            Bukkit.getConsoleSender().sendMessage("§b[WebSocket] Joueur reçu : " + p.getDisplayName());


            cmd = cmd.replace("player", p.getDisplayName());
            Bukkit.getConsoleSender().sendMessage("§e[WebSocket] Commande finis : " + cmd);


            try {
                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), cmd);
            }
            catch (Exception e) {
                Bukkit.getConsoleSender().sendMessage("§c[WebSocket] Erreur survenue lors de l'exécution de la commande : " + e);
                e.printStackTrace();
            }
        }
    }
}
