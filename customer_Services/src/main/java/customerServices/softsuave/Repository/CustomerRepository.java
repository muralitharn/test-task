package customerServices.softsuave.Repository;

import customerServices.softsuave.cuustomerServicse.CustomerException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import customerServices.softsuave.newModel.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    Customer findByCustomerName(String username)throws CustomerException;


    //@Query(nativeQuery = true, value = "SELECT * FROM customer cus where cus.email=:email")
    Customer findByEmail( String email);

//    @Query(value = "SELECT * FROM Customer WHERE mail = :mail", nativeQuery = true)
//    Customer findByEmail(@Param("mail") String mail);

}
