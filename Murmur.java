import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Base64;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Random;
import java.util.List;
import java.util.stream.Collectors;
import java.net.UnknownHostException;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

public class Murmur {
    private static DatagramSocket socket = null;
 
    public static void main(String[] args) throws IOException {
        InetAddress ip = null;
        String hostname = null;
        int udpPort = getRandomNumberInRange(3000,3999);
        
        try {
            ip = InetAddress.getLocalHost();
            hostname = ip.getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        
        String homesock = ip.toString()+":"+Integer.toString(udpPort);
        
        System.out.println("Sending broadcast beacon.");
        String murad = encodeString(homesock);
        System.out.println(murad);
        broadcast(murad, InetAddress.getByName("255.255.255.255"));
        
        System.out.println("Creating socket object for callbacks...");
        
        System.out.println("Port selected: " + Integer.toString(udpPort));
        DatagramSocket socket = new DatagramSocket(udpPort, InetAddress.getByName("0.0.0.0"));
       
        
        socket.setBroadcast(true);
        System.out.println("Socket: " + socket);
        byte[] buf = new byte[512];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        //Send the broadcast
        //broadcast(ip.toString(), InetAddress.getByName("255.255.255.255"));
        //Listening for incoming
        System.out.println("Listening for callbacks...");
        
        /*
        long endTime = System.currentTimeMillis() + 15000;
        while (System.currentTimeMillis() < endTime) {
           socket.receive(packet);
           System.out.println(".");
        }*/
        
        while (true) {
           socket.receive(packet);
           System.out.println(packet);
           DataInputStream dataIn = new DataInputStream(new ByteArrayInputStream(packet.getData(), 0, packet.getLength()));
        }
    }
 
    public static void broadcast(String broadcastMessage, InetAddress address) throws IOException {
        socket = new DatagramSocket();
        socket.setBroadcast(true);
        byte[] buffer = broadcastMessage.getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, 3333);
        socket.send(packet);
        socket.close();
    
    }
    
    private static int getRandomNumberInRange(int min, int max) {
   
   	if (min >= max) {
  			throw new IllegalArgumentException("max must be greater than min");
  		}
  		Random r = new Random();
   		return r.nextInt((max - min) + 1) + min;
    }
    
    public static String encodeString(String msg) {
      Base64.Encoder enc = Base64.getEncoder();
      byte[] encbytes = enc.encode(msg.getBytes());
      System.out.println("Original:      " + msg);
      System.out.println("Base64:          ");
      System.out.println(encbytes);
      String encodedStr = "";
      String st = "";
      for (byte b : encbytes) {
         st = String.format("%02X", b);
         System.out.print(st);
         }
      
      for (int i = 0; i < encbytes.length; i++) {
         //System.out.printf("%c", (char) encbytes[i]);
         //System.out.printf(encbytes[i].toString);
         //encodedStr += "%c", (char) encbytes[i];
         if (i != 0 && i %4 == 0) {
            System.out.print(' ');
            encodedStr += (' ');
         }
      }
      return st;
   }
   
}

    
  

