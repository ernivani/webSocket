package fr.ernicani;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public final class websocket extends JavaPlugin {

    private ServerSocket serverSocket;
    private Thread listenThread;


    @Override
    public void onEnable() {
        try {
            Bukkit.getConsoleSender().sendMessage("§b[WebSocket] Plugin activé !");
            run();
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

    private void run() {
        try {
            serverSocket = new ServerSocket(1234);
        } catch (IOException e) {
            Bukkit.getConsoleSender().sendMessage("§c[WebSocket] Erreur survenue lors de la création du serveur de socket : " + e);
            e.printStackTrace();
            return;
        }
        Bukkit.getConsoleSender().sendMessage("§a[WebSocket] Écoute activée sur le port 1234");

        listenThread = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Bukkit.getConsoleSender().sendMessage("§c[WebSocket] Erreur survenue lors de la mise en veille du thread d'écoute : " + e);
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
            while (!Thread.interrupted()) {
                try {
                    Socket socket = serverSocket.accept();
                    // Bukkit.getConsoleSender().sendMessage("§a[WebSocket] Socket : " + socket);
                    BufferedReader socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String request = socketReader.readLine();
                    // Bukkit.getConsoleSender().sendMessage("§b[WebSocket] Requête reçue : " + request);
                    String[] parts = request.split("\\?");
                    String[] params = parts[1].split("&");
                    String ip = "";
                    String command = "";
                    for (String param : params) {
                        if (param.startsWith("ip=")) {
                            ip = param.substring(3).replaceAll("%3A", ":");
                        } else if (param.startsWith("command=")) {
                            command = param.substring(8).replaceAll("%20", " ");
                        }
                    }

                    Bukkit.getConsoleSender().sendMessage("§b[WebSocket] IP : " + ip);
                    Bukkit.getConsoleSender().sendMessage("§b[WebSocket] Commande : " + command);

//                    TODO : Send command to the current ip server on the bungeecord network
                    

                    socket.close();

                } catch (IOException e) {
                    Bukkit.getConsoleSender().sendMessage("§c[WebSocket] Erreur survenue lors de la réception du socket : " + e);
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }
        });
        listenThread.start();
    }


}
