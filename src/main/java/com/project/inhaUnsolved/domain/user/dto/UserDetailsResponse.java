package com.project.inhaUnsolved.domain.user.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class UserDetailsResponse {
    
    private int count;

    private List<UserDetail> items;


}