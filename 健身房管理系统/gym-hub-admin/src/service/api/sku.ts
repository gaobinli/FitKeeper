
import { request } from '../request';
import bulidURL from './url'

export function fetchSkuData(data) {
  return request({
    url: bulidURL('/sku', data),
    method: 'get',
  });
}

export function fetchPostSku(data) {
  return request({
    url: '/sku',
    method: 'post',
    data
  });
}

export function fetchPatchSku(id, data) {
  return request({
    url: '/sku/' + id,
    method: 'patch',
    data
  });
}

export function fetchDeleteSku(ids) {
  return request({
    url: '/sku/' + ids,
    method: 'delete',
  });
}

export function fetchSkuList() {
  return request({
    url: '/sku/list',
    method: 'get',
  });
}

export function fetchSkuOne(id) {
  return request({
    url: bulidURL('/sku/' + id),
    method: 'get',
  });
}