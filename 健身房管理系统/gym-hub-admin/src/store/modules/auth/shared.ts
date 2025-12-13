import { localStg } from '@/utils/storage';

/** Get token */
export function getToken() {
  return localStg.get('token') || '';
}

/** Get user role */
export function getUserRole() {
  return localStg.get('userRole') || '';
}

/** Set user role */
export function setUserRole(role: string) {
  localStg.set('userRole', role);
}

/** Get user ID */
export function getUserId() {
  return localStg.get('userId') || '';
}

/** Set user ID */
export function setUserId(userId: string) {
  localStg.set('userId', userId);
}

/** Clear auth storage */
export function clearAuthStorage() {
  localStg.remove('token');
  localStg.remove('refreshToken');
  localStg.remove('userRole');
  localStg.remove('userId');
}
