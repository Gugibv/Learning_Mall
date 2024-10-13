package com.admin.ncs.kaicc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddVaEngineWsDTO {
    private String existVaenginId;
    private String status;
    private List<String> messages;
    private String modelInstallStatus;


}
