package learning.center.uz.config;

import learning.center.uz.entity.ProfileEntity;
import learning.center.uz.enums.ProfileRole;
import learning.center.uz.repository.ProfileRepository;
import learning.center.uz.repository.ProfileRoleRepository;
import learning.center.uz.service.AttachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private ProfileRoleRepository profileRoleRepository;
    @Autowired
    private AttachService attachService;

    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        Optional<ProfileEntity> profileOptional = profileRepository.findByPhone(phone);
        if (profileOptional.isEmpty()) {
            throw new UsernameNotFoundException("Username not found");
        }
        ProfileEntity profileEntity = profileOptional.get();
        List<ProfileRole> roleList = profileRoleRepository.roleListByProfile(profileEntity.getId());
        return new CustomUserDetails(profileEntity, roleList, attachService.getOnlyUrl(profileEntity.getPhotoId()));
    }
}
