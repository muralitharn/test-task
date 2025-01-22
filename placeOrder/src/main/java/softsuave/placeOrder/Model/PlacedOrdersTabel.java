package softsuave.placeOrder.Model;
import jakarta.persistence.*;

import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

import static java.lang.reflect.Modifier.isStatic;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Placed_Orders_Tabel"
        , indexes = {
//        @Index(name = "employee_creation_date", columnList = "creation_date")
}
)
public class PlacedOrdersTabel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID  orderNo;

    @NotNull
    private boolean OrderStatus;

    @NotNull
    private boolean orderCancellation;

    @NotNull
    int customerId;

    @OneToMany
    private Set<AdsCostTabel> adsCost;

    private int totalCost;

    @NotNull
    private int days;

    @NotNull
    private String customerName;

     @NonNull
    private boolean isUpdatedtoSuperuser;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation_date", insertable =true, updatable = false)
    private Date creationDate=new Date();

    public PlacedOrdersTabel(boolean OrderStatus, int customerId, int days, Set<AdsCostTabel> adsCost, boolean isUpdatedtoSuperuser) {
        this.OrderStatus = OrderStatus;
        this.customerId = customerId;
        this.days = days;
        this.adsCost = adsCost;
        this.isUpdatedtoSuperuser= isUpdatedtoSuperuser;
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class orderDto {
        private boolean orderStatus;
        private int customerId;
        private int days;
        private Set<String > adsCost;
        private boolean isUpdatedtoSuperuser;

    }

}

