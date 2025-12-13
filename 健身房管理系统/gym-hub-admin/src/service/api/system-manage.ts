import { request } from '../request';
import bulidURL from './url'

/** get role list */
// export function fetchGetRoleList(params?: Api.SystemManage.RoleSearchParams) {
//   return request<Api.SystemManage.RoleList>({
//     url: '/systemManage/getRoleList',
//     method: 'get',
//     params
//   });
// }

/**
 * get all roles
 *
 * these roles are all enabled
 */
export function fetchGetAllRoles() {
  return request<Api.SystemManage.AllRole[]>({
    url: '/systemManage/getAllRoles',
    method: 'get'
  });
}

/** get user list */
// export function fetchGetUserList(params?: Api.SystemManage.UserSearchParams) {
//   return request<Api.SystemManage.UserList>({
//     url: '/systemManage/getUserList',
//     method: 'get',
//     params
//   });
// }

/** get menu list */
// export function fetchGetMenuList() {
//   return request<Api.SystemManage.MenuList>({
//     url: '/systemManage/getMenuList/v2',
//     method: 'get'
//   });
// }

export function fetchGetMenuList() {
  return request<Api.SystemManage.MenuList>({
    url: '/sysMenu',
    method: 'get'
  });
}

export function patchMenu(id, data) {
  return request<Api.SystemManage.MenuList>({
    url: '/sysMenu/' + id,
    method: 'patch',
    data
  });
}

export function postMenu(data) {
  return request<Api.SystemManage.MenuList>({
    url: '/sysMenu',
    method: 'post',
    data
  });
}

export function deleteMenu(ids) {
  return request<Api.SystemManage.MenuList>({
    url: '/sysMenu/' + ids,
    method: 'delete',
  });
}

export function fetchAllParentMenu() {
  return request({
    url: '/sysMenu/getParentList',
    method: 'get',
  });
}




export function fetchGetRoleList(params) {
  return request<Api.SystemManage.MenuList>({
    url: bulidURL('/sysRole', params),
    method: 'get',
  });
}

export function fetchPatchRole(id, data) {
  return request<Api.SystemManage.MenuList>({
    url: '/sysRole/' + id,
    method: 'patch',
    data
  });
}

export function fetchPostRole(data) {
  return request<Api.SystemManage.MenuList>({
    url: '/sysRole',
    method: 'post',
    data
  });
}

export function fetchDeleteRole(ids) {
  return request<Api.SystemManage.MenuList>({
    url: '/sysRole/' + ids,
    method: 'delete',
  });
}





export function fetchGetAllRole() {
  return request({
    url: bulidURL('/sysRole/getAllRoleList'),
    method: 'get',
  });
}

export function fetchGetSysUserList(params) {
  return request<Api.SystemManage.MenuList>({
    url: bulidURL('/sysUser', params),
    method: 'get',
  });
}

export function fetchPatchSysUser(id, data) {
  return request<Api.SystemManage.MenuList>({
    url: '/sysUser/' + id,
    method: 'patch',
    data
  });
}

export function fetchPostSysUser(data) {
  return request<Api.SystemManage.MenuList>({
    url: '/sysUser',
    method: 'post',
    data
  });
}

export function fetchDeleteSysUser(ids) {
  return request<Api.SystemManage.MenuList>({
    url: '/sysUser/' + ids,
    method: 'delete',
  });
}





/** get all pages */
export function fetchGetAllPages() {
  return request<string[]>({
    url: '/systemManage/getAllPages',
    method: 'get'
  });
}

/** get menu tree */
export function fetchGetMenuTree() {
  return request<Api.SystemManage.MenuTree[]>({
    url: '/systemManage/getMenuTree',
    method: 'get'
  });
}
