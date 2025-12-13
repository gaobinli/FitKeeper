
import { request } from '../request';
import bulidURL from './url'

export function fetchCarouselData(data) {
  return request({
    url: bulidURL('/carousel', data),
    method: 'get',
  });
}

export function fetchPostCarousel(data) {
  return request({
    url: '/carousel',
    method: 'post',
    data
  });
}

export function fetchPatchCarousel(id, data) {
  return request({
    url: '/carousel/' + id,
    method: 'patch',
    data
  });
}

export function fetchDeleteCarousel(ids) {
  return request({
    url: '/carousel/' + ids,
    method: 'delete',
  });
}

export function fetchCarouselList() {
  return request({
    url: '/carousel/list',
    method: 'get',
  });
}

export function fetchCarouselOne(id) {
  return request({
    url: bulidURL('/carousel/' + id),
    method: 'get',
  });
}