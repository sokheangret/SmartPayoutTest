package sg.com.arrowlogic.smartpayouttest.network;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class MoneyReq extends AsyncTask<Void,Void,Void> {

    private String ipAddress, response = "", moneyReq;
    private int port;
    private Socket socket;
    private ConnectionCallback connectionCallback;

    public MoneyReq(String ipAddress, int port, String moneyReq,ConnectionCallback connectionCallback) {
        this.ipAddress = ipAddress;
        this.port = port;
        this.moneyReq = moneyReq;
        this.connectionCallback = connectionCallback;

        //new Thread(new Client()).start();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... voids) {

        try {
            socket = new Socket(ipAddress, port);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
            byte[] buffer = new byte[1024];

            DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());
            dOut.writeUTF(moneyReq);
            dOut.flush();

            int bytesRead;
            InputStream inputStream = socket.getInputStream();

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
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
        connectionCallback.onReceiveMoney(response);
    }

    public interface ConnectionCallback {
        void onReceiveMoney(String data);
    }

    /**
     * TODO Try to get back from Smart Payout but it seem not work
     */
    class Client extends Thread {

        public void run()
        {
            try
            {
                Socket socket = new Socket(ipAddress, port);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                //out.println(json);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String response = in.readLine();
                connectionCallback.onReceiveMoney(response);
                //socket.close();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
    @SuppressLint("StaticFieldLeak")
    class GetThread extends AsyncTask<Void,Void,Void> {
        String textMessage;
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                socket = new Socket(ipAddress, port);
                InputStreamReader isR = new InputStreamReader(socket.getInputStream());
                BufferedReader bfr = new BufferedReader(isR);
                textMessage = bfr.readLine();

            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            connectionCallback.onReceiveMoney(textMessage);
        }
    }
}
