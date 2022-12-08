package server;

import java.net.ServerSocket;
import java.net.Socket;

import javax.imageio.ImageIO;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import eventsocket.*;

import server.*;

public class Server extends Thread{
    ServerSocket serverSocket;
    Socket clientSocket;
    DataInputStream verification;
    int port;
    String width = "", height = "";
    private JDesktopPane desktop = new JDesktopPane();
    JFrame frame=new JFrame();
    private JInternalFrame interFrame = new JInternalFrame(
        "Server Screen",
        true,
        true,	
        true
    );
    private JPanel cPanel = new JPanel();
    
    public Server(int p){
        this.port=p;
        start();
    }
    @Override
    public void run() {
        try { 
                serverSocket=new ServerSocket(this.port);
                while (true) {
                    clientSocket=serverSocket.accept();
                    verification=new DataInputStream(clientSocket.getInputStream());
                    // try {
                        width = verification.readUTF();
                        height = verification.readUTF();
                        System.out.println("------Donnees envoyees par un Client---------");
                        System.out.println("width readUTF: "+width);
                        System.out.println("height readUTF: "+height);
                        System.out.println("---------------------------------------------");
                    // } catch (Exception e) {
                    //     System.out.print("The client socket didn't sent data");
                    //     e.printStackTrace();
                    // }
                    /************Server Frame ********/
                    // ServerFrame serverFrame=new ServerFrame(clientSocket,width,height);
                    InputStream in=null;

                    drawGUI();//create , design the frame
            
                    try {
                        in=clientSocket.getInputStream();
                        System.out.println(/*"This socket has not a problems,see others errors: "+*/clientSocket.getInputStream());
                    } catch (Exception e) {
                        System.out.println("-There's a problem in ServerFrame.java about the clientSocket:");
                        e.printStackTrace();
                    }
                    // new ReceiveScreen(in,cPanel);
                            // while (continueAct) {//continueLoop 
                                int tour=1;
                                /*********Screen capture recuperation******* */
                    while (true) {
                        try {
                            InputStream inputStr=in;
            
                            // BufferedInputStream inputBufImg=new BufferedInputStream(clientSocket.getInputStream());
                            // ByteArrayOutputStream outputByte=new ByteArrayOutputStream();
            
                            // int nRead;
                            // byte[] buffer = new byte[1024 * 1024];
                            // while((nRead = in.read(buffer, 0, buffer.length))!= -1) {
                            //     outputByte.write(buffer);
                            // }
            
                            byte[] bytes = new byte[1024 * 1024];
                            int count = 0;
                            do {
                                count += inputStr.read(bytes, count, bytes.length - count);//get screen image bytes by bytes
                            } while (
                                !(
                                    count > 4 &&
                                    bytes[count - 2] == (byte) -1 &&
                                    bytes[count - 1] == (byte) -39
                                )
                            );
                            // while (!(count > 4 && bytes[count - 2] == (byte) -1 && bytes[count - 1] == (byte) -39)) {
                            //     count += inputStr.read(bytes, count, bytes.length - count);//get screen image bytes by bytes
                            // }
                            ByteArrayInputStream byteIn=new ByteArrayInputStream(bytes);
                            // ByteArrayInputStream byteIn=new ByteArrayInputStream(outputByte.toByteArray());
                            Image image1=ImageIO.read(byteIn);
                            // Image image1=ImageIO.read(byteIn);
                            image1=image1.getScaledInstance(cPanel.getWidth(), cPanel.getHeight(), Image.SCALE_FAST);
                            Graphics graphics=cPanel.getGraphics();
                            graphics.drawImage(image1, 0, 0, cPanel.getWidth(), cPanel.getHeight(), cPanel);
                            // cPanel.repaint();
                            System.out.println("======>Image received from the tour :"+tour);
                            tour++;
                            new SendEvents(clientSocket,cPanel,width,height);
                            // outputByte.close();
                        } catch (Exception e) {
                            System.out.println("Error: Server.java: line 105");
                            e.printStackTrace();
                        }
                    }   
                    
                    // new SendEvents(clientSocket,cPanel,width,height);
                }
            // ServerThread serverThread=new ServerThread(clientSocket, width, height);
            // serverThread.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        public void drawGUI(){
            frame.add(cPanel,BorderLayout.CENTER);
            // frame.add(desktop,BorderLayout.CENTER);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
            frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
    
            frame.setVisible(true);
            // interFrame.setLayout(new BorderLayout());
            // interFrame.getContentPane().add(cPanel,BorderLayout.CENTER);
            // interFrame.setSize(100, 100);
            // desktop.add(interFrame);
            try {
                interFrame.setMaximum(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
    
            cPanel.setFocusable(true);
            // interFrame.setVisible(true);
        }
}
