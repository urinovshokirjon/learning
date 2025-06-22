package learning.center.uz.config;


import learning.center.uz.entity.ProfileEntity;
import learning.center.uz.entity.ProfileRoleEntity;
import learning.center.uz.enums.ProfileRole;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Getter
public class CustomUserDetails implements UserDetails {
    private String id;
    private String phone;
    private String name;
    private String surname;
    private String password;
    private String companyId;
    private String photoUrl;
    private List<SimpleGrantedAuthority> roleList = new LinkedList<>();


    public CustomUserDetails(ProfileEntity entity, List<ProfileRole> profileRoleList, String photoUrl) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.surname = entity.getSurname();
        this.phone = entity.getPhone();
        this.password = entity.getPassword();
        this.companyId = entity.getCompanyId();
        this.photoUrl = photoUrl;
        for (ProfileRole role : profileRoleList) {
            roleList.add(new SimpleGrantedAuthority(role.name()));
        }
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roleList;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return phone;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }
}
