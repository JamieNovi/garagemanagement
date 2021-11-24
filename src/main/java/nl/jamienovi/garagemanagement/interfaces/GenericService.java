package nl.jamienovi.garagemanagement.interfaces;

import java.util.List;

/**
 * Class represents a generic interface that contains contract for common CRUD operations
 * for all services.
 * @param <A> Entity
 * @param <B> Id of entity
 */
public interface GenericService<A,B,C> {
    List<A> findAll();

    A findOne(B id);

    default A add(A type){
        throw new UnsupportedOperationException();
    }

    default void update(B type1,C type2){
        throw new UnsupportedOperationException();
    }

    default void delete(B type) {
        throw new UnsupportedOperationException();
    }

}
