import { computed, reactive, ref } from 'vue';
import { useRoute } from 'vue-router';
import { defineStore } from 'pinia';
import { useLoading } from '@sa/hooks';
import { SetupStoreId } from '@/enum';
import { useRouterPush } from '@/hooks/common/router';
import { fetchGetSysUserInfo, fetchGetCoachInfo, fetchLogin, Login } from '@/service/api';
import { localStg } from '@/utils/storage';
import { $t } from '@/locales';
import { useRouteStore } from '../route';
import { useTabStore } from '../tab';
import { clearAuthStorage, getToken, getUserRole, setUserRole, getUserId, setUserId } from './shared';
import { randomUUID } from 'node:crypto';

export const useAuthStore = defineStore(SetupStoreId.Auth, () => {
  const route = useRoute();
  const routeStore = useRouteStore();
  const tabStore = useTabStore();
  const { toLogin, redirectFromLogin, toHome } = useRouterPush(false);
  const { loading: loginLoading, startLoading, endLoading } = useLoading();

  const token = ref(getToken());

  const userInfo: Api.Auth.UserInfo = reactive({
    userId: getUserId(),
    userName: '',
    roles: [],
    buttons: [],
    role: (getUserRole() as any) || undefined
  });

  // Store login type (1: admin, 2: coach)
  const loginType = ref<number | null>(null);

  /** is super role in static route */
  const isStaticSuper = computed(() => {
    const { VITE_AUTH_ROUTE_MODE, VITE_STATIC_SUPER_ROLE } = import.meta.env;

    return VITE_AUTH_ROUTE_MODE === 'static' && userInfo.roles.includes(VITE_STATIC_SUPER_ROLE);
  });

  /** Is login */
  const isLogin = computed(() => Boolean(token.value));

  /** Reset auth store */
  async function resetStore() {
    const authStore = useAuthStore();

    clearAuthStorage();

    authStore.$reset();

    if (!route.meta.constant) {
      await toLogin();
    }

    tabStore.cacheTabs();
    routeStore.resetStore();
  }

  /**
   * Login
   *
   * @param userName User name
   * @param password Password
   * @param uuid Captcha uuid
   * @param code Captcha code
   * @param type Login type (1: admin, 2: coach)
   * @param [redirect=true] Whether to redirect after login. Default is `true`
   */
  async function login(userName: string, password: string,  uuid: string, code: string, type: number, redirect = true) {
    startLoading();

    // const { data: loginToken, error } = await fetchLogin(userName, password);
    const { data: loginToken, error } = await Login(userName, password, uuid, code, type);

    if (!error) {
      // Store login type before getting user info
      loginType.value = type;
      const pass = await loginByToken(loginToken);

      if (pass) {
        await routeStore.initAuthRoute();

        toHome()
        // await redirectFromLogin(redirect);

        if (routeStore.isInitAuthRoute) {
          window.$notification?.success({
            title: $t('page.login.common.loginSuccess'),
            content: $t('page.login.common.welcomeBack', { userName: userInfo.userName }),
            duration: 4500
          });
        }
      }
    } else {
      resetStore();
    }

    endLoading();
  }

  async function loginByToken(loginToken: Api.Auth.LoginToken) {
    // 1. stored in the localStorage, the later requests need it in headers
    localStg.set('token', loginToken);
    // localStg.set('refreshToken', loginToken.refreshToken);

    // 2. get user info
    const pass = await getUserInfo();

    if (pass) {
      token.value = loginToken.token;

      return true;
    }

    return false;
  }

  async function getUserInfo() {
    // Call different endpoints based on login type
    let apiCall;
    let roleValue: string;

    if (loginType.value === 2) {
      // Coach login - call /coach/info
      apiCall = fetchGetCoachInfo();
      roleValue = 'other';
    } else {
      // Admin - call /sysUser/info
      apiCall = fetchGetSysUserInfo();
      roleValue = loginType.value === 1 ? 'admin' : 'other';
    }

    const { data: info, error } = await apiCall;

    if (!error) {
      // update store
      const userId = info.id || info.userId;
      const userInfo2 = {
        userId: userId,
        userName: info.name || info.userName,
        role: roleValue
      }

      Object.assign(userInfo, userInfo2);

      // 永久保存角色和用户ID到本地存储
      setUserRole(roleValue);
      setUserId(userId);

      return true;
    }

    return false;
  }

  async function initUserInfo() {
    const hasToken = getToken();

    if (hasToken) {
      // 从本地存储恢复角色和用户ID
      const savedRole = getUserRole();
      const savedUserId = getUserId();
      if (savedRole) {
        userInfo.role = savedRole as any;
      }
      if (savedUserId) {
        userInfo.userId = savedUserId;
      }

      const pass = await getUserInfo();

      if (!pass) {
        resetStore();
      }
    }
  }

  return {
    token,
    userInfo,
    loginType,
    isStaticSuper,
    isLogin,
    loginLoading,
    resetStore,
    login,
    initUserInfo
  };
});
