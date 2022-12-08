package client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import javax.imageio.ImageIO;

import eventsocket.ReceiveEvents;

import java.awt.Robot;
import java.io.OutputStream;
import java.awt.Rectangle;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

public class InitConnection {
    Socket socket=null;
    DataInputStream password=null;
    DataOutputStream verify=null;
    String width="";
    String height="";
    OutputStream output=null;

    public InitConnection(String ip,int port){
        Robot robot=null;
        Rectangle rectangle=null;
        try {
            System.out.println("I'm going to connect with the server");
            socket=new Socket(ip, port);

            GraphicsEnvironment grEnvir=GraphicsEnvironment .getLocalGraphicsEnvironment();//get graphic of the environnement,the client
            GraphicsDevice grDevice=grEnvir.getDefaultScreenDevice();//get the default screen of the system which we want to accede

            Dimension dim=Toolkit.getDefaultToolkit().getScreenSize();
            String width=""+dim.getWidth();
            String height=""+dim.getHeight();
            rectangle=new Rectangle(dim);
            robot=new Robot(grDevice);//to simulate the acceded computer device
            
            drawGUI();

            while (true) {
                // socket=new Socket(ip, port);
                verify=new DataOutputStream(socket.getOutputStream());
                verify.writeUTF(width);
                verify.writeUTF(height);
                // SendScreen sendScreen = new SendScreen(socket,robot,rectangle);
                // sendScreen.start();
                try {
                    output=socket.getOutputStream();
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("->There's a problem in source SendScreen.java(1)");
                }
                /***********The SEND SCREEN source */
                while (true) {
                    BufferedImage image=robot.createScreenCapture(rectangle);
                   
                    try {
                        // outObj=new ObjectOutputStream(this.output);
                        // outObj.writeObject(image);
                        ByteArrayOutputStream byteOut=new ByteArrayOutputStream();
                        ImageIO.write(image, "jpeg", byteOut); //Send and resend image(Like refresh with the condition WHILE)
                        // this.output.write(byteOut.toByteArray());
                        ByteArrayInputStream bInput=new ByteArrayInputStream(byteOut.toByteArray());
                        BufferedInputStream reader = new BufferedInputStream(bInput);
                        BufferedOutputStream out=new BufferedOutputStream(socket.getOutputStream());
        
                        byte[] buffer = new byte[1024 * 1024];
                        int bytesRead;
                        while((bytesRead = reader.read(buffer))!=-1) {
                            out.write(buffer, 0, bytesRead);
                        }
        
                        System.out.println("ByteOut's size: "+byteOut.size());
                        System.out.println("------Capture d'ecran du client---------");
                        System.out.println(".robot screen capture width: "+image.getWidth());
                        System.out.println(".robot screen capture heigth: "+image.getHeight());
                        System.out.println("---------------");
                        System.out.println("***************");
                        System.out.println("Image sended");
                        System.out.println("***************");
                        byteOut.close();
                        // image.flush();
                        output.flush(); 
        
                    } catch (Exception e) {
                        System.out.println("->There's a problem in source SendScreen.java(2)");
                        e.printStackTrace();
                    }/*finally{
                        try {
                            output.flush();   
                        } catch (Exception e) {
                            System.out.println("->There's a problem in SendScreen.java(4)");
                            e.printStackTrace();
                        }
                    }*/
                    ReceiveEvents receiveAndAttributeEvent =new ReceiveEvents(socket,robot);
                    receiveAndAttributeEvent.start();
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        System.out.println("->There's a problem in source SendScreen.java('Thread.sleep'))");
                        e.printStackTrace();
                    }
                }
                /***************************** */
            }
        } catch (Exception e) {
            System.out.println("There's a problem in InitConnection.java");
            e.printStackTrace();
        }
    }
    private void drawGUI(){}
}
