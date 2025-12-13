
import { request } from '../request';
import bulidURL from './url'

export function fetchPostCommentData(data) {
  return request({
    url: bulidURL('/postComment', data),
    method: 'get',
  });
}

export function fetchPostPostComment(data) {
  return request({
    url: '/postComment',
    method: 'post',
    data
  });
}

export function fetchPatchPostComment(id, data) {
  return request({
    url: '/postComment/' + id,
    method: 'patch',
    data
  });
}

export function fetchDeletePostComment(ids) {
  return request({
    url: '/postComment/' + ids,
    method: 'delete',
  });
}

export function fetchPostCommentList() {
  return request({
    url: '/postComment/list',
    method: 'get',
  });
}

export function fetchPostCommentOne(id) {
  return request({
    url: bulidURL('/postComment/' + id),
    method: 'get',
  });
}