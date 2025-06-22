package learning.center.uz.enums;

import java.util.List;

public enum ProfileRole {
    ROLE_ADMIN, // biz uchun
    ROLE_MODERATOR, // biz uchun
    //
    ROLE_COMPANY_MANAGER,
    ROLE_BRANCH_MANAGER,
    ROLE_TEACHER,
    ROLE_STAFF,
    ROLE_USER;


    public static List<String> companyRoles = List.of(
            ProfileRole.ROLE_COMPANY_MANAGER.toString(),
            ProfileRole.ROLE_BRANCH_MANAGER.toString(),
            ProfileRole.ROLE_TEACHER.toString(),
            ProfileRole.ROLE_STAFF.toString(),
            ProfileRole.ROLE_USER.toString());

    public static List<String> adminRoles = List.of(
            ProfileRole.ROLE_ADMIN.toString(),
            ProfileRole.ROLE_MODERATOR.toString());

}
