
import { request } from '../request';
import bulidURL from './url'

export function fetchClassificationData(data) {
  return request({
    url: bulidURL('/classification', data),
    method: 'get',
  });
}

export function fetchPostClassification(data) {
  return request({
    url: '/classification',
    method: 'post',
    data
  });
}

export function fetchPatchClassification(id, data) {
  return request({
    url: '/classification/' + id,
    method: 'patch',
    data
  });
}

export function fetchDeleteClassification(ids) {
  return request({
    url: '/classification/' + ids,
    method: 'delete',
  });
}

export function fetchClassificationList() {
  return request({
    url: '/classification/list',
    method: 'get',
  });
}

export function fetchClassificationOne(id) {
  return request({
    url: bulidURL('/classification/' + id),
    method: 'get',
  });
}