package run;

import javax.swing.JOptionPane;

import client.*;

public class ClientMain{
    public static void main(String[] args) {
        String ip = JOptionPane.showInputDialog("Please , enter the Server ip adress:");
        new InitConnection(ip,9111);
    }
}