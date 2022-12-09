package server;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.net.Socket;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.awt.*;
import java.awt.event.*;

import eventsocket.SendEvents;
import server.Server;

public class ServerTreat extends Thread{
    JFrame frame=new JFrame();
    Server server;
    Socket socket;
    
    public ServerTreat(Server serv, Socket socket) {
        this.server=serv;
        this.frame=this.server.getFrame();
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            while (true) {
                // System.out.println("Client ip"+server.getClientSocket().getRemoteSocketAddress());
                // server.setVerification(new DataInputStream(server.getClientSocket().getInputStream()));
                System.out.println("Client ip"+this.socket.getRemoteSocketAddress());
                server.setVerification(new DataInputStream(this.socket.getInputStream()));

                // try {
                    server.setWidth(server.getVerification().readUTF());
                    server.setHeight(server.getVerification().readUTF());
                    System.out.println("------Donnees envoyees par un Client---------");
                    System.out.println("width readUTF: "+server.getWidth());
                    System.out.println("height readUTF: "+server.getHeight());
                    System.out.println("---------------------------------------------");
                // } catch (Exception e) {
                //     System.out.print("The client socket didn't sent data");
                //     e.printStackTrace();
                // }
                /************Server Frame ********/
                // ServerFrame serverFrame=new ServerFrame(server.getClientSocket(),width,height);
                InputStream in=null;

                drawGUI();//create , design the frame
        
                try {
                    // in=server.getClientSocket().getInputStream();
                    in=this.socket.getInputStream();
                    System.out.println(/*"This socket has not a problems,see others errors: "+*/server.getClientSocket().getInputStream());
                } catch (Exception e) {
                    System.out.println("-There's a problem in ServerFrame.java about the server.getClientSocket():");
                    e.printStackTrace();
                    break;
                }
                // new ReceiveScreen(in,server.getcPanel());
                        // while (continueAct) {//continueLoop 
                            int tour=1;
                            /*********Screen capture recuperation******* */
                while (true) {
                    try {
                        InputStream inputStr=in;
        
                        // BufferedInputStream inputBufImg=new BufferedInputStream(server.getClientSocket().getInputStream());
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
                        image1=image1.getScaledInstance(server.getcPanel().getWidth(), server.getcPanel().getHeight(), Image.SCALE_FAST);
                        Graphics graphics=server.getcPanel().getGraphics();
                        graphics.drawImage(image1, 0, 0, server.getcPanel().getWidth(), server.getcPanel().getHeight(), server.getcPanel());
                        // server.getcPanel().repaint();
                        System.out.println("======>Image received from the tour :"+tour);
                        tour++;
                        // new SendEvents(server.getClientSocket(),server.getcPanel(),server.getWidth(),server.getHeight());
                        new SendEvents(this.socket ,server.getcPanel(),server.getWidth(),server.getHeight());
                        // outputByte.close();
                    } catch (Exception e) {
                        System.out.println("Error: ServerTreat.java: line 106");
                        e.printStackTrace();
                        break;
                    }
                }   
                
                // new SendEvents(server.getClientSocket(),server.getcPanel(),width,height);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void drawGUI(){
        frame.add(server.getcPanel(),BorderLayout.CENTER);
        // frame.add(desktop,BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        Dimension dimScreen=Toolkit.getDefaultToolkit().getScreenSize();
        // frame.setSize((int)(dimScreen.getWidth()/1.3),(int)(dimScreen.getHeight()/1.3));

        frame.setVisible(true);
        // interFrame.setLayout(new BorderLayout());
        // interFrame.getContentPane().add(server.getcPanel(),BorderLayout.CENTER);
        // interFrame.setSize(100, 100);
        // desktop.add(interFrame);
        try {
            server.getInterFrame().setMaximum(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        server.getcPanel().setFocusable(true);
        // interFrame.setVisible(true);
    }
}
