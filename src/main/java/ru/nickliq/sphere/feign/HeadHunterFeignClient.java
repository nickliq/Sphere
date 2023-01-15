package ru.nickliq.sphere.feign;

import org.springframework.cloud.openfeign.FeignClient;
import ru.nickliq.sphere.config.FeignConfig;

@FeignClient(url = "https://api.hh.ru", name = "hh", configuration = FeignConfig.class)
public interface HeadHunterFeignClient extends HeadHunterApi {
}
