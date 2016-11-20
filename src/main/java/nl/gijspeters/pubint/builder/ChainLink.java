package nl.gijspeters.pubint.builder;

import java.util.Collection;
import java.util.HashSet;

/**
 * Created by gijspeters on 03-11-16.
 */
public class ChainLink<T> extends HashSet<T> {

    private T hook = null;

    public ChainLink() {
        super();
    }

    public T getHook() {
        return hook;
    }

    public boolean add(T t) {
        if (contains(t)) {
            return false;
        } else {
            super.add(t);
            hook = t;
            return true;
        }
    }

    public boolean addAll(Collection c) {
        boolean added = false;
        for (Object o : c) {
            if (o != null) {
                if (add((T) o)) added = true;
            }
        }
        return added;
    }

    public boolean chain(ChainLink<? extends T> chain) {
        boolean added = addAll(chain);
        if (added) hook = chain.getHook();
        return added;
    }

    public boolean remove(Object o) {
        return false;
    }

    public boolean removeAll(Collection<?> c) {
        return false;
    }

    public boolean retainAll(Collection<?> c) {
        return false;
    }

    public void clear() {
        super.clear();
        hook = null;
    }

}
