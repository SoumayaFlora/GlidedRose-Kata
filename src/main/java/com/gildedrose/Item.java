package com.gildedrose;


public class Item {

    final String name;
    private int sellIn;
    private int quality;
    private AgedBrieStatus isAgedBrie;
    private BackstageStatus isBackstagePasses;
    private SulfurasStatus isSulfuras;

    private static final int EXPIRED = 0;
    private static final int MAX_QUALITY = 50;
    private static final int HAS_QUALITY = 0;
    private static final String AGED_BRIE = "Aged Brie";
    private static final String SULFURAS = "Sulfuras, Hand of Ragnaros";
    private static final String BACKSTAGE = "Backstage passes to a TAFKAL80ETC concert";

    public Item(String name, int sellIn, int quality) {
        this.name = name;
        this.sellIn = sellIn;
        this.quality = quality;
        // need for refactoring...
        this.isAgedBrie = name.equals(AGED_BRIE) ? new AgedBrie() : new NotAgedBrie();
        this.isBackstagePasses = name.equals(BACKSTAGE) ? new Backstage() : new NotBackstage();
        this.isSulfuras = name.equals(SULFURAS) ? new Sulfuras() : new NotSulfuras();
    }
    interface AgedBrieStatus {
        void handleExpired();
        void updateItems();
    }

    class AgedBrie implements AgedBrieStatus{
        @Override
        public void handleExpired(){
            increaseQualityIfNotMax();
        }
        @Override
        public void updateItems() {
            increaseQualityAndBackstagePasses();
        }
    }

    class NotAgedBrie implements AgedBrieStatus{
        @Override
        public void handleExpired(){
            isBackstagePasses.handleExpiredItem();
        }
        @Override
        public void updateItems() {
            isBackstagePasses.updateItems();
        }
    }

    interface BackstageStatus {
        void handleExpiredItem();
        void updateItems();
        void increaseQuality();
    }

    class Backstage implements BackstageStatus {
        @Override
        public void handleExpiredItem(){
            quality = 0;
        }
        @Override
        public void updateItems() {
            increaseQualityAndBackstagePasses();
        }
        @Override
        public void increaseQuality() {
            switch (sellIn) {
                case 11:
                    increaseQualityIfNotMax(); //no break: increases twice!
                case 6:
                    increaseQualityIfNotMax();
                    break;
            }
        }
    }

    class NotBackstage implements BackstageStatus {
        @Override
        public void handleExpiredItem(){
            decreaseItemHasQuality();
        }
        @Override
        public void updateItems() {
            decreaseItemHasQuality();
        }
        public void increaseQuality(){

        }
    }

    interface SulfurasStatus{
        void decreaseQuality();
    }

    class Sulfuras implements SulfurasStatus{
        @Override
        public void decreaseQuality() {
        }
    }

    class NotSulfuras implements SulfurasStatus{
        @Override
        public void decreaseQuality() {
            quality--;
        }
    }

    @Override
    public String toString() {
        return this.name + ", " + this.sellIn + ", " + this.quality;
    }

    void increaseQualityIfNotMax() {
        if (this.quality < MAX_QUALITY)
            this.quality++;
    }

    void increaseQualityAndBackstagePasses() {
        if (this.quality < MAX_QUALITY){
            this.quality++;
            isBackstagePasses.increaseQuality();
        }
    }

    void decreaseItemHasQuality() {
        if (this.quality > HAS_QUALITY)
            isSulfuras.decreaseQuality();
    }

    void handleIfExpired() {
        if (this.sellIn < EXPIRED)
            isAgedBrie.handleExpired();
    }

    void updateSellIn() {
        isSulfuras.decreaseQuality();
        handleIfExpired();
    }

    void update() {
        isAgedBrie.updateItems();
        updateSellIn();
    }
}
