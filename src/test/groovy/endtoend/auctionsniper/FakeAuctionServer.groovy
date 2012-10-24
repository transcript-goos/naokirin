package test.groovy.endtoend.auctionsniper

import static org.hamcrest.Matchers.equalTo
import static org.hamcrest.MatcherAssert.assertThat

import org.jivesoftware.smack.XMPPConnection
import org.jivesoftware.smack.Chat
import org.jivesoftware.smack.ChatManagerListener
import org.jivesoftware.smack.packet.Message
import org.jivesoftware.smack.MessageListener
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.TimeUnit
import main.groovy.auctionsniper.Main
import org.hamcrest.Matcher

import static org.hamcrest.Matchers.is
import static org.hamcrest.Matchers.notNullValue

class FakeAuctionServer {
    private final SingleMessageListener messageListener = new SingleMessageListener()

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
        connection.login(String.format(Main.ITEM_ID_AS_LOGIN, itemId), AUCTION_PASSWORD, Main.AUCTION_RESOURCE)
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

    void hasReceivedJoinRequestFrom(String sniperId) {
        receivesAMessageMatching(sniperId, equalTo(Main.JOIN_COMMAND_FORMAT))
    }

    void announceClosed() {
        currentChat.sendMessage(new Message())
    }

    void stop() {
        connection.disconnect()
    }

    void reportPrice(int price, int increment, String bidder) {
        currentChat.sendMessage(
                String.format('SOLVersion: 1.1; Event: PRICE; CurrentPrice: %d; '
                            + 'Increment: %d; Bidder: %s;', price, increment, bidder))
    }

    void hasReceivedBid(int bid, String sniperId) {
        receivesAMessageMatching(sniperId,
            equalTo(String.format(Main.BID_COMMAND_FORMAT, bid)))
    }

    void receivesAMessageMatching(String sniperId, Matcher<? super String> messageMatcher) {
        messageListener.receivesAMessage(messageMatcher)
        assertThat currentChat.getParticipant(), equalTo(sniperId)
    }

    class SingleMessageListener implements MessageListener {
        private final ArrayBlockingQueue<Message> messages =
            new ArrayBlockingQueue(1)

        @Override
        void processMessage(Chat chat, Message message) {
            messages.add(message)
        }

        @SuppressWarnings('unchecked')
        void receivesAMessage(Matcher<? super String> messageMatcher) {
            final Message message = messages.poll(5, TimeUnit.SECONDS)
            assertThat 'Message', message, is(notNullValue())
            assertThat message.getBody(), messageMatcher
        }
    }
}
