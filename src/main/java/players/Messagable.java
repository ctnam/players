/**
 * DOC
 * methods send, transmitMessage, reply: operations of a messaging process
 */

package players;

interface Messagable {
    void send (Message message);
    void transmitMessage (Message message, Player recipient);
    default void reply () {};
}
