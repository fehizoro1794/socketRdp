package server;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.awt.Image;
import java.awt.Graphics;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ReceiveScreen extends Thread {
    ObjectInputStream objInputStr;//private ObjectInputStream cObjectInputStream 
    JPanel cPanel;
    boolean continueAct=true;//continueLoop
    InputStream inputStr;//InputStream oin
    Image image1;
    Image img1;
    public ReceiveScreen(InputStream in,JPanel p){
        this.inputStr=in;
        this.cPanel=p;
        while (continueAct) {
        run();
        }
    }
    @Override
    public void run() {
        try {
            // while (continueAct) {//continueLoop 
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
                image1=ImageIO.read(new ByteArrayInputStream(bytes));
                image1=image1.getScaledInstance(cPanel.getWidth(), cPanel.getHeight(), Image.SCALE_FAST);
                Graphics graphics=cPanel.getGraphics();
                graphics.drawImage(image1, 0, 0, cPanel.getWidth(), cPanel.getHeight(), cPanel);
                // cPanel.repaint();
                
            // }
        } catch (Exception e) {
            System.out.println("-There's a probleme in source ReceiveScreen.java: "+e.getMessage());
            System.out.println(e.getCause());
            e.printStackTrace();
        }
    }
}
