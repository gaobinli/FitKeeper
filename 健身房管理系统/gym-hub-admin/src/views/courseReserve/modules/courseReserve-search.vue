
<script setup lang="ts">
import { computed, ref, watch } from 'vue';
import { $t } from '@/locales';
import { useFormRules, useNaiveForm } from '@/hooks/common/form';
import { translateOptions } from '@/utils/common';
import { useAuthStore } from '@/store/modules/auth';
import {
fetchUserList,
fetchCourseList,
} from '@/service/api';

defineOptions({
  name: 'CourseReserveSearch'
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

const userOptions = ref([])
async function getUserOptions() {
  const { error, data } = await fetchUserList();
  if (!data) {
    return
  }
  userOptions.value = []
  for (const item of data) {
    userOptions.value.push({ label: item.name, value: item.id });
  }
  //console.log('userOptions========>', userOptions.value)
}
const courseOptions = ref([])
async function getCourseOptions() {
  const { error, data } = await fetchCourseList();
  if (!data) {
    return
  }
  courseOptions.value = []
  for (const item of data) {
    courseOptions.value.push({ label: item.name, value: item.id });
  }
  //console.log('courseOptions========>', courseOptions.value)
}
const auditStatusOptions = ref([
    {
      label: '待审核',
      value: 1
    },
    {
      label: '审核驳回',
      value: 2
    },
    {
      label: '审核通过',
      value: 3
    },
])

function init() {
  getUserOptions()
  getCourseOptions()
}

init()
</script>

<template>
  <NCard :bordered="false" size="small" class="card-wrapper">
    <NCollapse :default-expanded-names="['courseReserve-search']">
      <NCollapseItem title="搜索" name="courseReserve-search">
        <NForm ref="formRef" :model="model" :rules="rules" label-placement="left" :label-width="80">
          <NGrid responsive="screen" item-responsive>
    <NFormItemGi span="24 s:12 m:6" label="用户：" path="qp_userId_eq" class="pr-24px" :label-width="100">
      <NSelect
        v-model:value="model.qp_userId_eq"
        :options="userOptions"
        placeholder="请选择"
      />
    </NFormItemGi>
    <NFormItemGi v-if="authStore.userInfo.role !== 'other'" span="24 s:12 m:6" label="课程：" path="qp_courseId_eq" class="pr-24px" :label-width="100">
      <NSelect
        v-model:value="model.qp_courseId_eq"
        :options="courseOptions"
        placeholder="请选择"
      />
    </NFormItemGi>
    <NFormItemGi span="24 s:12 m:6" label="预约状态：" path="qp_auditStatus_eq" class="pr-24px" :label-width="100">
      <NSelect
        v-model:value="model.qp_auditStatus_eq"
        :options="auditStatusOptions"
        placeholder="请选择"
      />
    </NFormItemGi>
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
