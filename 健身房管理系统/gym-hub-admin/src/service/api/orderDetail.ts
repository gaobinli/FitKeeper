
import { request } from '../request';
import bulidURL from './url'

export function fetchOrderDetailData(data) {
  return request({
    url: bulidURL('/orderDetail', data),
    method: 'get',
  });
}

export function fetchPostOrderDetail(data) {
  return request({
    url: '/orderDetail',
    method: 'post',
    data
  });
}

export function fetchPatchOrderDetail(id, data) {
  return request({
    url: '/orderDetail/' + id,
    method: 'patch',
    data
  });
}

export function fetchDeleteOrderDetail(ids) {
  return request({
    url: '/orderDetail/' + ids,
    method: 'delete',
  });
}

export function fetchOrderDetailList() {
  return request({
    url: '/orderDetail/list',
    method: 'get',
  });
}

export function fetchOrderDetailOne(id) {
  return request({
    url: bulidURL('/orderDetail/' + id),
    method: 'get',
  });
}

export function fetchOrderDetailList2(data) {
  return request({
    url: bulidURL('/orderDetail/list?orderNo=' + data),
    method: 'get',
  });
}