
import { request } from '../request';
import bulidURL from './url'

export function fetchSkuAppraiseData(data) {
  return request({
    url: bulidURL('/skuAppraise', data),
    method: 'get',
  });
}

export function fetchPostSkuAppraise(data) {
  return request({
    url: '/skuAppraise',
    method: 'post',
    data
  });
}

export function fetchPatchSkuAppraise(id, data) {
  return request({
    url: '/skuAppraise/' + id,
    method: 'patch',
    data
  });
}

export function fetchDeleteSkuAppraise(ids) {
  return request({
    url: '/skuAppraise/' + ids,
    method: 'delete',
  });
}

export function fetchSkuAppraiseList() {
  return request({
    url: '/skuAppraise/list',
    method: 'get',
  });
}

export function fetchSkuAppraiseOne(id) {
  return request({
    url: bulidURL('/skuAppraise/' + id),
    method: 'get',
  });
}