package listeners;

/**
 * The hit notifier interface.
 */
public interface HitNotifier {
    /**
     * Adds listener to hit events.
     *
     * @param hl the listener to add
     */
    void addHitListener(HitListener hl);

    /**
     * Removes the listener from list.
     *
     * @param hl the listener to be removed
     */
    void removeHitListener(HitListener hl);
}