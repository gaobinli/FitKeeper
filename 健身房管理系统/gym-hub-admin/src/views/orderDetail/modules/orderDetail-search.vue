
<script setup lang="ts">
import { computed, ref, watch } from 'vue';
import { $t } from '@/locales';
import { useFormRules, useNaiveForm } from '@/hooks/common/form';
import { translateOptions } from '@/utils/common';
import { useAuthStore } from '@/store/modules/auth';
import {
fetchSkuList,
} from '@/service/api';

defineOptions({
  name: 'OrderDetailSearch'
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

const skuOptions = ref([])
async function getSkuOptions() {
  const { error, data } = await fetchSkuList();
  if (!data) {
    return
  }
  skuOptions.value = []
  for (const item of data) {
    skuOptions.value.push({ label: item.name, value: item.id });
  }
  //console.log('skuOptions========>', skuOptions.value)
}

function init() {
  getSkuOptions()
}

init()
</script>

<template>
  <NCard :bordered="false" size="small" class="card-wrapper">
    <NCollapse :default-expanded-names="['orderDetail-search']">
      <NCollapseItem title="搜索" name="orderDetail-search">
        <NForm ref="formRef" :model="model" :rules="rules" label-placement="left" :label-width="80">
          <NGrid responsive="screen" item-responsive>
    <NFormItemGi span="24 s:12 m:6" label="订单编号：" path="qp_orderNo_like" class="pr-24px">
      <NInput v-model:value="model.qp_orderNo_like" placeholder="请输入" />
    </NFormItemGi>
    <NFormItemGi span="24 s:12 m:6" label="skuId：" path="qp_skuId_eq" class="pr-24px">
      <NSelect
        v-model:value="model.qp_skuId_eq"
        :options="skuOptions"
        placeholder="请选择"
      />
    </NFormItemGi>
    <NFormItemGi span="24 s:12 m:6" label="数量：" path="qp_number_like" class="pr-24px">
      <NInput v-model:value="model.qp_number_like" placeholder="请输入" />
    </NFormItemGi>
    <NFormItemGi span="24 s:12 m:6" label="购买价格：" path="qp_price_like" class="pr-24px">
      <NInput v-model:value="model.qp_price_like" placeholder="请输入" />
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
