package test.groovy.endtoend.auctionsniper

import org.jivesoftware.smack.XMPPConnection
import org.jivesoftware.smack.Chat
import org.jivesoftware.smack.ChatManagerListener
import org.jivesoftware.smack.packet.Message
import org.jivesoftware.smack.MessageListener
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.TimeUnit
import main.groovy.auctionsniper.Main

class FakeAuctionServer {
    private final SingleMessageListener messageListener = new SingleMessageListener()

    static final String XMPP_HOSTNAME = 'localhost'
    private static final String AUCTION_PASSWORD = 'auction'

    private final String itemId
    private final XMPPConnection connection
    private Chat currentChat

    FakeAuctionServer(String itemId) {
        this.itemId = itemId
        this.connection = new XMPPConnection(XMPP_HOSTNAME)
    }

    void startSellingItem() {
        connection.connect()
        connection.login(String.format(Main.ITEM_ID_AS_LOGIN, itemId), AUCTION_PASSWORD, Main.AUCTION_RESOURCE)
        connection.getChatManager().addChatListener(
                { Chat chat, boolean createdLocally ->
                    currentChat = chat
                    chat.addMessageListener(messageListener)
                } as ChatManagerListener)
    }

    String getItemId() {
        return itemId
    }

    void hasReceivedJoinRequestFrom(String sniperId) {
        receivesAMessageMatching(sniperId, Main.JOIN_COMMAND_FORMAT)
    }

    void announceClosed() {
        currentChat.sendMessage(new Message())
    }

    void stop() {
        connection.disconnect()
    }

    void reportPrice(int price, int increment, String bidder) {
        currentChat.sendMessage(
                "SOLVersion: 1.1; Event: PRICE; CurrentPrice: $price; Increment: $increment; Bidder: $bidder")
    }

    void hasReceivedBid(int bid, String sniperId) {
        receivesAMessageMatching(sniperId,
            String.format(Main.BID_COMMAND_FORMAT, bid))
    }

    void receivesAMessageMatching(String sniperId, String match) {
        messageListener.receivesAMessage(match)
        assert currentChat.participant == sniperId
    }

    class SingleMessageListener implements MessageListener {
        private final def messages = new ArrayBlockingQueue(1)

        @Override
        void processMessage(Chat chat, Message message) {
            messages.add(message)
        }

        @SuppressWarnings('unchecked')
        void receivesAMessage(match) {
            final Message message = messages.poll(5, TimeUnit.SECONDS)
            assert message != null, 'Message'
            assert match in message?.body
        }
    }
}
