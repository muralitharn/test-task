package customerServices.softsuave.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class jwtWrapper {

//        "access_token": "",
//            "acc_expire_time": "",
//            "refresh_token": "",
//            "ref_expire_time": ""

        private String accessToken;
        private Date acc_expire_time;
        private String  refresh_token;
        private int ref_expire_time;


}
