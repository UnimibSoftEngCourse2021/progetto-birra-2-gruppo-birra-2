package it.progettois.brewday.service.maximizebrew;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "MaximizeBrewService", url = "http://ec2-18-156-174-69.eu-central-1.compute.amazonaws.com:5000")
public interface MaximizeBrewService {

    @PostMapping()
    MaximizeBrewOutput getMaxBrew(MaximizeBrewInput maximizeBrewInput);
}