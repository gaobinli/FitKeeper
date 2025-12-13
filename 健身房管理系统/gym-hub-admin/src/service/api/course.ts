
import { request } from '../request';
import bulidURL from './url'

export function fetchCourseData(data) {
  return request({
    url: bulidURL('/course', data),
    method: 'get',
  });
}

export function fetchPostCourse(data) {
  return request({
    url: '/course',
    method: 'post',
    data
  });
}

export function fetchPatchCourse(id, data) {
  return request({
    url: '/course/' + id,
    method: 'patch',
    data
  });
}

export function fetchDeleteCourse(ids) {
  return request({
    url: '/course/' + ids,
    method: 'delete',
  });
}

export function fetchCourseList() {
  return request({
    url: '/course/list',
    method: 'get',
  });
}

export function fetchCourseOne(id) {
  return request({
    url: bulidURL('/course/' + id),
    method: 'get',
  });
}