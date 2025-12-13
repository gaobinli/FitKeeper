import { request } from '../request';

/**
 * Login
 *
 * @param userName User name
 * @param password Password
 */
export function fetchLogin(userName: string, password: string) {
  return request<Api.Auth.LoginToken>({
    url: '/auth/login',
    method: 'post',
    data: {
      userName,
      password
    }
  });
}

export function Login(username: string, password: string, uuid: string, code: string, type: number) {
  return request<Api.Auth.LoginToken>({
    // url: '/auth/login',
    url: '/admin/login',
    method: 'post',
    data: {
      username,
      password,
      uuid,
      code,
      type
    }
  });
}

export function getCode() {
  return request({
    // url: '/auth/login',
    url: '/captchaImage',
    method: 'get',
  })
}

/**
 * Register coach
 *
 * @param data Coach registration data
 */
export function registerCoach(data: any) {
  return request({
    url: '/coach',
    method: 'post',
    data
  });
}

/** Get user info */
// export function fetchGetUserInfo() {
//   return request<Api.Auth.UserInfo>({ url: '/auth/getUserInfo' });
// }
export function fetchGetSysUserInfo() {
  return request<Api.Auth.UserInfo>({ url: '/sysUser/info' });
}

/** Get coach info */
export function fetchGetCoachInfo() {
  return request<Api.Auth.UserInfo>({ url: '/coach/info' });
}


/**
 * Refresh token
 *
 * @param refreshToken Refresh token
 */
export function fetchRefreshToken(refreshToken: string) {
  return request<Api.Auth.LoginToken>({
    url: '/auth/refreshToken',
    method: 'post',
    data: {
      refreshToken
    }
  });
}

/**
 * return custom backend error
 *
 * @param code error code
 * @param msg error message
 */
export function fetchCustomBackendError(code: string, msg: string) {
  return request({ url: '/auth/error', params: { code, msg } });
}
