package com.example.changoo.mafia.network;


import com.example.changoo.mafia.model.SocketData;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.Socket;

import static android.R.attr.name;

public class SocketManager implements Serializable {
    final static String IP = "192.168.43.133";
    final static int PORT = 30025;
    static OutputStream os=null;
    static ObjectOutputStream oos=null;
    static InputStream is=null;
    static ObjectInputStream ois=null;
    private static Socket socket;

    public synchronized static Socket getSocket() throws IOException
    {
        if( socket == null) {
            socket = new Socket();
            os= getSocket().getOutputStream();
            oos= new ObjectOutputStream(os);
            is=socket.getInputStream();
            ois=new ObjectInputStream(is);
        }

        if( !socket.isConnected() )
            socket.connect(new InetSocketAddress(IP, PORT));

        return socket;
    }

    public synchronized static  ObjectInputStream getObjectInputStream() throws IOException{
        return ois;
    }
    public static void closeSocket() throws IOException
    {
        if ( socket != null )
            socket.close();
    }

    public static synchronized void sendMsg(String command, String name, Object object) throws IOException
    {
        oos.writeUnshared(new SocketData(command, name, object) );
        oos.reset();

    }
}