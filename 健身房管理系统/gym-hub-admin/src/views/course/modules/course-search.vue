
<script setup lang="ts">
import { computed, ref, watch } from 'vue';
import { $t } from '@/locales';
import { useFormRules, useNaiveForm } from '@/hooks/common/form';
import { translateOptions } from '@/utils/common';
import {
fetchCoachList,
fetchClassificationList,
} from '@/service/api';
import { useAuthStore } from '@/store/modules/auth';

defineOptions({
  name: 'CourseSearch'
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

const coachOptions = ref([])
async function getCoachOptions() {
  const { error, data } = await fetchCoachList();
  if (!data) {
    return
  }
  coachOptions.value = []
  for (const item of data) {
    coachOptions.value.push({ label: item.name, value: item.id });
  }
  //console.log('coachOptions========>', coachOptions.value)
}
const classificationOptions = ref([])
async function getClassificationOptions() {
  const { error, data } = await fetchClassificationList();
  if (!data) {
    return
  }
  classificationOptions.value = []
  for (const item of data) {
    classificationOptions.value.push({ label: item.name, value: item.id });
  }
  //console.log('classificationOptions========>', classificationOptions.value)
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
  getCoachOptions()
  getClassificationOptions()
}

init()
</script>

<template>
  <NCard :bordered="false" size="small" class="card-wrapper">
    <NCollapse :default-expanded-names="['course-search']">
      <NCollapseItem title="搜索" name="course-search">
        <NForm ref="formRef" :model="model" :rules="rules" label-placement="left" :label-width="80">
          <NGrid responsive="screen" item-responsive>
    <NFormItemGi v-if="authStore.userInfo.role !== 'other'" span="24 s:12 m:6" label="课程名称：" path="qp_name_like" class="pr-24px" :label-width="100">
      <NInput v-model:value="model.qp_name_like" placeholder="请输入" />
    </NFormItemGi>
    <NFormItemGi v-if="authStore.userInfo.role !== 'other'" span="24 s:12 m:6" label="教练：" path="qp_coachId_eq" class="pr-24px" :label-width="100">
      <NSelect
        v-model:value="model.qp_coachId_eq"
        :options="coachOptions"
        placeholder="请选择"
      />
    </NFormItemGi>
    <NFormItemGi span="24 s:12 m:6" label="分类：" path="qp_classificationId_eq" class="pr-24px" :label-width="100">
      <NSelect
        v-model:value="model.qp_classificationId_eq"
        :options="classificationOptions"
        placeholder="请选择"
      />
    </NFormItemGi>
    <NFormItemGi span="24 s:12 m:6" label="审核状态：" path="qp_auditStatus_eq" class="pr-24px" :label-width="100">
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
