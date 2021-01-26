package it.progettois.brewday.service.maximizebrew;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "MaximizeBrewService", url = "${feign.client.url.serviceUrl}")
public interface MaximizeBrewService {

    @PostMapping()
    MaximizeBrewOutput getMaxBrew(MaximizeBrewInput maximizeBrewInput);
}
