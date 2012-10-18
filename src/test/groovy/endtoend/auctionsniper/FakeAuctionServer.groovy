package test.groovy.endtoend.auctionsniper

import org.jivesoftware.smack.XMPPConnection
import org.jivesoftware.smack.Chat
import org.jivesoftware.smack.ChatManagerListener
import org.jivesoftware.smack.packet.Message
import org.jivesoftware.smack.MessageListener
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.TimeUnit

class FakeAuctionServer {
    private final SingleMessageListener messageListener = new SingleMessageListener()

    static final String ITEM_ID_AS_LOGIN = "auction-%s"
    static final String AUCTION_RESOURCE = "Auction"
    static final String XMPP_HOSTNAME = "localhost"
    private static final String AUCTION_PASSWORD = "auction"

    private final String itemId
    private final XMPPConnection connection
    private Chat currentChat

    FakeAuctionServer(String itemId) {
        this.itemId = itemId
        this.connection = new XMPPConnection(XMPP_HOSTNAME)
    }

    void startSellingItem() {
        connection.connect()
        connection.login(String.format(ITEM_ID_AS_LOGIN, itemId), AUCTION_PASSWORD, AUCTION_RESOURCE)
        connection.getChatManager().addChatListener(
            new ChatManagerListener() {

                @Override
                void chatCreated(Chat chat, boolean createdLocally) {
                    currentChat = chat
                    chat.addMessageListener(messageListener)
                }
            }
        )
    }

    String getItemId() {
        return itemId
    }

    void hasReceivedJoinRequestFromSniper() {
        messageListener.receivesAMessage()
    }

    void announceClosed() {
        currentChat.sendMessage(new Message())
    }

    void stop() {
        connection.disconnect()
    }

    class SingleMessageListener implements MessageListener {
        private final ArrayBlockingQueue<Message> messages =
            new ArrayBlockingQueue(1)

        @Override
        void processMessage(Chat chat, Message message) {
            messages.add(message)
        }

        void receivesAMessage() {
            assert messages.poll(5, TimeUnit.SECONDS) != null, 'Message'
        }
    }
}
