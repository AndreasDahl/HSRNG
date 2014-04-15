package logic.draft;

/**
 * @author Andreas
 * @since 24-03-14
 */
public abstract class DraftingFactory<T extends IDraft> {
    private boolean locked = false;

    protected void lock() {
        locked = true;
    }

    protected void testLock() {
        if (isLocked())
            throw new FactoryLockedException();
    }

    public boolean isLocked() {
        return locked;
    }

    public abstract T getDraft();
}
