
import { request } from '../request';
import bulidURL from './url'

export function fetchUserData(data) {
  return request({
    url: bulidURL('/user', data),
    method: 'get',
  });
}

export function fetchPostUser(data) {
  return request({
    url: '/user',
    method: 'post',
    data
  });
}

export function fetchPatchUser(id, data) {
  return request({
    url: '/user/' + id,
    method: 'patch',
    data
  });
}

export function fetchDeleteUser(ids) {
  return request({
    url: '/user/' + ids,
    method: 'delete',
  });
}

export function fetchUserList() {
  return request({
    url: '/user/list',
    method: 'get',
  });
}

export function fetchUserOne(id) {
  return request({
    url: bulidURL('/user/' + id),
    method: 'get',
  });
}

export function fetchGetUserList(id) {
  return request({
    url: bulidURL('/user/' + id),
    method: 'get',
  });
}