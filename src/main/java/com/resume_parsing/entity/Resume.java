package com.resume_parsing.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Resume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int companyId;
    private String candidateName;
    private String email;
    private String contactNumber;
    private String education;
    private Float experience;
    private int pinCode;
    private String linkedIn;
    private String city;
    private String states;
    private String country;
    private int addedBy;
    private int updatedBy;
    private String addedOn;
    private String updatedOn;
}
