package nl.gijspeters.pubint.structure;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Reference;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by gijspeters on 18-10-16.
 */
@Entity("ValidationLeg")
public class ValidationLeg extends Leg {

    @Reference
    private Set<Anchor> validators = new HashSet<Anchor>();

    public ValidationLeg() {
        super();
    }

    public ValidationLeg(Anchor origin, Anchor destination, Collection<Anchor> validators) {
        super(origin, destination);
        this.validators.addAll(validators);
    }

    public Set<Anchor> getValidators() {
        return validators;
    }

}
