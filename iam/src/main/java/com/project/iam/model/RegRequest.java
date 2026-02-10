package com.project.iam.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegRequest extends LoginRequest {
    private String email;
}
