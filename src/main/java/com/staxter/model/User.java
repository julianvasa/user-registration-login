package com.staxter.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * Simple POJO to hold user data
 *
 * @author Julian Vasa
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class User {
    private int id;
    @NotNull(message = "firstname should not be empty")
    @NotBlank(message = "firstname should not be empty")
    private String firstName;

    @NotNull(message = "lastname should not be empty")
    @NotBlank(message = "lastname should not be empty")
    private String lastName;

    @NotNull(message = "username should not be empty")
    @NotBlank(message = "username should not be empty")
    private String userName;

    @NotNull(message = "password should not be empty")
    @NotBlank(message = "password should not be empty")
    @Size(min = 8)
    @JsonProperty(value = "password",access = JsonProperty.Access.WRITE_ONLY)
    private String plainTextPassword;

    @JsonIgnore
    private String hashedPassword;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id &&
                firstName.equals(user.firstName) &&
                lastName.equals(user.lastName) &&
                userName.equals(user.userName) &&
                plainTextPassword.equals(user.plainTextPassword) &&
                hashedPassword.equals(user.hashedPassword);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, userName, plainTextPassword);
    }

}
