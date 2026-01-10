package com.project.iam.enumerations;

public enum AuditAction {
    LOGIN_SUCCESS,
    LOGIN_FAILURE,
    LOGOUT,

    ROLE_CREATE,
    ROLE_UPDATE,
    ROLE_DELETE,

    USER_CREATE,
    USER_UPDATE,
    USER_DELETE,

    PERMISSION_CREATE,
    PERMISSION_UPDATE,
    PERMISSION_DELETE,

    TOKEN_REFRESH
}
