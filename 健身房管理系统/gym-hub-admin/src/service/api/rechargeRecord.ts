
import { request } from '../request';
import bulidURL from './url'

export function fetchRechargeRecordData(data) {
  return request({
    url: bulidURL('/rechargeRecord', data),
    method: 'get',
  });
}

export function fetchPostRechargeRecord(data) {
  return request({
    url: '/rechargeRecord',
    method: 'post',
    data
  });
}

export function fetchPatchRechargeRecord(id, data) {
  return request({
    url: '/rechargeRecord/' + id,
    method: 'patch',
    data
  });
}

export function fetchDeleteRechargeRecord(ids) {
  return request({
    url: '/rechargeRecord/' + ids,
    method: 'delete',
  });
}

export function fetchRechargeRecordList() {
  return request({
    url: '/rechargeRecord/list',
    method: 'get',
  });
}

export function fetchRechargeRecordOne(id) {
  return request({
    url: bulidURL('/rechargeRecord/' + id),
    method: 'get',
  });
}