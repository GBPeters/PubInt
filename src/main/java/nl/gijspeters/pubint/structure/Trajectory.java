package nl.gijspeters.pubint.structure;

import nl.gijspeters.pubint.validation.ValidationLeg;

import java.util.*;

/**
 * Created by gijspeters on 17-10-16.
 */
public class Trajectory extends TreeSet<Anchor> implements Comparable<Trajectory> {

    private Agent agent;

    public Trajectory() {
        super(new AnchorDateComparator());
    }

    public Trajectory(Collection<Anchor> c) {
        this();
        if (c.size() > 0) {
            addAll(c);
        }
    }


    /**
     * Ensures that this collection contains the specified element (optional
     * operation).  Returns <tt>true</tt> if this collection changed as a
     * result of the call.  (Returns <tt>false</tt> if this collection does
     * not permit duplicates and already contains the specified element.)<p>
     * <p>
     * Collections that support this operation may place limitations on what
     * elements may be added to this collection.  In particular, some
     * collections will refuse to add <tt>null</tt> elements, and others will
     * impose restrictions on the type of elements that may be added.
     * Collection classes should clearly specify in their documentation any
     * restrictions on what elements may be added.<p>
     * <p>
     * If a collection refuses to add a particular element for any reason
     * other than that it already contains the element, it <i>must</i> throw
     * an exception (rather than returning <tt>false</tt>).  This preserves
     * the invariant that a collection always contains the specified element
     * after this call returns.
     *
     * @param anchor element whose presence in this collection is to be ensured
     * @return <tt>true</tt> if this collection changed as a result of the
     * call
     * @throws UnsupportedOperationException if the <tt>add</tt> operation
     *                                       is not supported by this collection
     * @throws ClassCastException            if the class of the specified element
     *                                       prevents it from being added to this collection
     * @throws NullPointerException          if the specified element is null and this
     *                                       collection does not permit null elements
     * @throws IllegalArgumentException      if some property of the element
     *                                       prevents it from being added to this collection
     * @throws IllegalStateException         if the element cannot be added at this
     *                                       time due to insertion restrictions
     */
    public boolean add(Anchor anchor) {
        if (isEmpty()) {
            agent = anchor.getUser().getAgent();
        } else if (getAgent() != anchor.getUser().getAgent()) {
            throw new IllegalStateException("Non-equal agents");
        }
        return super.add(anchor);
    }

    /**
     * Adds all of the elements in the specified collection to this collection
     * (optional operation).  The behavior of this operation is undefined if
     * the specified collection is modified while the operation is in progress.
     * (This implies that the behavior of this call is undefined if the
     * specified collection is this collection, and this collection is
     * nonempty.)
     *
     * @param c collection containing elements to be added to this collection
     * @return <tt>true</tt> if this collection changed as a result of the call
     * @throws UnsupportedOperationException if the <tt>addAll</tt> operation
     *                                       is not supported by this collection
     * @throws ClassCastException            if the class of an element of the specified
     *                                       collection prevents it from being added to this collection
     * @throws NullPointerException          if the specified collection contains a
     *                                       null element and this collection does not permit null elements,
     *                                       or if the specified collection is null
     * @throws IllegalArgumentException      if some property of an element of the
     *                                       specified collection prevents it from being added to this
     *                                       collection
     * @throws IllegalStateException         if not all the elements can be added at
     *                                       this time due to insertion restrictions
     * @see #add(Object)
     */
    public boolean addAll(Collection<? extends Anchor> c) {
        Agent cagent = c.iterator().next().getUser().getAgent();
        for (Anchor a : c) {
            if (isEmpty()) {
                agent = a.getUser().getAgent();
            } else if (getAgent() != a.getUser().getAgent() || cagent != a.getUser().getAgent()) {
                throw new IllegalStateException("Non-equal agents");
            }
        }
        return super.addAll(c);
    }

    public Trajectory subSet(Anchor fromElement, Anchor toElement) {
        return subSet(fromElement, true, toElement, false);
    }

    public Trajectory subSet(Anchor fromElement, boolean fromInclusive, Anchor toElement, boolean toInclusive) {
        return new Trajectory(super.subSet(fromElement, fromInclusive, toElement, toInclusive));
    }

    public Agent getAgent() {
        return agent;
    }

    public Date getStartTime() {
        return first().getDate();
    }

    public Date getEndTime() {
        return last().getDate();
    }

    public SortedSet<Trajectory> splitTrajectory(long maxDeltaTimeMillis) {
        TreeSet<Trajectory> ts = new TreeSet<Trajectory>();
        Anchor fromAnchor = first();
        Anchor previousAnchor = first();
        for (Anchor a : this) {
            if (a.getDate().getTime() - previousAnchor.getDate().getTime() > maxDeltaTimeMillis) {
                ts.add(this.subSet(fromAnchor, a));
                fromAnchor = a;
            }
            previousAnchor = a;
        }
        ts.add(this.subSet(fromAnchor, true, last(), true));
        return ts;
    }

    public Set<Leg> buildLegs() {
        HashSet<Leg> legs = new HashSet<Leg>();
        Anchor prevA = first();
        for (Anchor a : this) {
            if (a != prevA) {
                legs.add(new Leg(prevA, a));
            }
            prevA = a;
        }
        return legs;
    }

    public Set<Leg> buildLegs(long maxDeltaTimeMillis) {
        HashSet<Leg> legs = new HashSet<Leg>();
        SortedSet<Trajectory> ts = splitTrajectory(maxDeltaTimeMillis);
        for (Trajectory t : ts) {
            legs.addAll(t.buildLegs());
        }
        return legs;
    }

    public Set<ValidationLeg> buildValidationLegs(long maxDeltaTimeMillis) {
        HashSet<ValidationLeg> legs = new HashSet<ValidationLeg>();
        for (Anchor a : this) {
            Trajectory t = tailSet(a, false);
            for (Anchor a2 : t) {
                if (a2.getDate().getTime() - a.getDate().getTime() <= maxDeltaTimeMillis) {
                    Trajectory validators = subSet(a, false, a2, false);
                    if (validators.size() > 0) {
                        legs.add(new ValidationLeg(a, a2, validators));
                    }
                } else {
                    break;
                }
            }
        }
        return legs;
    }

    public Trajectory tailSet(Anchor fromElement) {
        return tailSet(fromElement, true);
    }

    public Trajectory tailSet(Anchor fromElement, boolean inclusive) {
        return new Trajectory(super.tailSet(fromElement, inclusive));
    }

    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     * <p>
     * <p>The implementor must ensure <tt>sgn(x.compareTo(y)) ==
     * -sgn(y.compareTo(x))</tt> for all <tt>x</tt> and <tt>y</tt>.  (This
     * implies that <tt>x.compareTo(y)</tt> must throw an exception iff
     * <tt>y.compareTo(x)</tt> throws an exception.)
     * <p>
     * <p>The implementor must also ensure that the relation is transitive:
     * <tt>(x.compareTo(y)&gt;0 &amp;&amp; y.compareTo(z)&gt;0)</tt> implies
     * <tt>x.compareTo(z)&gt;0</tt>.
     * <p>
     * <p>Finally, the implementor must ensure that <tt>x.compareTo(y)==0</tt>
     * implies that <tt>sgn(x.compareTo(z)) == sgn(y.compareTo(z))</tt>, for
     * all <tt>z</tt>.
     * <p>
     * <p>It is strongly recommended, but <i>not</i> strictly required that
     * <tt>(x.compareTo(y)==0) == (x.equals(y))</tt>.  Generally speaking, any
     * class that implements the <tt>Comparable</tt> interface and violates
     * this condition should clearly indicate this fact.  The recommended
     * language is "Note: this class has a natural ordering that is
     * inconsistent with equals."
     * <p>
     * <p>In the foregoing description, the notation
     * <tt>sgn(</tt><i>expression</i><tt>)</tt> designates the mathematical
     * <i>signum</i> function, which is defined to return one of <tt>-1</tt>,
     * <tt>0</tt>, or <tt>1</tt> according to whether the value of
     * <i>expression</i> is negative, zero or positive.
     *
     * @param o the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException   if the specified object's type prevents it
     *                              from being compared to this object.
     */
    public int compareTo(Trajectory o) {
        if (getStartTime().equals(o.getStartTime())) {
            return getEndTime().compareTo(o.getEndTime());
        } else {
            return getStartTime().compareTo(o.getStartTime());
        }
    }
}
