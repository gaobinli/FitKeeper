
import { request } from '../request';
import bulidURL from './url'

export function fetchAnnouncementData(data) {
  return request({
    url: bulidURL('/announcement', data),
    method: 'get',
  });
}

export function fetchPostAnnouncement(data) {
  return request({
    url: '/announcement',
    method: 'post',
    data
  });
}

export function fetchPatchAnnouncement(id, data) {
  return request({
    url: '/announcement/' + id,
    method: 'patch',
    data
  });
}

export function fetchDeleteAnnouncement(ids) {
  return request({
    url: '/announcement/' + ids,
    method: 'delete',
  });
}

export function fetchAnnouncementList() {
  return request({
    url: '/announcement/list',
    method: 'get',
  });
}

export function fetchAnnouncementOne(id) {
  return request({
    url: bulidURL('/announcement/' + id),
    method: 'get',
  });
}