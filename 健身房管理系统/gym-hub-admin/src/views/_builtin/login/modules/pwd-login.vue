<script setup lang="ts">
import { computed, reactive } from 'vue';
import { $t } from '@/locales';
import { loginModuleRecord } from '@/constants/app';
import { useRouterPush } from '@/hooks/common/router';
import { useFormRules, useNaiveForm } from '@/hooks/common/form';
import { useAuthStore } from '@/store/modules/auth';
import { getCode } from '@/service/api'

defineOptions({
  name: 'PwdLogin'
});

const authStore = useAuthStore();
const { toggleLoginModule } = useRouterPush();
const { formRef, validate } = useNaiveForm();

interface FormModel {
  userName: string;
  password: string;
  code: string;
  roleType: number;
}

const model: FormModel = reactive({
  userName: 'admin',
  password: '123456',
  uuid: '',
  code: '1234',
  codeUrl: '',
  roleType: 1
});

const rules = computed<Record<keyof FormModel, App.Global.FormRule[]>>(() => {
  // inside computed to make locale reactive, if not apply i18n, you can define it without computed
  const { formRules, defaultRequiredRule } = useFormRules();

  return {
    // userName: formRules.userName,
    // password: formRules.pwd
    userName: defaultRequiredRule,
    password: defaultRequiredRule,
    code: defaultRequiredRule
  };
});

async function fetchCode() {
  const { error, data } = await getCode();
  console.log('========code====>', data)
  model.uuid = data.uuid
  model.codeUrl = "data:image/gif;base64," + data.img
}

fetchCode()

async function handleSubmit() {
  await validate();
  await authStore.login(model.userName, model.password, model.uuid, model.code, model.roleType);
}

type AccountKey = 'super' | 'admin' | 'user';

interface Account {
  key: AccountKey;
  label: string;
  userName: string;
  password: string;
}

const accounts = computed<Account[]>(() => [
  {
    key: 'super',
    label: $t('page.login.pwdLogin.superAdmin'),
    userName: 'Super',
    password: '123456'
  },
  {
    key: 'admin',
    label: $t('page.login.pwdLogin.admin'),
    userName: 'Admin',
    password: '123456'
  },
  {
    key: 'user',
    label: $t('page.login.pwdLogin.user'),
    userName: 'User',
    password: '123456'
  }
]);

async function handleAccountLogin(account: Account) {
  await authStore.login(account.userName, account.password);
}
</script>

<template>
  <NForm ref="formRef" :model="model" :rules="rules" size="large" :show-label="false" @keyup.enter="handleSubmit">
    <NFormItem path="userName">
      <NInput v-model:value="model.userName" :placeholder="$t('page.login.common.userNamePlaceholder')" />
    </NFormItem>
    <NFormItem path="password">
      <NInput
        v-model:value="model.password"
        type="password"
        show-password-on="click"
        :placeholder="$t('page.login.common.passwordPlaceholder')"
      />
    </NFormItem>
    <NFormItem path="code">
      <div class="w-full flex-y-center gap-16px">

        <NInput v-model:value="model.code" :placeholder="$t('page.login.common.codePlaceholder')" />
        <!-- <NButton size="large" :disabled="isCounting" :loading="loading" @click="fetchCode()"> -->
          <img :src="model.codeUrl" style="cursor: pointer;" @click="fetchCode()" class="login-code-img"/>

        <!-- </NButton> -->
      </div>
    </NFormItem>
    <NFormItem label="登录角色" path="roleType">
      <NRadioGroup v-model:value="model.roleType">
        <NSpace>
          <NRadio :value="1">管理员</NRadio>
          <NRadio :value="2">教练</NRadio>
        </NSpace>
      </NRadioGroup>
    </NFormItem>
    <NSpace vertical :size="24">
      <!-- <div class="flex-y-center justify-between">
        <NCheckbox>{{ $t('page.login.pwdLogin.rememberMe') }}</NCheckbox>
        <NButton quaternary @click="toggleLoginModule('reset-pwd')">
          {{ $t('page.login.pwdLogin.forgetPassword') }}
        </NButton>
      </div> -->
      <div class="w-full">
        <NButton type="primary" size="large" round block :loading="authStore.loginLoading" @click="handleSubmit">
          {{ $t('common.confirm') }}
        </NButton>
      </div>
      <div class="text-right">
        <NButton type="text" size="small" @click="toggleLoginModule('coach-register')">
          教练注册
        </NButton>
      </div>
      <!-- <div class="flex-y-center justify-between gap-12px">
        <NButton class="flex-1" block @click="toggleLoginModule('code-login')">
          {{ $t(loginModuleRecord['code-login']) }}
        </NButton>
        <NButton class="flex-1" block @click="toggleLoginModule('register')">
          {{ $t(loginModuleRecord.register) }}
        </NButton>
      </div> -->
      <!-- <NDivider class="text-14px text-#666 !m-0">{{ $t('page.login.pwdLogin.otherAccountLogin') }}</NDivider> -->
      <!-- <div class="flex-center gap-12px">
        <NButton v-for="item in accounts" :key="item.key" type="primary" @click="handleAccountLogin(item)">
          {{ item.label }}
        </NButton>
      </div> -->
    </NSpace>
  </NForm>
</template>

<style scoped></style>
