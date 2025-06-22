package learning.center.uz.service;

import learning.center.uz.dto.admin.profile.ProfileDTO;
import learning.center.uz.dto.group.GroupCreatedDTO;
import learning.center.uz.dto.group.GroupDTO;
import learning.center.uz.dto.group.GroupFilterDTO;
import learning.center.uz.entity.AttachEntity;
import learning.center.uz.entity.GroupEntity;
import learning.center.uz.entity.GroupScheduleEntity;
import learning.center.uz.enums.ProfileRole;
import learning.center.uz.exp.AppBadException;
import learning.center.uz.mapper.GroupMapper;
import learning.center.uz.mapper.GroupNamesMapper;
import learning.center.uz.repository.BranchRepository;
import learning.center.uz.repository.GroupRepository;
import learning.center.uz.repository.company.CompanyRepository;
import learning.center.uz.repository.custom.GroupCustomRepository;
import learning.center.uz.service.company.CompanyService;
import learning.center.uz.util.SpringSecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GroupService {
    private final GroupRepository groupRepository;
    private final GroupScheduleService groupScheduleService;
    private final GroupCustomRepository groupCustomRepository;
    private final CompanyRepository companyRepository;
    private final BranchRepository branchRepository;
    private final AttachService attachService;


    public void create(GroupDTO dto) {
        GroupEntity entity = new GroupEntity();
        entity.setName(dto.getName());
        entity.setTeacherId(dto.getTeacherId());
        entity.setSubjectId(dto.getSubjectId());
        entity.setBranchId(dto.getBranchId() != null ? dto.getBranchId() : branchRepository.getBranchIdByManagerId(SpringSecurityUtil.getCurrentUserId()));
        entity.setStartDate(dto.getStartDate());
        entity.setDuration(dto.getDuration());
        entity.setFinishedDate(dto.getStartDate().plusMonths(dto.getDuration()));
        entity.setCreatedDate(LocalDateTime.now());
        GroupEntity saved = groupRepository.save(entity);
        groupScheduleService.save(saved.getId(), dto);
    }

    public GroupDTO getById(String id) {
        GroupEntity groupEntity = byId(id);

        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setId(groupEntity.getId());
        groupDTO.setName(groupEntity.getName());
        groupDTO.setTeacherName(groupEntity.getTeacher().getName());
        groupDTO.setTeacherSurname(groupEntity.getTeacher().getSurname());
        groupDTO.setTeacherPhone(groupEntity.getTeacher().getPhone());
        if (groupEntity.getTeacher().getPhotoId() != null) {
            groupDTO.setTeacherPhotoUrl(attachService.getOnlyUrl(groupEntity.getTeacher().getPhotoId()));
        }
        groupDTO.setSubjectName(groupEntity.getSubject().getName());
        groupDTO.setStartDate(groupEntity.getStartDate());
        groupDTO.setDuration(groupEntity.getDuration());
        groupDTO.setCreatedDate(groupEntity.getCreatedDate().toLocalDate());

        groupDTO.setMondaySelected(false);
        groupDTO.setTuesdaySelected(false);
        groupDTO.setWednesdaySelected(false);
        groupDTO.setThursdaySelected(false);
        groupDTO.setFridaySelected(false);
        groupDTO.setSaturdaySelected(false);
        groupDTO.setSundaySelected(false);

        List<GroupScheduleEntity> groupScheduleList = groupScheduleService.getByGroupId(id);
        for (GroupScheduleEntity entity : groupScheduleList) {
            switch (entity.getDayOfWeek().toUpperCase()) {
                case "MONDAY" -> {
                    groupDTO.setMondaySelected(true);
                    groupDTO.setMondayTime(entity.getTime());
                }
                case "TUESDAY" -> {
                    groupDTO.setTuesdaySelected(true);
                    groupDTO.setTuesdayTime(entity.getTime());
                }
                case "WEDNESDAY" -> {
                    groupDTO.setWednesdaySelected(true);
                    groupDTO.setWednesdayTime(entity.getTime());
                }
                case "THURSDAY" -> {
                    groupDTO.setThursdaySelected(true);
                    groupDTO.setThursdayTime(entity.getTime());
                }
                case "FRIDAY" -> {
                    groupDTO.setFridaySelected(true);
                    groupDTO.setFridayTime(entity.getTime());
                }
                case "SATURDAY" -> {
                    groupDTO.setSaturdaySelected(true);
                    groupDTO.setSaturdayTime(entity.getTime());
                }
                case "SUNDAY" -> {
                    groupDTO.setSundaySelected(true);
                    groupDTO.setSundayTime(entity.getTime());
                }
                default -> {
                    return null;
                }
            }
        }
        return groupDTO;
    }

    public void update(String groupId, GroupDTO dto) {
        GroupEntity groupEntity = groupRepository.getById(groupId).orElse(null);
        groupEntity.setName(dto.getName());
        groupEntity.setSubjectId(dto.getSubjectId());
        groupEntity.setTeacherId(dto.getTeacherId());
        groupEntity.setStartDate(dto.getStartDate());
        groupEntity.setDuration(dto.getDuration());
        groupEntity.setFinishedDate(dto.getStartDate().plusMonths(dto.getDuration()));
        groupEntity.setUpdatedDate(LocalDateTime.now());
        groupRepository.save(groupEntity);
        groupScheduleService.save(groupId, dto);
    }

    public boolean delete(String id) {
        int effectedRows = groupRepository.deleteById(id, branchRepository.getBranchIdByManagerId(SpringSecurityUtil.getCurrentUserId()));
        return effectedRows != 0;
    }

    public Page<GroupDTO> getGroupList(GroupFilterDTO filterDTO, Integer page, Integer size) {
        List<SimpleGrantedAuthority> roleList = SpringSecurityUtil.getCurrentUserDetail().getRoleList();

        if (roleList.stream().anyMatch(role -> role.getAuthority().equals(ProfileRole.ROLE_COMPANY_MANAGER.toString()))) {
            String companyId = companyRepository.getCompanyIdByOwnerId(SpringSecurityUtil.getCurrentUserId());
            return groupCustomRepository.filter(filterDTO, page - 1, size, companyId, null, null);
        } else if (roleList.stream().anyMatch(role -> role.getAuthority().equals(ProfileRole.ROLE_BRANCH_MANAGER.toString()))) {
            String branchId = branchRepository.getBranchIdByManagerId(SpringSecurityUtil.getCurrentUserId());
            return groupCustomRepository.filter(filterDTO, page - 1, size, null, branchId, null);
        } else if (roleList.stream().anyMatch(role -> role.getAuthority().equals(ProfileRole.ROLE_TEACHER.toString()))) {
            String teacherId = SpringSecurityUtil.getCurrentUserId();
            return groupCustomRepository.filter(filterDTO, page - 1, size, null, null, teacherId);
        }
        return null;
    }

    public List<GroupMapper> getAllGroups() {
        List<SimpleGrantedAuthority> roleList = SpringSecurityUtil.getCurrentUserDetail().getRoleList();

        if (roleList.stream().anyMatch(role -> role.getAuthority().equals(ProfileRole.ROLE_COMPANY_MANAGER.toString()))) {
            String companyId = companyRepository.getCompanyIdByOwnerId(SpringSecurityUtil.getCurrentUserId());
            return groupRepository.getAllGroupsByCompanyId(companyId);
        } else if (roleList.stream().anyMatch(role -> role.getAuthority().equals(ProfileRole.ROLE_BRANCH_MANAGER.toString()))) {
            String branchId = branchRepository.getBranchIdByManagerId(SpringSecurityUtil.getCurrentUserId());
            return groupRepository.getAllGroupsByBranchId(branchId);
        } else if (roleList.stream().anyMatch(role -> role.getAuthority().equals(ProfileRole.ROLE_TEACHER.toString()))) {
            String teacherId = SpringSecurityUtil.getCurrentUserId();
            return groupRepository.getAllGroupsByTeacherId(teacherId);
        }
        return null;
    }

    public List<GroupNamesMapper> getGroupNames(String studentId) {
        List<SimpleGrantedAuthority> roleList = SpringSecurityUtil.getCurrentUserDetail().getRoleList();

        if (roleList.stream().anyMatch(role -> role.getAuthority().equals(ProfileRole.ROLE_COMPANY_MANAGER.toString()))) {
            String companyId = companyRepository.getCompanyIdByOwnerId(SpringSecurityUtil.getCurrentUserId());
            return groupRepository.getGroupNames(companyId, studentId, true);
        } else if (roleList.stream().anyMatch(role -> role.getAuthority().equals(ProfileRole.ROLE_BRANCH_MANAGER.toString()))) {
            String branchId = branchRepository.getBranchIdByManagerId(SpringSecurityUtil.getCurrentUserId());
            return groupRepository.getGroupNames(branchId, studentId, false);
        }
        return null;
    }

    public GroupMapper getGroupById(String groupId) {
        return groupRepository.getGroupById(groupId);
    }

    public GroupEntity byId(String id) {
        return groupRepository.getById(id).orElseThrow(() -> {
            throw new AppBadException("Group Not Found");
        });
    }
}
