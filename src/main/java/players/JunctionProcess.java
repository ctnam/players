/**
 * DOC
 * method run corresponds to the task of players communicating with each other - initiator1 identifies the recipients then sending the message, while receiver(s) may reply or not
 */

package players;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//5. both players should run in the same java process (strong requirement)
public class JunctionProcess {
    static ExecutorService executor = Executors.newSingleThreadExecutor();
    static List<Player> playerList = Player.newInstances("Initiator1", "Receiver2", "Receiver3", "Receiver1", "Receiver1", "Receiver1");  //1. create 2 Player instances
    static {System.out.println("Same process with Hello");}
    
    public static void run () {
        executor.submit(new Runnable () {
            @Override
            public void run() {
                playerList.stream().filter(p -> p.getReplyMode()==true).forEach(System.out::println);  // players who reply
                //2. one of the players should send a message to second player (let's call this player "initiator")
                playerList.get(0).initiateToRecipient(playerList.get(1))
                                        .addRecipient(playerList.get(2))
                                        .addRecipient(playerList.get(3))
                                        .addRecipient(playerList.get(4))
                                        .addRecipient(playerList.get(5))
                                        .send(new Message.Compose("Hallo ", LocalDateTime.now().toString()).finish());       
            }
        });
        executor.close();
    }
}
