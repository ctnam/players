/**
 * DOC
 * initiateToRecipient: one player self-identified as sender identifies the recipient, which makes himself/herself the initiator
 * send: sets the stop condition, identifies the message, forwarding to sendLog and transmitMessage
 * reply: forwards to send after setting the recipient
 * transmitMessage: sets the message to recipient, saves the counters, forwarding to reply
 * sendLog: prints out the sending to console
 */

package players;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

class Player implements Initiator<Player>, Messagable {
    private byte sendMax = 10;
    private byte receiveMax = 10; 
    private boolean replyMode = true;
    private byte sentCounter, receivedCounter;
    private final String name;
    private Player sender, recipient;
    private List<Player> recipienList = new ArrayList<>();     
    private static Player initiator;
    private Message sendMessage, receiveMessage;

    protected Player (final String name) {this.name = name;}
    public static Player newInstance (final String name) {return new Player(name);}
    public static List<Player> newInstances (final String... names) {return Stream.of(names).map(Player::newInstance).toList();}

    {replyMode = ThreadLocalRandom.current().nextBoolean();}
    public boolean getReplyMode () {return replyMode;}

    @Override
    public Player initiateToRecipient (Player recipient) {
        this.recipient = recipient;
        if (!recipient.equals(this)) {recipienList.add(recipient);}
        recipient.sender = this;        
        initiator = this;
        return this;
    }

    protected Player addRecipient (Player recipient) {
        this.recipient = recipient;
        if (!recipient.equals(this)) {recipienList.add(recipient);}
        recipient.sender = this;        
        return this;
    }

    //2. one of the players should send a message to second player (let's call this player "initiator")
    @Override
    synchronized public void send (Message sendMessage) {
        //4. finalize the program (gracefully) after the initiator sent 10 messages and received back 10 messages (stop condition)
        byte index = 0;
        while (initiator.sentCounter < sendMax && index < recipienList.size()) {
                System.out.println("SEND v1");
                this.sendMessage = sendMessage;     
                send(sendMessage, recipienList.get(index));
                index++;
        }
    }

    protected void send (Message sendMessage, Player recipient) {
                    System.out.println("SEND v2");
                    this.sendMessage = sendMessage;   
                    sentCounter++;             
                    transmitMessage(sendMessage, recipient);   
    }

    //3. when a player receives a message, it should reply with a message that contains the received message concatenated with the value of a counter holding the number of messages this player already sent.
    @Override
    public void reply () {
        System.out.println("REPLY");
        recipient = sender;
        recipient.sender = this;
        addRecipient(recipient).send(new Message.Compose(receiveMessage.toString(), String.valueOf(sentCounter))
                                                .signature("signed")
                                                .finish(),
                                                recipient);
    }

    //3. when a player receives a message, it should reply with a message that contains the received message concatenated with the value of a counter holding the number of messages this player already sent.
    @Override
    public void transmitMessage (Message transmitMessage, Player recipient) {
        if (recipient.receivedCounter < receiveMax) {
        try {
            System.out.println("TRANSMIT");
            recipient.receiveMessage = transmitMessage;
            recipient.receivedCounter++;
            sendLog(this, recipient, sendMessage);
        }
        catch (Exception ex) {ex.printStackTrace();}
        finally {
            if (recipient.replyMode) recipient.reply();
        }
        }
    }

    public void activateReply () {if (!replyMode) replyMode = true;}

    private Player sendLog (Player sender, Player recipient, Message message) {
        System.out.printf("From %s to %s: %s %n", sender, recipient, message);
        System.out.printf("    *Player=%s, sent=%s, received=%s %n", this, sentCounter, receivedCounter);
        return this;
    }

    @Override
    public String toString () {return name;}

    @Override
    public boolean equals (Object object) {
        return object instanceof Player && ((Player)object).name.equals(this.toString());
    }
}

