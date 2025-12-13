
<script setup lang="ts">
import { computed, ref, watch } from 'vue';
import { $t } from '@/locales';
import { useFormRules, useNaiveForm } from '@/hooks/common/form';
import { translateOptions } from '@/utils/common';
import { useAuthStore } from '@/store/modules/auth';
import {
fetchPostList,
fetchUserList,
} from '@/service/api';

defineOptions({
  name: 'PostCommentSearch'
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

const postOptions = ref([])
async function getPostOptions() {
  const { error, data } = await fetchPostList();
  if (!data) {
    return
  }
  postOptions.value = []
  for (const item of data) {
    postOptions.value.push({ label: item.title, value: item.id });
  }
  //console.log('postOptions========>', postOptions.value)
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
  //console.log('replyUserOptions========>', replyUserOptions.value)
}
const commentOptions = ref([])
async function getCommentOptions() {
  const { error, data } = await fetchUserList();
  if (!data) {
    return
  }
  commentOptions.value = []
  for (const item of data) {
    commentOptions.value.push({ label: item.name, value: item.id });
  }
  //console.log('commentOptions========>', commentOptions.value)
}

function init() {
  getPostOptions()
  getUserOptions()
  getCommentOptions()
}

init()
</script>

<template>
  <NCard :bordered="false" size="small" class="card-wrapper">
    <NCollapse :default-expanded-names="['postComment-search']">
      <NCollapseItem title="搜索" name="postComment-search">
        <NForm ref="formRef" :model="model" :rules="rules" label-placement="left" :label-width="80">
          <NGrid responsive="screen" item-responsive>
    <NFormItemGi span="24 s:12 m:6" label="帖子：" path="qp_postId_eq" class="pr-24px">
      <NSelect
        v-model:value="model.qp_postId_eq"
        :options="postOptions"
        placeholder="请选择"
      />
    </NFormItemGi>
    <NFormItemGi span="24 s:12 m:6" label="评论人：" path="qp_userId_eq" class="pr-24px">
      <NSelect
        v-model:value="model.qp_userId_eq"
        :options="userOptions"
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
