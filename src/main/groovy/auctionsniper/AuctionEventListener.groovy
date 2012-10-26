package main.groovy.auctionsniper

interface AuctionEventListener extends EventListener {
    void auctionClosed()
    void currentPrice(int price, int increment)
}
