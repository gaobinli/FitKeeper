
import { request } from '../request';
import bulidURL from './url'

export function fetchCourseReserveData(data) {
  return request({
    url: bulidURL('/courseReserve', data),
    method: 'get',
  });
}

export function fetchPostCourseReserve(data) {
  return request({
    url: '/courseReserve',
    method: 'post',
    data
  });
}

export function fetchPatchCourseReserve(id, data) {
  return request({
    url: '/courseReserve/' + id,
    method: 'patch',
    data
  });
}

export function fetchDeleteCourseReserve(ids) {
  return request({
    url: '/courseReserve/' + ids,
    method: 'delete',
  });
}

export function fetchCourseReserveList() {
  return request({
    url: '/courseReserve/list',
    method: 'get',
  });
}

export function fetchCourseReserveOne(id) {
  return request({
    url: bulidURL('/courseReserve/' + id),
    method: 'get',
  });
}