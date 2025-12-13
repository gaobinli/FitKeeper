
<script setup lang="ts">
import { computed, ref, watch } from 'vue';
import { $t } from '@/locales';
import { useFormRules, useNaiveForm } from '@/hooks/common/form';
import { translateOptions } from '@/utils/common';
import { useAuthStore } from '@/store/modules/auth';
import {
} from '@/service/api';

defineOptions({
  name: 'CoachSearch'
});

interface Emits {
  (e: 'reset'): void;
  (e: 'search'): void;
}

const authStore = useAuthStore();

const emit = defineEmits<Emits>();

const { formRef, validate, restoreValidation } = useNaiveForm();

const model = defineModel('model', { required: true });

type RuleKey = Extract<keyof Api.SystemManage.UserSearchParams, 'userEmail' | 'userPhone'>;

const rules = computed<Record<RuleKey, App.Global.FormRule>>(() => {
  const { patternRules } = useFormRules(); // inside computed to make locale reactive

  return {
    userEmail: patternRules.email,
    userPhone: patternRules.phone
  };
});

async function reset() {
  await restoreValidation();
  emit('reset');
}

async function search() {
  await validate();
  emit('search');
}


function init() {
}

init()
</script>

<template>
  <NCard :bordered="false" size="small" class="card-wrapper">
    <NCollapse :default-expanded-names="['coach-search']">
      <NCollapseItem title="搜索" name="coach-search">
        <NForm ref="formRef" :model="model" :rules="rules" label-placement="left" :label-width="80">
          <NGrid responsive="screen" item-responsive>
    <NFormItemGi span="24 s:12 m:6" label="姓名：" path="qp_name_like" class="pr-24px" :label-width="100">
      <NInput v-model:value="model.qp_name_like" placeholder="请输入" />
    </NFormItemGi>
    <!-- <NFormItemGi span="24 s:12 m:6" label="账号：" path="qp_username_like" class="pr-24px" :label-width="100">
      <NInput v-model:value="model.qp_username_like" placeholder="请输入" />
    </NFormItemGi>
    <NFormItemGi span="24 s:12 m:6" label="密码：" path="qp_password_like" class="pr-24px" :label-width="100">
      <NInput v-model:value="model.qp_password_like" placeholder="请输入" />
    </NFormItemGi> -->
    <NFormItemGi span="24 s:12 m:6" label="手机号：" path="qp_phone_like" class="pr-24px" :label-width="100">
      <NInput v-model:value="model.qp_phone_like" placeholder="请输入" />
    </NFormItemGi>
    <!-- <NFormItemGi span="24 s:12 m:6" label="封面：" path="qp_url_like" class="pr-24px" :label-width="100">
      <NInput v-model:value="model.qp_url_like" placeholder="请输入" />
    </NFormItemGi>
    <NFormItemGi span="24 s:12 m:6" label="出生日期：" path="qp_birthday_like" class="pr-24px" :label-width="100">
      <NInput v-model:value="model.qp_birthday_like" placeholder="请输入" />
    </NFormItemGi>
    <NFormItemGi span="24 s:12 m:6" label="地址：" path="qp_address_like" class="pr-24px" :label-width="100">
      <NInput v-model:value="model.qp_address_like" placeholder="请输入" />
    </NFormItemGi>
    <NFormItemGi span="24 s:12 m:6" label="性别，1 男 2 女：" path="qp_sex_like" class="pr-24px" :label-width="100">
      <NInput v-model:value="model.qp_sex_like" placeholder="请输入" />
    </NFormItemGi> -->
    <!-- <NFormItemGi span="24 s:12 m:6" label="是否启用，1启用：" path="qp_enable_like" class="pr-24px" :label-width="100">
      <NInput v-model:value="model.qp_enable_like" placeholder="请输入" />
    </NFormItemGi> -->
            <NFormItemGi span="24 m:6" class="pr-24px" :label-width="100">
              <NSpace class="w-full" justify="start">
                <NButton @click="reset">
                  <template #icon>
                    <icon-ic-round-refresh class="text-icon" />
                  </template>
                  {{ $t('common.reset') }}
                </NButton>
                <NButton type="primary" ghost @click="search">
                  <template #icon>
                    <icon-ic-round-search class="text-icon" />
                  </template>
                  {{ $t('common.search') }}
                </NButton>
              </NSpace>
            </NFormItemGi>
          </NGrid>
        </NForm>
      </NCollapseItem>
    </NCollapse>
  </NCard>
</template>

<style scoped></style>
