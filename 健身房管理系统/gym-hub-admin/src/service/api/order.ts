
import { request } from '../request';
import bulidURL from './url'

export function fetchOrderData(data) {
  return request({
    url: bulidURL('/order', data),
    method: 'get',
  });
}

export function fetchPostOrder(data) {
  return request({
    url: '/order',
    method: 'post',
    data
  });
}

export function fetchPatchOrder(id, data) {
  return request({
    url: '/order/' + id,
    method: 'patch',
    data
  });
}

export function fetchDeleteOrder(ids) {
  return request({
    url: '/order/' + ids,
    method: 'delete',
  });
}

export function fetchOrderList() {
  return request({
    url: '/order/list',
    method: 'get',
  });
}

export function fetchOrderOne(id) {
  return request({
    url: bulidURL('/order/' + id),
    method: 'get',
  });
}

export function postOrderSend(id) {
  return request({
    url: '/order/send?id= ' + id,
    method: 'post',
  });
}