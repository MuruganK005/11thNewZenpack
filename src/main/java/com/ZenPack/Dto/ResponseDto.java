package com.ZenPack.Dto;



import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseDto {
    private String responseMessage;
    private String responseCode;
    private Integer statusCode;
    private String responseDescription;
    private Object data;
    private ZenPackDto Jdata;
    private Boolean validation=false;

}
