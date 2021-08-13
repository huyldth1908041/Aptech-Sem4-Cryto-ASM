package com.t1908e.sever;

import com.t1908e.utils.CypherUtil;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Map;

public class ServerMain {
    public static void main(String[] args) {

        String sentence_from_client;
        String sentence_to_client;

        try {
            Map<String, Object> keys = CypherUtil.getRSAKeys();
            PrivateKey privateKey = (PrivateKey) keys.get("private");

            ServerSocket chatSocket = new ServerSocket(8080);

            while (true) {
                System.out.println("waiting for client...");
                Socket connectionSocket = chatSocket.accept();
                InetAddress addr = chatSocket.getInetAddress();
                String clientIP = addr.getHostAddress();
                BufferedReader inFromClient =
                        new BufferedReader(new
                                InputStreamReader(connectionSocket.getInputStream()));


                DataOutputStream outToClient =
                        new DataOutputStream(connectionSocket.getOutputStream());

                sentence_from_client = inFromClient.readLine();
                System.out.println("Client sent: " + sentence_from_client); //decryped text

                String plainMsg = CypherUtil.decryptMessage(sentence_from_client, privateKey);
                sentence_to_client = clientIP + " said: " + plainMsg + '\n';
                outToClient.writeBytes(sentence_to_client);
            }
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }
}
