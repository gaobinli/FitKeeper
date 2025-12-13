
import { request } from '../request';
import bulidURL from './url'

export function fetchPostData(data) {
  return request({
    url: bulidURL('/post', data),
    method: 'get',
  });
}

export function fetchPostPost(data) {
  return request({
    url: '/post',
    method: 'post',
    data
  });
}

export function fetchPatchPost(id, data) {
  return request({
    url: '/post/' + id,
    method: 'patch',
    data
  });
}

export function fetchDeletePost(ids) {
  return request({
    url: '/post/' + ids,
    method: 'delete',
  });
}

export function fetchPostList() {
  return request({
    url: '/post/list',
    method: 'get',
  });
}

export function fetchPostOne(id) {
  return request({
    url: bulidURL('/post/' + id),
    method: 'get',
  });
}