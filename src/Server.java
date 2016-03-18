import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Vector;

/**
 * Created by KNapret on 18.03.2016.
 */
public class Server {

    private List<User> users = new Vector<>();


    public Server (){

        try (ServerSocket serverSocket =
                    new ServerSocket(5090)){
            while (true){

                try{
                    users.add(new User(
                            serverSocket.accept()));

                }catch(IOException e){
                    //kommt hier her, wenn
                    //etwas mit dem Verbindungsaufbau
                    //schief ging
                }

            }
        } catch (IOException e) {
            //Kommt hier hin, wenn der Port schon
            //belegt ist, oder man darf
            //gar keinen Port öffnen

            e.printStackTrace();
        }


    }

    private void neueNachricht(User user, String nachricht) {
        System.out.println(nachricht);
        for (User u : users) {
            if (u != user) // Damit ich meine Nachrichten nicht selbst sehe
                u.sendeNachricht(nachricht);
        }
    }

    private void loescheMich(User user) {
        users.remove(user);
    }

    

    private class User {

        private Socket socket;

        public User(Socket socket) {
            this.socket = socket;

            new Thread(){
                public void run(){
                    String ip = socket.getInetAddress().toString();
                    System.out.println("Client mit IP: "+ip +" angemeldet");

                    try(BufferedReader reader = new BufferedReader(
                            new InputStreamReader(socket.getInputStream()))){
                        String zeile;
                        while( (zeile = reader.readLine())!=null){
                            neueNachricht(User.this, zeile);
                        }

                    }catch (IOException e){
                        //Wenn er hir ist,
                        //kann er den Client nicht erreichen.
                    }

                    System.out.println("Client mit "+ip+" abgemeldet");
                    loescheMich(User.this);
                }
            }.start();
        }

        private void sendeNachricht(String nachricht) {
            try{

                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(
                                socket.getOutputStream()));

            }catch (IOException e){
                e.printStackTrace();
            }

        }
        
    }//end UserClass


    
    

  

}
