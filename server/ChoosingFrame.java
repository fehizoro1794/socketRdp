package gui;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import listener.ChoiceListener;

import java.awt.*;

public class ChoosingFrame extends Thread{
    ArrayList<JButton> btIpList=new ArrayList<>(); 
    ServerSocket serverSocket;
    Socket clientSocket;
    JPanel panel=new JPanel();
    JFrame frame=new JFrame();
    
    public ChoosingFrame(int port) throws Exception{
        this.serverSocket=new ServerSocket(port);
        start();
        // this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // this.setLocationRelativeTo(null);
        // this.setSize(200, 300);
        // panel.setLayout(new FlowLayout());

        // run();

        // JButton[] btIp=new JButton[btIpList.size()];
        // for (int i = 0; i < btIpList.size(); i++) {
        //     btIp[i]=(JButton)btIpList.get(i);
        //     btIp[i].addActionListener(new ChoiceListener(this,btIp[i].getText()));

        //     panel.add(btIp[i]);
        // }

        // this.add(panel);
        
    }
    @Override
    public void run() {
        try {
            while (true) {
                drawIHM();

                clientSocket=serverSocket.accept();
                this.btIpList.add(new JButton(clientSocket.getRemoteSocketAddress().toString()));
            }
        } catch (Exception e) {
            System.out.println("Error :ChoosingFrame.java p49");
            e.printStackTrace();
        }
    }
    public void drawIHM(){
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setSize(200, 300);
        panel.setLayout(new FlowLayout());

        if (btIpList.size()!=0 && !btIpList.isEmpty()) {
            JButton[] btIp=new JButton[btIpList.size()];
            for (int i = 0; i < btIpList.size(); i++) {
                btIp[i]=(JButton)btIpList.get(i);
                btIp[i].addActionListener(new ChoiceListener(this,btIp[i].getText()));

                panel.add(btIp[i]);
            }   
        }

        frame.add(panel);
        frame.setVisible(true);
    }
    public static void main(String[] args) {
        try {
            new ChoosingFrame(9111);   
        } catch (Exception e) {
            System.out.println("Error :ChoosingFrame.java p58");
            e.printStackTrace();
        }
    }
}
