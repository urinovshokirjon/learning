package learning.center.uz.service;

import learning.center.uz.dto.subject.SubjectDTO;
import learning.center.uz.dto.subject.SubjectFilterDTO;
import learning.center.uz.entity.SubjectEntity;
import learning.center.uz.enums.ProfileRole;
import learning.center.uz.exp.AppBadException;
import learning.center.uz.mapper.SubjectMapperI;
import learning.center.uz.repository.company.CompanyRepository;
import learning.center.uz.repository.SubjectRepository;
import learning.center.uz.repository.custom.SubjectCustomRepository;
import learning.center.uz.service.company.CompanyService;
import learning.center.uz.util.SpringSecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class SubjectService {
    private final SubjectRepository subjectRepository;
    private final SubjectCustomRepository subjectCustomRepository;
    private final CompanyRepository companyRepository;

    @Autowired
    private ProfileService profileService;

    public List<SubjectMapperI> getSubjectList() {
        String companyId = profileService.getRequestedProfileCompanyId();
        return subjectRepository.findByCompanyId(companyId);
    }

    public Page<SubjectDTO> getSubjectList(SubjectFilterDTO filterDTO, int page, int size) {
        String companyId = profileService.getRequestedProfileCompanyId();
        PageImpl<SubjectDTO> filter = subjectCustomRepository.filter(filterDTO, companyId, page - 1, size);
        return filter;
    }

    public void create(SubjectDTO dto) {
        List<SimpleGrantedAuthority> roleList = SpringSecurityUtil.getCurrentUserDetail().getRoleList();
        SubjectEntity entity = new SubjectEntity();
        entity.setCompanyId(profileService.getRequestedProfileCompanyId());
        entity.setName(dto.getName());
        subjectRepository.save(entity);
    }

    public SubjectDTO getBySubjectId(String subjectId) {
        Optional<SubjectEntity> optional = subjectRepository.getSubjectById(subjectId);
        return toDTO(optional.get());
    }

    private SubjectDTO toDTO(SubjectEntity entity) {
        SubjectDTO dto = new SubjectDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        return dto;
    }


    public boolean delete(String id) {
        Integer effectiveRow = subjectRepository.deleteSubject(id);
        return effectiveRow != 0;
    }

    public void update(String id, SubjectDTO dto) {
        Optional<SubjectEntity> optional = subjectRepository.getSubjectById(id);
        SubjectEntity entity = optional.get();
        entity.setName(dto.getName());
        entity.setUpdatedDate(LocalDateTime.now());
        subjectRepository.save(entity);
    }
}
