package nl.jamienovi.garagemanagement.invoice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.List;

@Repository
public class InvoiceRepository {
    private final EntityManagerFactory emf;

    @Autowired
    public InvoiceRepository(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @SuppressWarnings("unchecked")
    public InvoiceCustomerDataDto getCustomerData(Integer customerId){
        EntityManager entityManager = emf.createEntityManager();
        TypedQuery<InvoiceCustomerDataDto> query = (TypedQuery<InvoiceCustomerDataDto>) entityManager
                .createQuery(
                "SELECT new nl.jamienovi.garagemanagement.invoice.InvoiceCustomerDataDto(" +
                        "k.firstName,k.lastName,k.address,k.postalCode,k.city,k.email, " +
                        "c.brand, c.model, c.registrationPlate, " +
                        "ro.id, ro.status) " +
                        "FROM Customer as k " +
                        "INNER JOIN Car c on k.id = c.customer.id " +
                        "INNER JOIN RepairOrder as ro on ro.customer.id = k.id " +
                        "WHERE k.id = :id"
        );
        query.setParameter("id",customerId);

      return query.getSingleResult();
    }

    @SuppressWarnings("unchecked")
    public List<CarPartOrderlineDto> getInvoiceOrderLinesCarparts(Integer customerId) {
        EntityManager entityManager = emf.createEntityManager();

        try {
            TypedQuery<CarPartOrderlineDto> query = (TypedQuery<CarPartOrderlineDto>) entityManager
                    .createQuery(
                            "SELECT new nl.jamienovi.garagemanagement.invoice.CarPartOrderlineDto(" +
                                    "ro.id, ro.customer.id, cp.name, rol.orderLinePrice, rol.orderLineQuantity)" +
                                    "FROM RepairOrder as ro " +
                                    "INNER JOIN RepairOrderLine as rol on ro.id = rol.repairOrder.id " +
                                    "INNER JOIN CarPart as cp on rol.carpart.id = cp.id " +
                                    "AND ro.customer.id = :id" +
                                    " AND cp.typeofItem = 'PART_ITEM'"
                    );
            query.setParameter("id", customerId);
            List<CarPartOrderlineDto> orderLines = query.getResultList();
            return orderLines;
        }catch (NoResultException ex){
            throw new NoResultException("No results");
        }
    }

    @SuppressWarnings("unchecked")
    public List<CarPartOrderlineDto> getInvoiceLaborOrderLines(Integer customerid) {
        EntityManager entityManager = emf.createEntityManager();

        TypedQuery<CarPartOrderlineDto> query = (TypedQuery<CarPartOrderlineDto>) entityManager
                .createQuery(
                        "SELECT new nl.jamienovi.garagemanagement.invoice.CarPartOrderlineDto(" +
                                "ro.id, ro.customer.id," +
                                " l.name," +
                                " rol.orderLinePrice, rol.orderLineQuantity)" +
                                "FROM RepairOrder as ro " +
                                "INNER JOIN RepairOrderLine as rol on ro.id = rol.repairOrder.id " +
                                "INNER JOIN Labor as l on rol.labor.id = l.id " +
                                "AND ro.customer.id =:id" +
                                " AND l.type = 'LABOR_ITEM'"
                );
        query.setParameter("id",customerid);
        List<CarPartOrderlineDto> orderLines = query.getResultList();
        return orderLines;

    }
}