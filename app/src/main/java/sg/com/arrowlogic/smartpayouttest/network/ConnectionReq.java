package sg.com.arrowlogic.smartpayouttest.network;

import android.os.AsyncTask;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class ConnectionReq extends AsyncTask<Void,Void,Void> {

    private String machineName, ipAddress, response = "";
    private int port, group, buffer;
    private Socket socket;
    private ConnectionCallback connectionCallback;

    public ConnectionReq(String machineName, String ipAddress, int port, int group, int buffer, ConnectionCallback connectionCallback) {
        this.machineName = machineName;
        this.ipAddress = ipAddress;
        this.port = port;
        this.group = group;
        this.buffer = buffer;
        this.connectionCallback = connectionCallback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... voids) {

        try {
            socket = new Socket(ipAddress, port);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(buffer);
            byte[] buffer = new byte[this.buffer];

            //////////////
            DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());
            dOut.writeUTF("Mobile");
            dOut.flush();
            /////////////
            int bytesRead;
            InputStream inputStream = socket.getInputStream();

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, group, bytesRead);
                response += byteArrayOutputStream.toString("UTF-8");
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
            response = "UnknownHostException: " + e.toString();
        } catch (IOException e) {
            e.printStackTrace();
            response = "IOException: " + e.toString();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Log.e("ConnectionReq",response);
        connectionCallback.onConnect(response);
        if(response.contains("Mobile Connected")){

        }else if(response.contains("CashMachine Connected")){

        }
    }

    public interface ConnectionCallback {
        void onConnect(String data);
    }
}
