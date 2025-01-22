package softsuave.AdsCost.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softsuave.AdsCost.entity.AdsCostTabel;
import softsuave.AdsCost.enums.AdsType;
import softsuave.AdsCost.exception.adsCostException;
import softsuave.AdsCost.repo.AdsTypeRepository;

@Service
public class AdsServices {

    @Autowired
    AdsTypeRepository adsTypeRepository;

    public String getAdsPrices(AdsType type, int days) throws adsCostException {
        if (type != null) {
            AdsCostTabel adsCostTabel = adsTypeRepository.findByType(type);
            if (adsCostTabel.getCost() > 0.0) {
                int billAmount = days * (int) adsCostTabel.getCost();
                String response = "for the " + type + "of ads group and your required days you will be charged about " + billAmount;
                return response;
            } else {
                throw new adsCostException("days can't be lesser than zero");
            }
        }
        throw new adsCostException("ads type is mandatory");
    }
}
