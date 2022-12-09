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
import java.awt.BorderLayout;
import java.beans.PropertyVetoException;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.zip.*;
import server.*;

public class ServerFrame extends Thread/*extends JFrame implements Runnable*/{//CreateFrame.java
    String width = "", height = "";
    private JDesktopPane desktop = new JDesktopPane();
    private Socket cSocket = null;
    private ServerSocket serverSocket=null;
    JFrame frame=new JFrame();
    private JInternalFrame interFrame = new JInternalFrame(
        "Server Screen",
        true,
        true,	
        true
    );
    private JPanel cPanel = new JPanel();

    public ServerFrame(Socket cSocket/*ServerSocket sv*/,String width,String height){
        this.width=width;
        this.height=height;
        // this.serverSocket=sv;
        this.cSocket=cSocket;
        start();
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
    }
    @Override
    public void run() {
        InputStream in=null;

        drawGUI();//create , design the frame

        try {
            in=cSocket.getInputStream();
            System.out.println(/*"This socket has not a problems,see others errors: "+*/cSocket.getInputStream());
        } catch (Exception e) {
            System.out.println("-There's a problem in ServerFrame.java about the cSocket:");
            e.printStackTrace();
        }
        // new ReceiveScreen(in,cPanel);
                   // while (continueAct) {//continueLoop 
                    int tour=1;
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
                // System.out.println("Image received from the tour :"+tour);
                // tour++;
            } catch (Exception e) {
                System.out.println("Error: ServerFrame.java: line 102");
                e.printStackTrace();
            }
        }
    }
}
