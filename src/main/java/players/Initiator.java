/**
 * DOC
 * initiateToRecipient: an initiator identifies the recipient(s)
 */

package players;

interface Initiator<T extends Player> {
    T initiateToRecipient (T recipient);
}

