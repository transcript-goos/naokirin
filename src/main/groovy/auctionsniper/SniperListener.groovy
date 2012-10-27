package main.groovy.auctionsniper

interface SniperListener extends EventListener {
    void sniperLost()
    void sniperBidding()
}
