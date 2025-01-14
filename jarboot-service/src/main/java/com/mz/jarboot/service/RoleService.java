package com.mz.jarboot.service;

import com.mz.jarboot.common.ResponseForList;
import com.mz.jarboot.entity.RoleInfo;

import java.util.List;

public interface RoleService {

    /**
     * get roles by page.
     *
     * @param pageNo pageNo
     * @param pageSize pageSize
     * @return roles page info
     */
    ResponseForList<RoleInfo> getRoles(int pageNo, int pageSize);

    /**
     * query the user's roles by username.
     *
     * @param username username
     * @param pageNo pageNo
     * @param pageSize pageSize
     * @return roles page info
     */
    ResponseForList<RoleInfo> getRolesByUserName(String username, int pageNo, int pageSize);

    /**
     * assign role to user.
     *
     * @param role role
     * @param userName username
     */
    void addRole(String role, String userName);

    /**
     * delete role.
     *
     * @param role role
     */
    void deleteRole(String role);

    /**
     * delete user's role.
     *
     * @param role role
     * @param username username
     */
    void deleteRole(String role, String username);

    /**
     * fuzzy query roles by role name.
     *
     * @param role role
     * @return roles
     */
    List<String> findRolesLikeRoleName(String role);
}
