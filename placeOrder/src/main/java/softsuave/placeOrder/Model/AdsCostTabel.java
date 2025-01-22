package softsuave.placeOrder.Model;
import jakarta.persistence.*;
import lombok.*;
import softsuave.placeOrder.enums.AdsType;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class AdsCostTabel {

    @Id
    private int ADScostId;

    @Column(name="Ads_prices",nullable = true)
    private long cost;

    @Column(nullable = true, columnDefinition = "varchar(255) default 'OTHERS'")
    @Enumerated(EnumType.STRING)
    private AdsType type;

    @Column(name="interset_Rate")
    private int intersetRate;

}