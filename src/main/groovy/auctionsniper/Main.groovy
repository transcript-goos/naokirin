package main.groovy.auctionsniper

import main.groovy.auctionsniper.ui.MainWindow

import org.jivesoftware.smack.XMPPConnection
import org.jivesoftware.smack.Chat
import java.awt.event.WindowAdapter
import javax.swing.SwingUtilities
import org.jivesoftware.smack.XMPPException

class Main {
    @SuppressWarnings('unused')
    private Chat notToBeGCd

    private static final int ARG_HOSTNAME = 0
    private static final int ARG_USERNAME = 1
    private static final int ARG_PASSWORD = 2
    private static final int ARG_ITEM_ID = 3

    public static final String ITEM_ID_AS_LOGIN = 'auction-%s'
    public static final String AUCTION_RESOURCE = 'Auction'
    public static final String AUCTION_ID_FORMAT =
        "$ITEM_ID_AS_LOGIN@%s/$AUCTION_RESOURCE"

    static String JOIN_COMMAND_FORMAT = 'SOLVersion: 1.1; Command: JOIN;'
    static String BID_COMMAND_FORMAT = 'SOLVersion: 1.1; Command: Bid; Price: %d;'

    private MainWindow ui

    static final MAIN_WINDOW_NAME = 'Auction Sniper Main'


    Main() {
        startUserInterface()
    }

    static void main(String... args) {
        Main main = new Main()
        main.joinAuction(connection(args[ARG_HOSTNAME], args[ARG_USERNAME], args[ARG_PASSWORD]),
                                    args[ARG_ITEM_ID])
    }

    private void joinAuction(XMPPConnection connection, String itemId) {
        disconnectWhenUICloses(connection)

        final Chat chat = connection.getChatManager().createChat(
                auctionId(itemId, connection), null)
        this.notToBeGCd = chat

        def auction = new XMPPAuction(chat)
        chat.addMessageListener(
                new AuctionMessageTranslator(
                        new AuctionSniper(auction, new SniperStateDisplayer())))
        auction.join()
    }

    static class XMPPAuction implements Auction {
        private final Chat chat

        XMPPAuction(chat) {
            this.chat = chat
        }

        void bid(int amount) {
            sendMessage(String.format(BID_COMMAND_FORMAT, amount))
        }

        void join() {
            sendMessage(JOIN_COMMAND_FORMAT)
        }

        private void sendMessage(final String message) {
            try {
                chat.sendMessage(message)
            } catch (XMPPException e) {
                e.printStackTrace()
            }
        }
    }

    class SniperStateDisplayer implements SniperListener {
        @Override
        void sniperLost() {
            showStatus(MainWindow.STATUS_LOST)
        }

        void sniperBidding() {
            showStatus(MainWindow.STATUS_BIDDING)
        }

        void sniperWinning() {
            showStatus(MainWindow.STATUS_WINNING)
        }

        void showStatus(final String status) {
            SwingUtilities.invokeLater({ ui.showStatus(status) } as Runnable)
        }
    }

    void disconnectWhenUICloses(XMPPConnection connection) {
        ui.addWindowListener({ connection.disconnect() } as WindowAdapter)
    }

    private static String auctionId(String itemId, XMPPConnection connection) {
        return String.format(AUCTION_ID_FORMAT, itemId, connection.getServiceName())
    }

    private static XMPPConnection connection(String hostname, String username, String password) {
        XMPPConnection connection = new XMPPConnection(hostname)
        connection.connect()
        connection.login(username, password, AUCTION_RESOURCE)

        return connection
    }

    private void startUserInterface() {
        SwingUtilities.invokeAndWait({ ui = new MainWindow() } as Runnable)
    }

    @Override
    void sniperLost() {
        SwingUtilities.invokeLater({ ui.showStatus(MainWindow.STATUS_LOST) } as Runnable)
    }

    @Override
    void sniperBidding() {
        SwingUtilities.invokeLater({ ui.showStatus(MainWindow.STATUS_BIDDING) } as Runnable)
    }
}
