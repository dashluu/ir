package cfg;

public interface ICFGElementIterator<E> {
    boolean hasNext();
    E next();
    void set(E elm);
    void add(E elm);
    void remove(E elm);
}
