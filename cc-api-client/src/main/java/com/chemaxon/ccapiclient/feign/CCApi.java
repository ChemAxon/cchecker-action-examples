package com.chemaxon.ccapiclient.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.chemaxon.ccapiclient.request.CheckWithIdRequest;
import com.chemaxon.ccapiclient.resource.Substance;
import com.chemaxon.ccapiclient.response.CheckWithIdResult;

@FeignClient("cc-api")
public interface CCApi {

    @RequestMapping(value = "cc-api/check-with-id/", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CheckWithIdResult> check(@RequestBody CheckWithIdRequest checkRequest);

    @RequestMapping(
            value = "cc-api/substances/",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Substance> getSubstances();
}
