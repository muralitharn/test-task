package softsuave.AdsCost.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import softsuave.AdsCost.entity.AdsCostTabel;
import softsuave.AdsCost.enums.AdsType;
import softsuave.AdsCost.exception.adsCostException;
import softsuave.AdsCost.repo.AdsTypeRepository;
import softsuave.AdsCost.services.AdsServices;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping(value = "/AdsCosting-api")
public class AdsCostAndPricing {

    @Autowired
    AdsServices adsServices;

    @Autowired
    AdsTypeRepository adsTypeRepository;

    @GetMapping("/choose-ads")
    public ResponseEntity<Object> getAdsPrices(@RequestParam(name = "adtype") AdsType type, @RequestParam("days") int days) throws adsCostException {
        AdsCostTabel adsCostTabel = adsTypeRepository.findByType(AdsType.EDUCATION);
        if (adsCostTabel == null) {
            List<AdsCostTabel> adsCost = new ArrayList<>();
            adsCost.add(new AdsCostTabel(1, 1000, AdsType.EDUCATION,2));
            adsCost.add(new AdsCostTabel(2, 2000, AdsType.BUSINESS,4));
            adsCost.add(new AdsCostTabel(3, 3000, AdsType.ENTERTAINMENT,7));
            adsCost.add(new AdsCostTabel(4, 4000, AdsType.POLITICS,12));
            adsCost.add(new AdsCostTabel(5, 5000, AdsType.OTHERS,5));
            adsCost.add(new AdsCostTabel(6, 6000, AdsType.PERSONAL,7));
            adsTypeRepository.saveAll(adsCost);
        }
        String response = adsServices.getAdsPrices(type, days);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/find-ads/{role}")
    public AdsCostTabel findAds(@PathVariable(name="role")String role ){
       return adsTypeRepository.findByType(AdsType.valueOf(role.toUpperCase()));
    }

    @GetMapping("/is-payment/completed")
    public boolean ThirdpartyServices(){
        Random rr= new Random();
        return rr.nextBoolean();
    }
}
