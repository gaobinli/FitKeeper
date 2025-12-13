<script setup lang="ts">
import { computed, reactive } from 'vue';
import { $t } from '@/locales';
import { useRouterPush } from '@/hooks/common/router';
import { useFormRules, useNaiveForm } from '@/hooks/common/form';
import { registerCoach } from '@/service/api';

defineOptions({
  name: 'CoachRegister'
});

const { toggleLoginModule } = useRouterPush();
const { formRef, validate } = useNaiveForm();

interface FormModel {
  name: string;
  username: string;
  password: string;
  confirmPassword: string;
  phone: string;
}

const model: FormModel = reactive({
  name: '',
  username: '',
  password: '',
  confirmPassword: '',
  phone: ''
});

const rules = computed<Record<keyof FormModel, App.Global.FormRule[]>>(() => {
  const { formRules, createConfirmPwdRule, defaultRequiredRule } = useFormRules();

  return {
    name: defaultRequiredRule,
    username: defaultRequiredRule,
    password: formRules.pwd,
    confirmPassword: createConfirmPwdRule(model.password),
    phone: formRules.phone
  };
});

const loading = reactive({
  submit: false
});

async function handleSubmit() {
  await validate();

  loading.submit = true;

  try {
    const submitData = {
      name: model.name,
      username: model.username,
      password: model.password,
      phone: model.phone,
      enable: 0
    };

    const { error } = await registerCoach(submitData);

    if (!error) {
      window.$message?.success('注册成功，请返回登录');
      toggleLoginModule('pwd-login');
    }
  } finally {
    loading.submit = false;
  }
}
</script>

<template>
  <NForm ref="formRef" :model="model" :rules="rules" size="large" :show-label="false" @keyup.enter="handleSubmit">
    <NFormItem path="name">
      <NInput v-model:value="model.name" placeholder="姓名" />
    </NFormItem>
    <NFormItem path="username">
      <NInput v-model:value="model.username" placeholder="账号" />
    </NFormItem>
    <NFormItem path="password">
      <NInput
        v-model:value="model.password"
        type="password"
        show-password-on="click"
        placeholder="密码"
      />
    </NFormItem>
    <NFormItem path="confirmPassword">
      <NInput
        v-model:value="model.confirmPassword"
        type="password"
        show-password-on="click"
        placeholder="确认密码"
      />
    </NFormItem>
    <NFormItem path="phone">
      <NInput v-model:value="model.phone" placeholder="手机号" />
    </NFormItem>
    <NSpace vertical :size="18" class="w-full">
      <NButton type="primary" size="large" round block :loading="loading.submit" @click="handleSubmit">
        {{ $t('common.confirm') }}
      </NButton>
      <NButton size="large" round block @click="toggleLoginModule('pwd-login')">
        {{ $t('page.login.common.back') }}
      </NButton>
    </NSpace>
  </NForm>
</template>

<style scoped></style>
