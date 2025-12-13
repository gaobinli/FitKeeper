
<script setup lang="ts">
import { computed, ref, watch } from 'vue';
import { $t } from '@/locales';
import { useFormRules, useNaiveForm } from '@/hooks/common/form';
import { translateOptions } from '@/utils/common';
import { useAuthStore } from '@/store/modules/auth';
import { fetchUserList,
} from '@/service/api';

defineOptions({
  name: 'OrderSearch'
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

const statusOptions = ref([
    {
      label: '待出餐',
      value: 1
    },
    {
      label: '已出餐',
      value: 2
    },
    {
      label: '已取餐',
      value: 3
    },
])
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

function init() {
  getUserOptions()
}

init()
</script>

<template>
  <NCard :bordered="false" size="small" class="card-wrapper">
    <NCollapse :default-expanded-names="['order-search']">
      <NCollapseItem title="搜索" name="order-search">
        <NForm ref="formRef" :model="model" :rules="rules" label-placement="left" :label-width="80">
          <NGrid responsive="screen" item-responsive>
    <NFormItemGi span="24 s:12 m:6" label="用户：" path="qp_userId_eq" class="pr-24px">
      <NSelect
        v-model:value="model.qp_userId_eq"
        :options="userOptions"
        placeholder="请选择"
      />
    </NFormItemGi>
    <NFormItemGi span="24 s:12 m:6" label="订单状态：" path="qp_status_eq" class="pr-24px">
      <NSelect
        v-model:value="model.qp_status_eq"
        :options="statusOptions"
        placeholder="请选择"
      />
    </NFormItemGi>
    <NFormItemGi span="24 s:12 m:6" label="订单编号：" path="qp_orderNo_like" class="pr-24px">
      <NInput v-model:value="model.qp_orderNo_like" placeholder="请输入" />
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
