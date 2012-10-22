package main.groovy.auctionsniper

import javax.swing.SwingUtilities
import main.groovy.auctionsniper.ui.MainWindow
import org.jivesoftware.smack.XMPPConnection
import org.jivesoftware.smack.Chat
import org.jivesoftware.smack.MessageListener
import org.jivesoftware.smack.packet.Message

class Main {
    @SuppressWarnings('unused')
    private Chat notToBeGCd

    private static final int ARG_HOSTNAME = 0
    private static final int ARG_USERNAME = 1
    private static final int ARG_PASSWORD = 2
    private static final int ARG_ITEM_ID = 3

    public static final String ITEM_ID_AS_LOGIN = "auction-%s"
    public static final String AUCTION_RESOURCE = "Auction"
    public static final String AUCTION_ID_FORMAT =
        ITEM_ID_AS_LOGIN + '@%s/' + AUCTION_RESOURCE

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

        // If create Runnable object in the argument of the invokeLater,
        // throws MissingFieldException about ui
        def runnable = new Runnable() {
            @Override
            void run() {
                ui.showStatus(MainWindow.STATUS_LOST)
            }
        }

        final Chat chat = connection.getChatManager().createChat(
                auctionId(itemId, connection), new MessageListener() {

                    @Override
                    void processMessage(Chat chat, Message message) {
                        SwingUtilities.invokeLater(runnable)
                    }
                })
        this.notToBeGCd = chat
        chat.sendMessage(new Message())
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
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            void run() {
                ui = new MainWindow()
            }
        })
    }


}
