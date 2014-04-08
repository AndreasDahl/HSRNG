package logic.draft;

/**
 * @author Andreas
 * @since 24-03-14
 */
public interface DraftingFactory<T extends IDraft> {
    public T getDraft();
}
