package com.noobprogrammer.scoutspace.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;


@Document(collection = "User")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    private ObjectId id;

    @NotNull
    @UniqueElements
    private String username;

    @NotNull
    @UniqueElements
    private String email;

    @NotNull
    private String password;

    @CreatedDate
    private Date createddate;

    @CreatedBy
    private String createdby;


    @LastModifiedDate
    private Date modifieddate;

    @LastModifiedBy
    private String modifiedby;
}
