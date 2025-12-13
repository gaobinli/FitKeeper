
<script setup lang="tsx">
import { computed, ref, watch, reactive } from 'vue';
import type { SelectOption } from 'naive-ui';
import { useFormRules, useNaiveForm } from '@/hooks/common/form';
import { $t } from '@/locales';
import SvgIcon from '@/components/custom/svg-icon.vue';
import { getLocalIcons } from '@/utils/icon';
import {
fetchPostCoach,
fetchPatchCoach,
} from '@/service/api';
import { useAuthStore } from '@/store/modules/auth';
import dayjs from 'dayjs';
import WangEditor from "@/components/custom/WangEditor.vue";

defineOptions({
  name: 'CoachOperateModal'
});

const uploadUrl = import.meta.env.VITE_SERVICE_BASE_URL + "/upload"
const authStore = useAuthStore();

export type OperateType = NaiveUI.TableOperateType | 'addChild';

interface Props {
  /** the type of operation */
  operateType: OperateType;
  /** the edit menu data or the parent menu data when adding a child menu */
  rowData?: Api.SystemManage.Menu | null;
}

const props = defineProps<Props>();

interface Emits {
  (e: 'submitted'): void;
}

const emit = defineEmits<Emits>();

const visible = defineModel<boolean>('visible', {
  default: false
});

const { formRef, validate, restoreValidation } = useNaiveForm();
const { defaultRequiredRule, patternRules } = useFormRules();

const title = computed(() => {
  const titles: Record<OperateType, string> = {
    add: '新增',
    edit: '编辑'
  };
  return titles[props.operateType];
});

const passwordValue = computed({
  get: () => props.operateType === 'edit' ? model.value.newPassword : model.value.password,
  set: (val) => {
    if (props.operateType === 'edit') {
      model.value.newPassword = val;
    } else {
      model.value.password = val;
    }
  }
});

const model = ref(createDefaultModel());

function createDefaultModel(): Model {
  return {
      id: null,
      name: null,
      username: null,
      password: null,
      newPassword: null,
      phone: null,
      url: null,
      birthday: null,
      address: null,
      sex: null,
      enable: null,
      introduceContent: null,
  };
}

type RuleKey = Extract<keyof Model,
'undefind'
| 'id'
| 'name'
| 'username'
| 'password'
| 'newPassword'
| 'phone'
| 'url'
| 'birthday'
| 'address'
| 'sex'
| 'enable'
| 'introduceContent'
>;

const rules: Record<RuleKey, App.Global.FormRule> = {
  'id' : defaultRequiredRule,
  'name' : defaultRequiredRule,
  'username' : defaultRequiredRule,
  'phone' : [defaultRequiredRule, patternRules.phone],
  'sex' : defaultRequiredRule,
  'enable' : defaultRequiredRule,
};

function handleInitModel() {
  model.value = createDefaultModel();

  if (props.operateType === 'edit' && props.rowData) {
    Object.assign(model.value, props.rowData);
    if(model.value.birthday && typeof model.value.birthday === 'string') {
      model.value.birthday = new Date(model.value.birthday).getTime();
    }
  }
  if(model.value.url) {
      previewFileList.value = [
          {
            id: '1',
            name: '我是react.png',
            status: 'finished',
            url: model.value.url
          },
      ]
      model.value.url
    }
}

function closeDrawer() {
  visible.value = false;
}

async function handleSubmit() {
  await validate();
  // request
  const submitData = { ...model.value };
  if (submitData.birthday && typeof submitData.birthday === 'number') {
    submitData.birthday = dayjs(submitData.birthday).format('YYYY-MM-DD HH:mm:ss');
  }

  // 处理密码字段
  if (model.value.id) {
    // 编辑时：如果输入了新密码则使用newPassword，否则删除密码字段
    if (submitData.newPassword) {
      submitData.password = submitData.newPassword;
    } else {
      delete submitData.password;
    }
    delete submitData.newPassword;

    const { error, data } = await fetchPatchCoach(model.value.id, submitData);
    if (error) {
      return
    }
  } else {
    // 新增时删除newPassword字段
    delete submitData.newPassword;
    const { error, data } = await fetchPostCoach(submitData);
    if (error) {
      return
    }
  }

  window.$message?.success($t('common.updateSuccess'));
  closeDrawer();
  emit('submitted');
}

watch(visible, () => {
  if (visible.value) {
    handleInitModel();
    restoreValidation();
  } else {
    previewFileList.value = []
  }
});

// watch(
//   () => model.value.routeName,
//   () => {
//     handleUpdateRoutePathByRouteName();
//     handleUpdateI18nKeyByRouteName();
//   }
// );

const previewFileList = ref([])
function handleFileListUpdate(files) {
  console.log("======files======>", files)
  if (files && files.event && files.event?.target && files.event.target.response) {
    model.value.url = JSON.parse(files.event.target.response).data
  }

  if(model.value.url) {
    previewFileList.value = [
        {
          id: '1',
          name: '我是react.png',
          status: 'finished',
          url: model.value.url
        },
    ]
    // model.value.url
  }
}

const sexOptions = ref([
    {
      label: '男',
      value: 1
    },
    {
      label: '女',
      value: 2
    },
])

const enableOptions = ref([
    {
      label: '启用',
      value: true
    },
    {
      label: '禁用',
      value: false
    },
])

const getWangEditorValue = (str: string) => {
  model.value.introduceContent = str
};

</script>


<template>
  <NModal v-model:show="visible" :title="title" preset="card" class="w-800px">
    <NScrollbar class="h-480px pr-20px">
      <NForm ref="formRef" :model="model" :rules="rules" label-placement="left" :label-width="100">
        <NGrid responsive="screen" item-responsive>
          <NFormItemGi span="24 m:24" label="账号：" path="username">
            <NInput v-model:value="model.username" placeholder="请输入账号" :disabled="operateType === 'edit'" />
          </NFormItemGi>
          <NFormItemGi span="24 m:24" label="姓名：" path="name">
            <NInput v-model:value="model.name" placeholder="请输入姓名" />
          </NFormItemGi>
          <NFormItemGi span="24 m:24" label="密码：" path="password">
            <NInput v-model:value="passwordValue" :placeholder="operateType === 'edit' ? '不修改密码请留空' : '请输入密码'" />
          </NFormItemGi>
          <NFormItemGi span="24 m:24" label="手机号：" path="phone">
            <NInput v-model:value="model.phone" placeholder="请输入手机号" />
          </NFormItemGi>
          <NFormItemGi span="24 m:24" label="宣传照：" path="url">
            <NUpload
              :action="uploadUrl"
              :default-file-list="previewFileList"
              list-type="image-card"
              :max=1
              @change="handleFileListUpdate"
            />
          </NFormItemGi>
          <NFormItemGi span="24 m:24" label="出生日期：" path="birthday">
            <NDatePicker v-model:value="model.birthday" type="date" clearable />
          </NFormItemGi>
          <NFormItemGi span="24 m:24" label="地址：" path="address">
            <NInput v-model:value="model.address" placeholder="请输入地址" />
          </NFormItemGi>
          <NFormItemGi span="24 m:24" label="性别：" path="sex">
            <NSelect
              v-model:value="model.sex"
              :options="sexOptions"
              placeholder="请选择"
            />
          </NFormItemGi>
          <NFormItemGi span="24 m:24" label="状态：" path="enable">
            <NSelect
              v-model:value="model.enable"
              :options="enableOptions"
              placeholder="请选择"
            />
          </NFormItemGi>
          <NFormItemGi span="24 m:24" label="介绍：" path="introduceContent">
            <wang-editor :contentHtml="model.introduceContent" v-on:getWangEditorValue="getWangEditorValue"></wang-editor>
          </NFormItemGi>
        </NGrid>
      </NForm>
    </NScrollbar>
    <template #footer>
      <NSpace justify="end" :size="16">
        <NButton @click="closeDrawer">{{ $t('common.cancel') }}</NButton>
        <NButton type="primary" @click="handleSubmit">{{ $t('common.confirm') }}</NButton>
      </NSpace>
    </template>
  </NModal>
</template>

<style scoped></style>