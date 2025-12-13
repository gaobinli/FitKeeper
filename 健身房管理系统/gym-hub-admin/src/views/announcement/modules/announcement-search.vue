
<script setup lang="ts">
import { computed, ref, watch } from 'vue';
import { $t } from '@/locales';
import { useFormRules, useNaiveForm } from '@/hooks/common/form';
import { translateOptions } from '@/utils/common';
import { useAuthStore } from '@/store/modules/auth';
import {
} from '@/service/api';

defineOptions({
  name: 'AnnouncementSearch'
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

const typeOptions = ref([
    {
      label: '健身资讯',
      value: 1
    },
    {
      label: '系统公告',
      value: 2
    },
])

function init() {
}

init()
</script>

<template>
  <NCard :bordered="false" size="small" class="card-wrapper">
    <NCollapse :default-expanded-names="['announcement-search']">
      <NCollapseItem title="搜索" name="announcement-search">
        <NForm ref="formRef" :model="model" :rules="rules" label-placement="left" :label-width="80">
          <NGrid responsive="screen" item-responsive>
    <NFormItemGi span="24 s:12 m:6" label="标题：" path="qp_title_like" class="pr-24px">
      <NInput v-model:value="model.qp_title_like" placeholder="请输入" />
    </NFormItemGi>
    <NFormItemGi span="24 s:12 m:6" label="类型：" path="qp_type_eq" class="pr-24px">
      <NSelect
        v-model:value="model.qp_type_eq"
        :options="typeOptions"
        placeholder="请选择"
      />
    </NFormItemGi>
            <NFormItemGi span="24 m:6" class="pr-24px">
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
