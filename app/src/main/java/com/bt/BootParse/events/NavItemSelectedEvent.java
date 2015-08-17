package com.bt.BootParse.events;

/**
 * Pub/Sub event used to communicate between fragment and activity.
 * Subscription occurs in the {@link com.bt.BootParse.ui.MainActivity}
 */
public class NavItemSelectedEvent {
    private int itemPosition;

    public NavItemSelectedEvent(int itemPosition) {
        this.itemPosition = itemPosition;
    }

    public int getItemPosition() {
        return itemPosition;
    }
}
