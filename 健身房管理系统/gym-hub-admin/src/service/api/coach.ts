
import { request } from '../request';
import bulidURL from './url'

export function fetchCoachData(data) {
  return request({
    url: bulidURL('/coach', data),
    method: 'get',
  });
}

export function fetchPostCoach(data) {
  return request({
    url: '/coach',
    method: 'post',
    data
  });
}

export function fetchPatchCoach(id, data) {
  return request({
    url: '/coach/' + id,
    method: 'patch',
    data
  });
}

export function fetchDeleteCoach(ids) {
  return request({
    url: '/coach/' + ids,
    method: 'delete',
  });
}

export function fetchCoachList() {
  return request({
    url: '/coach/list',
    method: 'get',
  });
}

export function fetchCoachOne(id) {
  return request({
    url: bulidURL('/coach/' + id),
    method: 'get',
  });
}