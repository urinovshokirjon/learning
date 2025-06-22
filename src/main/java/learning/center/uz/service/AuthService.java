package learning.center.uz.service;

import learning.center.uz.dto.auth.AuthDTO;
import learning.center.uz.dto.admin.profile.ProfileDTO;
import learning.center.uz.entity.ProfileEntity;
import learning.center.uz.entity.ProfileRoleEntity;
import learning.center.uz.enums.ProfileRole;
import learning.center.uz.enums.ProfileStatus;
import learning.center.uz.exp.AppBadException;
import learning.center.uz.repository.ProfileRepository;
import learning.center.uz.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;


@Service
public class AuthService {
    @Autowired
    private ProfileRepository profileRepository;

    public ProfileDTO login(AuthDTO dto) {
        Optional<ProfileEntity> optional = profileRepository.getProfile(dto.getPhone(), MD5Util.encode(dto.getPassword()));
        if (optional.isEmpty()) {
            throw new AppBadException("profile.not.fount");
        }
        ProfileEntity profileEntity = optional.get();
        if (!profileEntity.getStatus().equals(ProfileStatus.ACTIVE)) {
            throw new AppBadException("profile.not.fount");
        }
        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setName(profileEntity.getName());
        profileDTO.setSurname(profileEntity.getSurname());
        profileDTO.setPhone(profileEntity.getPhone());
        List<ProfileRole> roles = new LinkedList<>();
        List<ProfileRoleEntity> profileRoles = profileEntity.getProfileRoles();
        for (ProfileRoleEntity profileRole : profileRoles) {
            roles.add(profileRole.getRole());
        }
        profileDTO.setRoles(roles);
        return profileDTO;
    }
}
