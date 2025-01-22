package softsuave.AdsCost.entity;

import jakarta.persistence.*;
import lombok.*;
import softsuave.AdsCost.enums.AdsType;


@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ads_Cost_Tabel")
public class AdsCostTabel {

    @Id

    private int ADScostId;

    @Column(name="Ads_prices",nullable = true)
    private long cost;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true, columnDefinition = "varchar(255) default 'OTHERS'")
    private AdsType type;

    @Column(name="interset_Rate")
    private int intersetRate;
}
