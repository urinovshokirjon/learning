package learning.center.uz.service;

import learning.center.uz.dto.StudentGroupRequest;
import learning.center.uz.dto.group.StudentGroupDTO;
import learning.center.uz.entity.StudentGroupEntity;
import learning.center.uz.enums.ProfileRole;
import learning.center.uz.exp.AppBadException;
import learning.center.uz.mapper.StudentGroupMapper;
import learning.center.uz.repository.BranchRepository;
import learning.center.uz.repository.StudentGroupRepository;
import learning.center.uz.repository.company.CompanyRepository;
import learning.center.uz.util.SpringSecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentGroupService {
    private final StudentGroupRepository studentGroupRepository;
    private final CompanyRepository companyRepository;
    private final BranchRepository branchRepository;

    public StudentGroupEntity save(StudentGroupDTO dto) {
        StudentGroupEntity entity = new StudentGroupEntity();
        entity.setStudentId(dto.getStudentId());
        entity.setGroupId(dto.getGroupId());
        entity.setJoinedDate(LocalDate.now());
        return studentGroupRepository.save(entity);
    }

    public Page<StudentGroupMapper> getStudentGroupList(String studentId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        List<SimpleGrantedAuthority> roleList = SpringSecurityUtil.getCurrentUserDetail().getRoleList();

        if (roleList.stream().anyMatch(role -> role.getAuthority().equals(ProfileRole.ROLE_COMPANY_MANAGER.toString()))) {
            String companyId = companyRepository.getCompanyIdByOwnerId(SpringSecurityUtil.getCurrentUserId());
            return studentGroupRepository.getStudentGroupMapperByStudentId(pageable, companyId, studentId, true);
        } else if (roleList.stream().anyMatch(role -> role.getAuthority().equals(ProfileRole.ROLE_BRANCH_MANAGER.toString()))) {
            String branchId = branchRepository.getBranchIdByManagerId(SpringSecurityUtil.getCurrentUserId());
            return studentGroupRepository.getStudentGroupMapperByStudentId(pageable, branchId, studentId, false);
        }
        return null;
    }

    public void create(String studentId, StudentGroupRequest studentGroupRequest) {
        StudentGroupEntity entity = new StudentGroupEntity();
        entity.setStudentId(studentId);
        entity.setGroupId(studentGroupRequest.getGroupId());
        entity.setJoinedDate(LocalDate.now());
        entity.setCreatedDate(LocalDateTime.now());
        studentGroupRepository.save(entity);
    }

    public StudentGroupDTO getStudentGroupByStudentId(String studentId, String groupId) {
        StudentGroupEntity entity = getSGByStudentId(studentId,groupId);
        StudentGroupDTO dto = new StudentGroupDTO();
        dto.setId(entity.getId());
        dto.setStudentId(studentId);
        dto.setGroupId(entity.getGroupId());
        dto.setJoinedDate(entity.getJoinedDate());
        dto.setLeftDate(entity.getLeftDate());
        return dto;
    }

    private StudentGroupEntity getSGByStudentId(String studentId, String groupId) {
        Optional<StudentGroupEntity> optional = studentGroupRepository.getStudentGroupByStudentIdAndGroupId(studentId, groupId);
        if (optional.isEmpty()) {
            throw new AppBadException("student not found");
        }
        return optional.get();
    }

    public void deleteStudentFromGroup(String studentGroupId) {
        studentGroupRepository.deleteStudentFromGroup(studentGroupId);
    }

    public void delete(StudentGroupDTO dto) {
        studentGroupRepository.updateStatus(dto.getId(), dto.getGroupId());
    }
}
