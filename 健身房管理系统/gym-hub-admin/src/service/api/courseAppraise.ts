
import { request } from '../request';
import bulidURL from './url'

export function fetchCourseAppraiseData(data) {
  return request({
    url: bulidURL('/courseAppraise', data),
    method: 'get',
  });
}

export function fetchPostCourseAppraise(data) {
  return request({
    url: '/courseAppraise',
    method: 'post',
    data
  });
}

export function fetchPatchCourseAppraise(id, data) {
  return request({
    url: '/courseAppraise/' + id,
    method: 'patch',
    data
  });
}

export function fetchDeleteCourseAppraise(ids) {
  return request({
    url: '/courseAppraise/' + ids,
    method: 'delete',
  });
}

export function fetchCourseAppraiseList() {
  return request({
    url: '/courseAppraise/list',
    method: 'get',
  });
}

export function fetchCourseAppraiseOne(id) {
  return request({
    url: bulidURL('/courseAppraise/' + id),
    method: 'get',
  });
}