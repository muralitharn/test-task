package customerServices.softsuave.dtos;

import lombok.Data;

@Data
public class UserDto {
    private long userId;
    private String username;
    private String password;
}
