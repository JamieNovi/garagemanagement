package nl.jamienovi.garagemanagement.service;

import java.util.List;

public interface GenericService<A,B,C> {
    List<A> findAll();

    A findOne(B id);

    A add(A type);

    void update(B id,C type);

    void delete(B id);
}
