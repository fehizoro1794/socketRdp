package run;

import javax.swing.JOptionPane;

import server.Server;

public class ServerMain{
    public static void main(String[] args) {
        // String ip = JOptionPane.showInputDialog("Please enter pc ip you want to accede:");
        // ServerFrame serverRDP=new ServerFrame();
        // serverRDP.setVisible(true);
        // new Init
        Server server=new Server(9111);
    }
}