package com.resturant.management.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
public class UserTokenModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(targetEntity = UserModel.class,fetch = FetchType.EAGER)
    private UserModel user;

    private String token;

    private Date createdDate;

    private Date modifiedDate;


}
