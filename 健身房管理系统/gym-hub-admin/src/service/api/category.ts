
import { request } from '../request';
import bulidURL from './url'

export function fetchCategoryData(data) {
  return request({
    url: bulidURL('/category', data),
    method: 'get',
  });
}

export function fetchPostCategory(data) {
  return request({
    url: '/category',
    method: 'post',
    data
  });
}

export function fetchPatchCategory(id, data) {
  return request({
    url: '/category/' + id,
    method: 'patch',
    data
  });
}

export function fetchDeleteCategory(ids) {
  return request({
    url: '/category/' + ids,
    method: 'delete',
  });
}

export function fetchCategoryList() {
  return request({
    url: '/category/list',
    method: 'get',
  });
}

export function fetchCategoryOne(id) {
  return request({
    url: bulidURL('/category/' + id),
    method: 'get',
  });
}