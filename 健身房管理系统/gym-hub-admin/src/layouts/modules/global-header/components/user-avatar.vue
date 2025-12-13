<script setup lang="ts">
import { computed, ref, watch } from 'vue';
import type { VNode } from 'vue';
import { useAuthStore } from '@/store/modules/auth';
import { useRouterPush } from '@/hooks/common/router';
import { useSvgIcon } from '@/hooks/common/icon';
import { $t } from '@/locales';
defineOptions({
  name: 'UserAvatar'
});
import { fetchGetSysUserInfo, fetchPatchSysUser, fetchPatchCoach, fetchCoachOne } from '@/service/api';
import { useFormRules, useNaiveForm } from '@/hooks/common/form';
import WangEditor from '@/components/custom/WangEditor.vue';
import dayjs from 'dayjs';

const authStore = useAuthStore();
const { routerPushByKey, toLogin } = useRouterPush();
const { SvgIconVNode } = useSvgIcon();

const { defaultRequiredRule, patternRules } = useFormRules();

const uploadUrl = import.meta.env.VITE_SERVICE_BASE_URL + "/upload"
const previewFileList = ref([])

function loginOrRegister() {
  toLogin();
}

function handleFileListUpdate(files) {
  if (files && files.event && files.event?.target && files.event.target.response) {
    userInfo.value.url = JSON.parse(files.event.target.response).data
  }

  if(userInfo.value.url) {
    previewFileList.value = [
        {
          id: '1',
          name: '我是react.png',
          status: 'finished',
          url: userInfo.value.url
        },
    ]
  }
}

const getWangEditorValue = (str: string) => {
  userInfo.value.introduce_content = str
};

type DropdownKey = 'user-center' | 'logout';

type DropdownOption =
  | {
      key: DropdownKey;
      label: string;
      icon?: () => VNode;
    }
  | {
      type: 'divider';
      key: string;
    };

const options = computed(() => {
  const opts: DropdownOption[] = [
    {
      label: $t('common.userCenter'),
      key: 'user-center',
      icon: SvgIconVNode({ icon: 'ph:user-circle', fontSize: 18 })
    },
    {
      type: 'divider',
      key: 'divider'
    },
    {
      label: $t('common.logout'),
      key: 'logout',
      icon: SvgIconVNode({ icon: 'ph:sign-out', fontSize: 18 })
    }
  ];

  return opts;
});

function logout() {
  window.$dialog?.info({
    title: $t('common.tip'),
    content: $t('common.logoutConfirm'),
    positiveText: $t('common.confirm'),
    negativeText: $t('common.cancel'),
    onPositiveClick: () => {
      authStore.resetStore();
    }
  });
}

async function handleDropdown(key: DropdownKey) {
  if (key === 'logout') {
    logout();
  } else {
    // Get user info based on role
    let data;
    if (authStore.userInfo.role === 'other') {
      // Coach: use coach API with userId
      const result = await fetchCoachOne(authStore.userInfo.userId);
      data = result.data;
    } else {
      // Admin: use sysUser API
      const result = await fetchGetSysUserInfo();
      data = result.data;
    }

    if (data) {
      userInfo.value = data;
      visible.value = true;
    } else {
      window.$message?.error('获取用户信息失败');
    }
  }
}
const userInfo = ref(createDefaultModel());

const visible = ref(false)

function createDefaultModel(): Model {
  return {
      id: null,
      username: null,
      name: null,
      phone: null,
      password: null,
      birthday: null,
      address: null,
      sex: null,
      introduce_content: null,
      url: null,
  };
}

type RuleKey = Extract<keyof Model,
'undefind'
// | 'id'
| 'username'
| 'name'
| 'phone'
>;

const rules: Record<RuleKey, App.Global.FormRule> = {
  // 'id' : defaultRequiredRule,
  'username' : defaultRequiredRule,
  'name' : defaultRequiredRule,
  'phone' : [defaultRequiredRule, patternRules.phone],
};
const { formRef, validate, restoreValidation } = useNaiveForm();
async function handleSubmit() {
  await validate();
  // request
  if (userInfo.value.id) {
    let error, data;

    if (authStore.userInfo.role === 'other') {
      // Coach: use coach API (PATCH /coach/{id})
      // For coach, only send editable fields, exclude password if not changed
      const submitData = {
        name: userInfo.value.name,
        phone: userInfo.value.phone,
        birthday: userInfo.value.birthday ? dayjs(userInfo.value.birthday).format('YYYY-MM-DD HH:mm:ss') : null,
        address: userInfo.value.address,
        sex: userInfo.value.sex,
        introduce_content: userInfo.value.introduce_content,
        url: userInfo.value.url,
      };
      // Only include password if it's provided
      if (userInfo.value.password) {
        submitData.password = userInfo.value.password;
      }
      const result = await fetchPatchCoach(userInfo.value.id, submitData);
      error = result.error;
      data = result.data;
    } else {
      // Admin: use sysUser API
      userInfo.value.type = 1;
      const result = await fetchPatchSysUser(userInfo.value.id, userInfo.value);
      error = result.error;
      data = result.data;
    }

    if (error) {
      return
    }
    authStore.userInfo.userName = userInfo.value.name
    visible.value = false
    if (userInfo.value.password) {
      window.$message?.success("密码修改成功，请重新登录");
      setTimeout(function() {
          authStore.resetStore();
      }, 1000);
      return
    }
    window.$message?.success($t('common.updateSuccess'));
  }

}

watch(visible, (newVal) => {
  if (newVal && userInfo.value.url) {
    // Initialize previewFileList when modal opens and there's an existing image
    previewFileList.value = [
      {
        id: '1',
        name: '我是react.png',
        status: 'finished',
        url: userInfo.value.url
      },
    ]
  } else if (!newVal) {
    // Clear previewFileList when modal closes
    previewFileList.value = []
  }
});
</script>

<template>
<!-- {{authStore.isLogin}}  {{authStore.userInfo.userName}} -->
  <!-- <NButton v-if="!authStore.isLogin" quaternary @click="loginOrRegister">
    {{ $t('page.login.common.loginOrRegister') }}
  </NButton> -->
  <!-- <NDropdown v-else placement="bottom" trigger="click" :options="options" @select="handleDropdown"> -->
  <NDropdown placement="bottom" trigger="click" :options="options" @select="handleDropdown">
    <div>
      <ButtonIcon>
        <SvgIcon icon="ph:user-circle" class="text-icon-large" />
        <span class="text-16px font-medium">{{ authStore.userInfo.userName }}</span>
      </ButtonIcon>
    </div>
  </NDropdown>
  <NModal v-model:show="visible" :title="authStore.userInfo.role === 'other' ? '教练信息' : '管理员信息'" preset="card" class="w-800px">
    <NScrollbar class="h-480px pr-20px">
      <NForm ref="formRef" :model="userInfo" :rules="rules" label-placement="left" :label-width="100">
        <NGrid responsive="screen" item-responsive>
          <NFormItemGi span="24 m:24" label="账号：" path="username">
            <NInput v-model:value="userInfo.username" disabled placeholder="请输入账号" />
          </NFormItemGi>
          <NFormItemGi span="24 m:24" label="姓名：" path="name">
            <NInput v-model:value="userInfo.name" placeholder="请输入姓名" />
          </NFormItemGi>
          <NFormItemGi span="24 m:24" label="手机号：" path="phone">
            <NInput v-model:value="userInfo.phone" placeholder="请输入手机号" />
          </NFormItemGi>
          <!-- Coach-specific fields -->
          <NFormItemGi v-if="authStore.userInfo.role === 'other'" span="24 m:24" label="出生日期：" path="birthday">
            <NDatePicker v-model:value="userInfo.birthday" type="datetime" clearable />
          </NFormItemGi>
          <NFormItemGi v-if="authStore.userInfo.role === 'other'" span="24 m:24" label="地址：" path="address">
            <NInput v-model:value="userInfo.address" placeholder="请输入地址" />
          </NFormItemGi>
          <NFormItemGi v-if="authStore.userInfo.role === 'other'" span="24 m:24" label="性别：" path="sex">
            <NSelect
              v-model:value="userInfo.sex"
              :options="[
                { label: '男', value: 1 },
                { label: '女', value: 2 }
              ]"
              placeholder="请选择性别"
            />
          </NFormItemGi>
          <!-- Password field for coach users (after sex field) -->
          <NFormItemGi v-if="authStore.userInfo.role === 'other'" span="24 m:24" label="密码：" path="password">
            <NInput v-model:value="userInfo.password" type="password" placeholder="留空表示不修改密码" />
          </NFormItemGi>
          <NFormItemGi v-if="authStore.userInfo.role === 'other'" span="24 m:24" label="宣传照：" path="url">
            <NUpload
              :action="uploadUrl"
              :default-file-list="previewFileList"
              list-type="image-card"
              :max="1"
              @change="handleFileListUpdate"
            />
          </NFormItemGi>
          <NFormItemGi v-if="authStore.userInfo.role === 'other'" span="24 m:24" label="介绍：" path="introduce_content">
            <wang-editor :contentHtml="userInfo.introduce_content" v-on:getWangEditorValue="getWangEditorValue"></wang-editor>
          </NFormItemGi>
          <!-- Password field for admin users (at the end) -->
          <NFormItemGi v-if="authStore.userInfo.role !== 'other'" span="24 m:24" label="密码：" path="password">
            <NInput v-model:value="userInfo.password" type="password" placeholder="留空表示不修改密码" />
          </NFormItemGi>
        </NGrid>
      </NForm>
    </NScrollbar>
    <template #footer>
      <NSpace justify="end" :size="16">
        <NButton @click="visible = false">{{ $t('common.cancel') }}</NButton>
        <NButton type="primary" @click="handleSubmit">{{ $t('common.confirm') }}</NButton>
      </NSpace>
    </template>
  </NModal>
</template>

<style scoped></style>
