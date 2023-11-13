package com.example.UsersService.records;

import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;


public record UserUpdateRequest(@Length(max = 40) String firstName,@Length(max = 40) String lastName, @Length(max = 40) String city) {
}
