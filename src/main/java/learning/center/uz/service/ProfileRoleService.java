package learning.center.uz.service;

import learning.center.uz.entity.ProfileRoleEntity;
import learning.center.uz.enums.ProfileRole;
import learning.center.uz.repository.ProfileRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ProfileRoleService {
    private final ProfileRoleRepository profileRoleRepository;

    public void merge(String profileId, List<ProfileRole> newList) {
        List<ProfileRole> oldList = getProfileRoles(profileId);
        // create new role
        for (ProfileRole role : newList) {
            if (oldList.stream().noneMatch(r -> r.equals(role))) {
                create(profileId, role);
            }
        }
        // remove role
        for (ProfileRole role : oldList) {
            if (!newList.contains(role)) {
                profileRoleRepository.deleteRoleFromProfile(profileId, role);
            }
        }
    }

    public void create(String personId, ProfileRole role) {
        ProfileRoleEntity profileRoleEntity = new ProfileRoleEntity();
        profileRoleEntity.setRole(role);
        profileRoleEntity.setProfileId(personId);
        profileRoleRepository.save(profileRoleEntity);
    }

    public List<ProfileRole> getProfileRoles(String profileId) {
        return profileRoleRepository.roleListByProfile(profileId);
    }
}
