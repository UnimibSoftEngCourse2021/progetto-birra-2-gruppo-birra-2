package it.progettois.brewday.service.maximizeBrew;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "MaximizeBrewService", url = "http://localhost:5000")
public interface MaximizeBrewService {

    @RequestMapping(method = RequestMethod.POST)
    MaximizeBrewOutput getMaxBrew(MaximizeBrewInput maximizeBrewInput);
}
