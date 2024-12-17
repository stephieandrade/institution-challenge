package com.test.challenge.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.test.challenge.model.User;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstitutionDTO {
    private Long institutionId;
    private String name;
    private List<User> users;

}
