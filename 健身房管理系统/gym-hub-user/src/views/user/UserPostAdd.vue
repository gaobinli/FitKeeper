
<template>
    <div class="main">
        <el-form :inline="false" :model="oneData" ref="dataFormRuleRef" :rules="customizeRules">

            <el-row :gutter="20" style="background-color: white;">
                <el-col :span="10">
                    <el-form-item label="主题：" prop="title">
                        <el-input v-model="oneData.title" placeholder="请输入主题"></el-input>
                    </el-form-item>
                </el-col>
            </el-row>

            <el-form-item label="内容：" prop="content">
                <v-wangeditor
                    :content="oneData.content"
                    :options="options"
                    @change="change"
                    style="width: 80%; margin: 0 0"
                />
            </el-form-item>
            <el-form-item style="padding-left:45%">
                <el-button @click="toIndex">返回</el-button>
                <el-button type="primary" @click="add" >确定</el-button>
            </el-form-item>
        </el-form>
    </div>
</template>

<script>
import { reactive, ref, onMounted, onBeforeUnmount,getCurrentInstance } from 'vue'
import { Plus } from '@element-plus/icons'
import { get,post,del,patch } from '@/api/http'
import { ElMessageBox, ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'

export default({
    components: { Plus },
    setup() {
        const router = useRouter()
        let userId = localStorage.getItem('userId');
        const { proxy } = getCurrentInstance()
        let dataFormRuleRef = ref(null)
        let oneData = reactive({
            id: null,
            title: '',
            content: '',
        })

        let customizeRules = reactive({
            title: {required: true, message: '请输入标题', trigger: 'blur'},
            content: {required: true, message: '请输入内容', trigger: 'blur'},
            excellent: {required: true, message: '请输入是否加精，1 加精', trigger: 'blur'},
            auditStatus: {required: true, message: '请输入审核状态，0 初始化、1 审核通过', trigger: 'blur'},
        })

        const options = reactive({
            height: 600,
            zIndex: 1,
            pasteIgnoreImg: false,
            uploadImgServer: proxy.$imgUpload,
            uploadFileName: 'file',
            uploadImgHooks: {
                customInsert: function (insertLinkImg, result, editor) {
                    console.log('customInsert')
                    console.log('result：', result)
                    // 图片上传并返回结果，自定义插入图片的事件，而不是编辑器自动插入图片
                    insertLinkImg(result.data)
                },
            },
            uploadVideoServer: proxy.$imgUpload,
            uploadVideoName: 'file',
            uploadVideoHooks: {
                customInsert: function(insertVideoFn, result) {
                    // result 即服务端返回的接口
                    console.log('customInsert', result)
                    // insertVideoFn 可把视频插入到编辑器，传入视频 src ，执行函数即可
                    insertVideoFn(result.data)
                }
            },
        })

        function change({ html }) {
            console.log(html)
            oneData.content = html
        }

        function add() {
            const userToken = localStorage.getItem('token')
            if (!userToken)  {
                ElMessage({message: '亲，请先登录', type: 'info'})
                setTimeout(function() {
                    router.push({path:'/login'});
                }, 2000);
                return
            }
            
            dataFormRuleRef.value.validate((valid) => {
                if (valid) {
                    oneData.userId = userId
                    post('post', oneData).then(res => {
                        oneData.title = ''
                        oneData.content = ''
                        router.push({path:'/post'})
                        ElMessage({message: '亲，内容发布成功', type: 'success'})
                    })
                } else {
                    return false
                }
            })
        }

        function toIndex() {
            router.push({path:'/post'})
        }

        function handleAvatarSuccess(res, file) {
            oneData.coverUrl = res.data
        }


        onMounted(() => {

        })

        return {
            oneData,
            customizeRules,
            dataFormRuleRef,
            handleAvatarSuccess,
            add,
            options,
            change,
            toIndex
        }
    }
})
</script>

<style lang="scss" scoped>
.main {
    margin: 0 auto;
    margin-top: 50px;
    width: 1152px;
}
</style>