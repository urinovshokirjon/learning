package learning.center.uz.service;

import learning.center.uz.dto.student.StudentFilterDTO;
import learning.center.uz.dto.student.StudentRequest;
import learning.center.uz.dto.student.StudentResponse;
import learning.center.uz.entity.StudentEntity;
import learning.center.uz.enums.ProfileRole;
import learning.center.uz.enums.StudentStatus;
import learning.center.uz.enums.StudyStatus;
import learning.center.uz.exp.AppBadException;
import learning.center.uz.mapper.StudentMapper;
import learning.center.uz.repository.BranchRepository;
import learning.center.uz.repository.StudentRepository;
import learning.center.uz.repository.company.CompanyRepository;
import learning.center.uz.repository.custom.StudentCustomRepository;
import learning.center.uz.util.MD5Util;
import learning.center.uz.util.SpringSecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final StudentCustomRepository studentCustomRepository;
    private final AttachService attachService;
    private final CompanyRepository companyRepository;
    private final BranchRepository branchRepository;
    @Autowired
    private ProfileService profileService;

    public void create(StudentRequest student, MultipartFile file) {
        Optional<StudentEntity> optional = studentRepository.findByPhone(student.getPhone());
        if (optional.isPresent()) {
            throw new AppBadException("phone already in use");
        }

        StudentEntity entity = new StudentEntity();
        entity.setName(student.getName());
        entity.setSurname(student.getSurname());
        entity.setPhone(student.getPhone());
        entity.setParentPhone(student.getParentPhone());
        if (!file.isEmpty()) {
            entity.setPhotoId(attachService.save2(file));
        }
        entity.setPassword(MD5Util.encode(student.getPassword()));
        entity.setAddress(student.getAddress());
        entity.setDateOfBirth(student.getDateOfBirth());
        entity.setGender(student.getGender());
        entity.setStudentStatus(StudentStatus.ACTIVE);
        entity.setStudyStatus(StudyStatus.LEARNING);
        entity.setBranchId(student.getBranchId() != null ? student.getBranchId() : branchRepository.getBranchIdByManagerId(SpringSecurityUtil.getCurrentUserId()));
        entity.setCompanyId(profileService.getRequestedProfileCompanyId());
        entity.setCreatedDate(LocalDateTime.now());
        studentRepository.save(entity);
    }


    public Page<StudentResponse> getStudentList(StudentFilterDTO filterDTO, int page, int size) {

        List<SimpleGrantedAuthority> roleList = SpringSecurityUtil.getCurrentUserDetail().getRoleList();

        if (roleList.stream().anyMatch(role -> role.getAuthority().equals(ProfileRole.ROLE_COMPANY_MANAGER.toString()))) {
            String companyId = companyRepository.getCompanyIdByOwnerId(SpringSecurityUtil.getCurrentUserId());
            return studentCustomRepository.filter(filterDTO, page - 1, size, companyId, null);
        } else if (roleList.stream().anyMatch(role -> role.getAuthority().equals(ProfileRole.ROLE_BRANCH_MANAGER.toString()))) {
            String branchId = branchRepository.getBranchIdByManagerId(SpringSecurityUtil.getCurrentUserId());
            return studentCustomRepository.filter(filterDTO, page - 1, size, null, branchId);
        }
        return null;
    }

    public boolean delete(String studentId) {
        int effectedRows = studentRepository.deleteById(studentId, SpringSecurityUtil.getCurrentUserId());
        return effectedRows != 0;
    }

    private StudentEntity getStudent(String studentId) {
        Optional<StudentEntity> optional = studentRepository.findById(studentId);
        if (optional.isEmpty()) {
            throw new AppBadException("student not found");
        }
        return optional.get();
    }

    public StudentResponse getStudentById(String studentId) {
        return toDTO(getStudent(studentId));
    }

    private StudentResponse toDTO(StudentEntity studentEntity) {
        StudentResponse studentResponse = new StudentResponse();
        studentResponse.setId(studentEntity.getId());
        studentResponse.setName(studentEntity.getName());
        studentResponse.setSurname(studentEntity.getSurname());
        studentResponse.setPhone(studentEntity.getPhone());
        studentResponse.setParentPhone(studentEntity.getParentPhone());
        if (studentEntity.getPhotoId() != null) {
            studentResponse.setPhotoUrl(attachService.getOnlyUrl(studentEntity.getPhotoId()));
        }
        studentResponse.setAddress(studentEntity.getAddress());
        studentResponse.setDateOfBirth(studentEntity.getDateOfBirth());
        studentResponse.setGender(studentEntity.getGender());
        studentResponse.setStudentStatus(studentEntity.getStudentStatus());
        studentResponse.setStudyStatus(studentEntity.getStudyStatus());
        return studentResponse;
    }


    public void update(String studentId, StudentRequest studentRequest, MultipartFile file) {
        StudentEntity studentEntity = getStudent(studentId);

        if (!file.isEmpty()) {
            String photoId = attachService.save2(file);
            studentEntity.setPhotoId(photoId);
        }
        studentEntity.setName(studentRequest.getName());
        studentEntity.setSurname(studentRequest.getSurname());
        studentEntity.setPhone(studentRequest.getParentPhone());
        studentEntity.setParentPhone(studentRequest.getParentPhone());
        studentEntity.setAddress(studentRequest.getAddress());
        studentEntity.setDateOfBirth(studentRequest.getDateOfBirth() != null ? studentRequest.getDateOfBirth() : studentEntity.getDateOfBirth());
        studentEntity.setGender(studentRequest.getGender());
        // studentEntity.setBranchId(studentRequest.getBranchId());
        studentRepository.save(studentEntity);
    }

    public List<StudentEntity> getAllStudentByGroupId(String groupId) {
        return studentRepository.getAllStudentByGroupId(groupId);
    }

    public List<StudentResponse> findStudentByGroupId(String groupId) {
        List<StudentMapper> mapperList = studentRepository.findAllStudentByGroupId(groupId);
        List<StudentResponse> studentList = new ArrayList<>();
        for (StudentMapper studentMapper : mapperList) {
            StudentResponse studentResponse = new StudentResponse();
            studentResponse.setId(studentMapper.getId());
            studentResponse.setName(studentMapper.getName());
            studentResponse.setSurname(studentMapper.getSurname());
            studentResponse.setPhone(studentMapper.getPhone());
            if (studentMapper.getPhotoId() != null) {
                studentResponse.setPhotoUrl(attachService.getOnlyUrl(studentMapper.getPhotoId()));
            }
            studentResponse.setStudyStatus(studentMapper.getStatus());
            studentResponse.setJoinedDate(studentMapper.getJoinedDate());
            studentResponse.setLeftDate(studentMapper.getLeftDate());
            studentList.add(studentResponse);
        }
        return studentList;
    }

    public List<StudentEntity> getBranchStudentsByGroupId(String groupId) {
        return studentRepository.getBranchStudentsByGroupId(groupId);
    }

    public void updateStudyStatus(String studentId, StudyStatus studyStatus, String message) {
        studentRepository.updateStudyStatus(studentId, studyStatus, message);
    }
}
