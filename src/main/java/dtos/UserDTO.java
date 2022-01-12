package dtos;

import entities.User;

import java.util.List;

public class UserDTO {
    private String username;
    private String password;
    private List<RoleDTO> roles;

    public UserDTO(User entity) {
        this.username = entity.getUserName();
        this.password = entity.getUserPass();
        this.roles = RoleDTO.getDTOs(entity.getRoleList());
    }
}
