<script setup lang="ts">
import { computed, ref, watch } from 'vue';
import { useFormRules, useNaiveForm } from '@/hooks/common/form';
import { fetchGetAllRoles, fetchPostSysUser, fetchPatchSysUser, fetchGetAllRole } from '@/service/api';
import { $t } from '@/locales';
import { enableStatusOptions, userGenderOptions } from '@/constants/business';

defineOptions({
  name: 'UserOperateDrawer'
});

interface Props {
  /** the type of operation */
  operateType: NaiveUI.TableOperateType;
  /** the edit row data */
  rowData?: Api.SystemManage.User | null;
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
  const titles: Record<NaiveUI.TableOperateType, string> = {
    add: $t('page.manage.user.addUser'),
    edit: $t('page.manage.user.editUser')
  };
  return titles[props.operateType];
});

type Model = Pick<
  Api.SystemManage.User,
  'username' | 'name' | 'phone'
>;

const model = ref(createDefaultModel());

function createDefaultModel(): Model {
  return {
    id: null,
    username: '',
    password: '',
    name: '',
    phone: '',
    email: ''
  };
}

type RuleKey = Extract<keyof Model, 'username' | 'name' | 'phone'>;

const rules: Record<RuleKey, App.Global.FormRule> = {
  username: defaultRequiredRule,
  name: defaultRequiredRule,
  phone: [defaultRequiredRule, patternRules.phone],
};

/** the enabled role options */
const roleOptions = ref<CommonType.Option<string>[]>([]);

async function getRoleOptions() {
  // const { error, data } = await fetchGetAllRoles();

  // if (!error) {
  //   const options = data.map(item => ({
  //     label: item.roleName,
  //     value: item.roleCode
  //   }));

  //   // the mock data does not have the roleCode, so fill it
  //   // if the real request, remove the following code
  //   const userRoleOptions = model.value.userRoles.map(item => ({
  //     label: item,
  //     value: item
  //   }));
  //   // end

  //   roleOptions.value = [...userRoleOptions, ...options];
  // }
}

function handleInitModel() {
  model.value = createDefaultModel();

  if (props.operateType === 'edit' && props.rowData) {
    Object.assign(model.value, props.rowData);
  }

  getAllRoleOptions()
}

async function getAllRoleOptions() {
  const { error, data } = await fetchGetAllRole();
  if (!data) {
    return
  }
  allRoleOptions.value = []
  
  for (const item of data) {
    allRoleOptions.value.push({ label: item.name, value: item.code });
  }
}

const allRoleOptions = ref([])

function closeDrawer() {
  visible.value = false;
}

async function handleSubmit() {
  await validate();
  // request
  if (model.value.id) {
    const { error, data } = await fetchPatchSysUser(model.value.id, model.value);
    if (error) {
      return 
    }
  } else {
    if (!model.value.password) {
      model.value.password = model.value.code
    }
    const { error, data } = await fetchPostSysUser(model.value);
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
    getRoleOptions();
  }
});
</script>

<template>
  <NDrawer v-model:show="visible" display-directive="show" :width="360">
    <NDrawerContent :title="title" :native-scrollbar="false" closable>
      <NForm ref="formRef" :model="model" :rules="rules">
        <NFormItem label="账号" path="username">
          <NInput v-model:value="model.username" placeholder="请输入" />
        </NFormItem>
        <NFormItem label="密码" path="password">
          <NInput v-model:value="model.password" placeholder="请输入" />
        </NFormItem>
        <NFormItem label="姓名" path="name">
          <NInput v-model:value="model.name" placeholder="请输入" />
        </NFormItem>
        <NFormItem label="手机号" path="phone">
          <NInput v-model:value="model.phone" placeholder="请输入" />
        </NFormItem>
        <NFormItem label="邮箱" path="email">
          <NInput v-model:value="model.email" placeholder="请输入" />
        </NFormItem>
        <NFormItem label="状态" path="enable">
            <n-switch v-model:value="model.enable" />
        </NFormItem>
        <NFormItem label="角色" path="roles">
          <NSelect
            v-model:value="model.roleCodes"
            multiple
            :options="allRoleOptions"
            placeholder="请选择"
          />
        </NFormItem>
      </NForm>
      <template #footer>
        <NSpace :size="16">
          <NButton @click="closeDrawer">{{ $t('common.cancel') }}</NButton>
          <NButton type="primary" @click="handleSubmit">{{ $t('common.confirm') }}</NButton>
        </NSpace>
      </template>
    </NDrawerContent>
  </NDrawer>
</template>

<style scoped></style>
