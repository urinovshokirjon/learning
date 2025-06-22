package learning.center.uz.service.company;

import jakarta.transaction.Transactional;
import learning.center.uz.entity.ProfileSubjectEntity;
import learning.center.uz.repository.company.ProfileSubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileSubjectService {
    private final ProfileSubjectRepository profileSubjectRepository;


    @Transactional
    public void merge(String profileId, List<String> newList) {
        if (newList == null) {
            newList = new LinkedList<>();
        }

        // old subject ID list
        List<String> oldList = profileSubjectRepository.findAllIdByProfileId(profileId);

        // remove old subject
        for (String oldId : oldList) {
            if (!newList.contains(oldId)) {
               delete(profileId, oldId);
            }
        }

        // new subject create
        for (String newId : newList) {
            if (!oldList.contains(newId)) {
                create(profileId, newId);
            }
        }
    }


    public void create(String profileId, String subjectId) {
        ProfileSubjectEntity profileSubjectEntity = new ProfileSubjectEntity();
        profileSubjectEntity.setProfileId(profileId);
        profileSubjectEntity.setSubjectId(subjectId);
        profileSubjectRepository.save(profileSubjectEntity);
    }

    public void delete(String profileId, String subjectId) {
        profileSubjectRepository.deleteByProfileIdAndSubjectId(profileId, subjectId);
    }

    public List<String> getProfileSubjectIds(String id) {
        return profileSubjectRepository.findAllIdByProfileId(id);
    }
}

