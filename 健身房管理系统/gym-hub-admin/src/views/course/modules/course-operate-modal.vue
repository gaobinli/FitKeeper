
<script setup lang="tsx">
import { computed, ref, watch, reactive } from 'vue';
import type { SelectOption } from 'naive-ui';
import { useFormRules, useNaiveForm } from '@/hooks/common/form';
import { $t } from '@/locales';
import SvgIcon from '@/components/custom/svg-icon.vue';
import { getLocalIcons } from '@/utils/icon';
import {
fetchPostCourse,
fetchPatchCourse,
fetchCoachList,
fetchClassificationList,
} from '@/service/api';
import { useAuthStore } from '@/store/modules/auth';
import dayjs from 'dayjs';

defineOptions({
  name: 'CourseOperateModal'
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
const { defaultRequiredRule } = useFormRules();

const title = computed(() => {
  const titles: Record<OperateType, string> = {
    add: '新增',
    edit: '编辑'
  };
  return titles[props.operateType];
});

const model = ref(createDefaultModel());

function createDefaultModel(): Model {
  return {
      id: null,
      name: null,
      coachId: null,
      price: null,
      classificationId: null,
      coverUrl: null,
      content: null,
      courseTime: null,
      limitNumber: null,
      simpleDesc: null,
      auditDesc: null,
      auditStatus: null,
      recommendedScore: null,
  };
}

type RuleKey = Extract<keyof Model,
'undefind'
| 'id'
| 'name'
| 'coachId'
| 'price'
| 'classificationId'
| 'coverUrl'
| 'content'
| 'courseTime'
| 'limitNumber'
| 'simpleDesc'
| 'auditDesc'
| 'auditStatus'
| 'recommendedScore'
>;

const rules: Record<RuleKey, App.Global.FormRule> = {
  'id' : defaultRequiredRule,
  'name' : defaultRequiredRule,
  'coachId' : defaultRequiredRule,
  'price' : defaultRequiredRule,
  'classificationId' : defaultRequiredRule,
  'coverUrl' : defaultRequiredRule,
  'content' : defaultRequiredRule,
  'courseTime' : defaultRequiredRule,
  'limitNumber' : defaultRequiredRule,
  'recommendedScore' : defaultRequiredRule,
};

function handleInitModel() {
  model.value = createDefaultModel();

  if (props.operateType === 'edit' && props.rowData) {
    Object.assign(model.value, props.rowData);
    // Keep original auditStatus when editing
    if(model.value.courseTime && typeof model.value.courseTime === 'string') {
      model.value.courseTime = new Date(model.value.courseTime).getTime();
    }
  } else if (props.operateType === 'add') {
    // Set default auditStatus to 1 (pending audit) when creating new course
    model.value.auditStatus = 1;
  }
  if(model.value.coverUrl) {
      previewFileList.value = [
          {
            id: '1',
            name: '我是react.png',
            status: 'finished',
            url: model.value.coverUrl
          },
      ]
      model.value.coverUrl
    }
}

function closeDrawer() {
  visible.value = false;
}

async function handleSubmit() {
  await validate();
  // request
  const submitData = { ...model.value };
  if (submitData.courseTime && typeof submitData.courseTime === 'number') {
    submitData.courseTime = dayjs(submitData.courseTime).format('YYYY-MM-DD HH:mm:ss');
  }

  if (model.value.id) {
    // When editing, reset auditStatus to 1 (pending audit)
    submitData.auditStatus = 1;
    const { error, data } = await fetchPatchCourse(model.value.id, submitData);
    if (error) {
      return
    }
  } else {
    const { error, data } = await fetchPostCourse(submitData);
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
    getCoachOptions()
    getClassificationOptions()
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
  //console.log("======files======>", files)
  if (files && files.event && files.event?.target && files.event.target.response) {
    model.value.coverUrl = JSON.parse(files.event.target.response).data
  }

  if(model.value.coverUrl) {
    previewFileList.value = [
        {
          id: '1',
          name: '我是react.png',
          status: 'finished',
          url: model.value.coverUrl
        },
    ]
    model.value.coverUrl
  }
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
import WangEditor from "@/components/custom/WangEditor.vue";
const getWangEditorValue = (str: string) => {
  model.value.content = str
};
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
</script>


<template>
  <NModal v-model:show="visible" :title="title" preset="card" class="w-800px">
    <NScrollbar class="h-480px pr-20px">
      <NForm ref="formRef" :model="model" :rules="rules" label-placement="left" :label-width="100">
        <NGrid responsive="screen" item-responsive>
          <NFormItemGi span="24 m:24" label="课程名称：" path="name">
            <NInput v-model:value="model.name" placeholder="请输入课程名称" />
          </NFormItemGi>
          <NFormItemGi span="24 m:24" label="课程封面：" path="url">
            <NUpload
              :action="uploadUrl"
              :default-file-list="previewFileList"
              list-type="image-card"
              :max=1
              @change="handleFileListUpdate"
            />
          </NFormItemGi>
          <NFormItemGi span="24 m:24" label="教练：" path="coachId" class="pr-24px">
            <NSelect
              v-model:value="model.coachId"
              :options="coachOptions"
              placeholder="请选择"
            />
          </NFormItemGi>
          <NFormItemGi span="24 m:24" label="分类：" path="classificationId" class="pr-24px">
            <NSelect
              v-model:value="model.classificationId"
              :options="classificationOptions"
              placeholder="请选择"
            />
          </NFormItemGi>
          <NFormItemGi span="24 m:24" label="价格：" path="price">
            <NInputNumber v-model:value="model.price" placeholder="请输入价格" />
          </NFormItemGi>

          <NFormItemGi span="24 m:24" label="课程时间：" path="courseTime">
            <NDatePicker v-model:value="model.courseTime" type="datetime" clearable />
          </NFormItemGi>
          <NFormItemGi span="24 m:24" label="限制人数：" path="limitNumber">
            <NInputNumber v-model:value="model.limitNumber" placeholder="请输入限制人数" />
          </NFormItemGi>
          <NFormItemGi span="24 m:24" label="简单描述：" path="simpleDesc">
            <NInput v-model:value="model.simpleDesc" type="textarea" placeholder="请输入简单描述" />
          </NFormItemGi>
          <NFormItemGi span="24 m:24" label="推荐指数：" path="recommendedScore">
            <NRate v-model:value="model.recommendedScore" />
          </NFormItemGi>
          
          <NFormItemGi span="24 m:24" label="课程内容：" path="content">
            <wang-editor :contentHtml="model.content" v-on:getWangEditorValue="getWangEditorValue"></wang-editor>
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