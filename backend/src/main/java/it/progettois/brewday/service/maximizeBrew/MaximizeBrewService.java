package it.progettois.brewday.service.maximizeBrew;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "MaximizeBrewService", url = "http://ec2-18-156-174-69.eu-central-1.compute.amazonaws.com:5000")
public interface MaximizeBrewService {

    @RequestMapping(method = RequestMethod.POST)
    MaximizeBrewOutput getMaxBrew(MaximizeBrewInput maximizeBrewInput);
}
