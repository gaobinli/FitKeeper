<template>
  <div class="userinfo-container">
    <div class="userinfo-wrapper">
      <!-- 左侧菜单 -->
      <div class="left-sidebar">
        <div class="sidebar-menu">
          <div
            class="menu-item"
            :class="{ active: activeTab === 'profile' }"
            @click="activeTab = 'profile'"
          >
            个人信息
          </div>
          <div
            class="menu-item"
            :class="{ active: activeTab === 'wallet' }"
            @click="activeTab = 'wallet'"
          >
            钱包
          </div>
        </div>
      </div>

      <!-- 右侧内容 -->
      <div class="right-content">
        <!-- 个人信息 -->
        <div v-if="activeTab === 'profile'" class="content-section">
          <h2>个人信息</h2>
          <el-form :model="registerForm" label-width="120px" class="user-form" :rules="arules" ref="registerFormRule">
            <el-form-item label="手机号：" prop="phone">
              <el-input v-model="registerForm.phone" placeholder="请输入手机号"></el-input>
            </el-form-item>
            <el-form-item label="姓名：" prop="name">
              <el-input v-model="registerForm.name" placeholder="请输入姓名"></el-input>
            </el-form-item>
            <el-form-item label="头像：" prop="avatarUrl">
              <el-upload
                :action="$imgUpload"
                list-type="picture-card"
                name="file"
                multiple="false"
                method="post"
                :show-file-list="false"
                :on-success="handleAvatarSuccess"
              >
                <img v-if="registerForm.avatarUrl" :src="registerForm.avatarUrl" class="avatar" />
                <el-icon v-else class="avatar-uploader-icon"><plus /></el-icon>
              </el-upload>
            </el-form-item>
            <el-form-item label="密码：">
              <el-input v-model="registerForm.password" placeholder="请输入密码，不修改密码则不输入" show-password></el-input>
            </el-form-item>
            <el-form-item label="年龄：" prop="age">
              <el-input-number v-model="registerForm.age" :min="1" :max="127" />
            </el-form-item>
            <el-form-item label="性别：" prop="sex">
              <el-radio v-model="registerForm.sex" label="1">男</el-radio>
              <el-radio v-model="registerForm.sex" label="2">女</el-radio>
            </el-form-item>
            <el-form-item>
              <el-button v-if="!loading" type="primary" @click="onSubmit">修改信息</el-button>
              <el-button v-else type="primary">修改中...</el-button>
            </el-form-item>
          </el-form>
        </div>

        <!-- 钱包 -->
        <div v-else-if="activeTab === 'wallet'" class="content-section">
          <h2>我的钱包</h2>
          <div class="wallet-card">
            <div class="balance-info">
              <span class="balance-label">账户余额</span>
              <span class="balance-amount">¥{{ registerForm.balance || 0 }}</span>
            </div>
            <el-button type="primary" @click="showRechargeDialog">充值</el-button>
          </div>
        </div>
      </div>
    </div>
  </div>

  <!-- 充值对话框 -->
  <el-dialog v-model="rechargeDialogVisible" title="账户充值" width="40%" center @close="resetRechargeForm">
    <el-form :model="rechargeForm" label-width="100px" :rules="rechargeRules" ref="rechargeFormRule">
      <el-form-item label="充值金额：" prop="money">
        <el-input-number v-model="rechargeForm.money" :min="0.01" :step="10" placeholder="请输入充值金额"></el-input-number>
        <span class="input-hint">元</span>
      </el-form-item>
      <el-form-item label="充值方式：" prop="channel">
        <el-select v-model="rechargeForm.channel" placeholder="请选择充值方式">
          <el-option label="微信" :value="1"></el-option>
          <el-option label="支付宝" :value="2"></el-option>
          <el-option label="银行卡" :value="3"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="备注：" prop="remark">
        <el-input
          v-model="rechargeForm.remark"
          type="textarea"
          :rows="4"
          placeholder="请输入备注信息..."
        ></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="rechargeDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitRecharge">确认充值</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script>
import { reactive, ref, onMounted } from 'vue'
import { get, post, patch } from '@/api/http'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons'

export default {
  components: { Plus },
  setup() {
    const router = useRouter()
    let loading = ref(false)
    let activeTab = ref('profile')
    let registerFormRule = ref(null)
    let rechargeFormRule = ref(null)
    let rechargeDialogVisible = ref(false)

    let registerForm = reactive({
      id: '',
      password: '',
      age: '',
      name: '',
      phone: '',
      sex: '',
      avatarUrl: '',
      balance: 0
    })

    let rechargeForm = reactive({
      money: null,
      channel: null,
      remark: ''
    })

    let arules = reactive({
      phone: [
        { required: true, trigger: ['blur', 'change'], message: '请输入正确的手机号' },
        { pattern: /^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\d{8}$/, message: '请输入正确的手机号码' }
      ],
      name: [{ required: true, trigger: ['blur', 'change'], message: '请输入您的姓名' }],
      age: [{ required: true, trigger: ['blur', 'change'], message: '请选择年龄' }],
      sex: [{ required: true, trigger: ['blur', 'change'], message: '请输入性别' }],
    })

    let rechargeRules = reactive({
      money: [
        { required: true, trigger: ['blur', 'change'], message: '请输入充值金额' }
      ],
      channel: [
        { required: true, trigger: ['blur', 'change'], message: '请选择充值方式' }
      ]
    })

    onMounted(() => {
      // Check if user is logged in
      const token = localStorage.getItem('token')
      if (!token) {
        ElMessage({
          message: '请先登录',
          type: 'warning',
        })
        router.push({ path: '/login' })
        return
      }

      get('user/getUserInfo', '').then(res => {
        registerForm.id = res.id
        registerForm.age = res.age
        registerForm.name = res.name
        registerForm.phone = res.phone
        registerForm.avatarUrl = res.avatarUrl
        registerForm.sex = res.sex + ''
        registerForm.balance = res.balance || 0
      }).catch(() => {
        ElMessage({
          message: '获取用户信息失败，请重新登录',
          type: 'error',
        })
        router.push({ path: '/login' })
      })
    })

    function onSubmit() {
      registerFormRule.value.validate((valid) => {
        if (valid) {
          loading.value = true
          patch('user/' + registerForm.id, registerForm).then(res => {
            ElMessage({
              message: '修改成功',
              type: 'success',
            })
            loading.value = false
          }).catch(() => {
            loading.value = false
          })
        } else {
          return false
        }
      })
    }

    function handleAvatarSuccess(res, file) {
      registerForm.avatarUrl = res.data
    }

    function showRechargeDialog() {
      rechargeDialogVisible.value = true
    }

    function resetRechargeForm() {
      rechargeForm.money = null
      rechargeForm.channel = null
      rechargeForm.remark = ''
    }

    function submitRecharge() {
      rechargeFormRule.value.validate((valid) => {
        if (valid) {
          let userId = parseInt(localStorage.getItem('userId'))

          // 准备充值记录数据
          let rechargeData = {
            userId: userId,
            money: rechargeForm.money,
            channel: rechargeForm.channel,
            remark: rechargeForm.remark
          }

          // 同时调用两个接口
          Promise.all([
            // 保存充值记录
            post('rechargeRecord', rechargeData),
            // 更新用户余额
            patch('user/' + registerForm.id, {
              id: registerForm.id,
              balance: registerForm.balance + rechargeForm.money
            })
          ]).then(() => {
            ElMessage({
              message: '充值成功',
              type: 'success',
            })
            registerForm.balance += rechargeForm.money
            rechargeDialogVisible.value = false
            resetRechargeForm()
          }).catch(err => {
            ElMessage({
              message: '充值失败，请重试',
              type: 'error',
            })
          })
        }
      })
    }

    return {
      loading,
      activeTab,
      arules,
      registerForm,
      registerFormRule,
      onSubmit,
      handleAvatarSuccess,
      rechargeDialogVisible,
      rechargeForm,
      rechargeRules,
      rechargeFormRule,
      showRechargeDialog,
      resetRechargeForm,
      submitRecharge
    }
  }
}
</script>

<style lang="scss" scoped>
.userinfo-container {
  width: 100%;
  min-height: 95vh;
  background-color: #f5f5f5;
  padding: 20px 0;
}

.userinfo-wrapper {
  width: 1152px;
  margin: 0 auto;
  display: flex;
  gap: 20px;
}

.left-sidebar {
  width: 200px;
  background-color: white;
  border-radius: 4px;
  padding: 20px 0;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  height: fit-content;
}

.sidebar-menu {
  display: flex;
  flex-direction: column;
}

.menu-item {
  padding: 16px 20px;
  cursor: pointer;
  color: #666;
  font-size: 14px;
  border-left: 3px solid transparent;
  transition: all 0.3s ease;

  &:hover {
    background-color: #f5f5f5;
    color: var(--primaryColor);
  }

  &.active {
    background-color: #f0f9f6;
    color: var(--primaryColor);
    border-left-color: var(--primaryColor);
    font-weight: 500;
  }
}

.right-content {
  flex: 1;
  background-color: white;
  border-radius: 4px;
  padding: 30px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.content-section {
  h2 {
    font-size: 20px;
    color: #333;
    margin-bottom: 24px;
    padding-bottom: 12px;
    border-bottom: 1px solid #e5e5e5;
  }
}

.user-form {
  max-width: 600px;

  .avatar {
    width: 100%;
    height: 100%;
    display: block;
  }

  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    text-align: center;
  }

  .el-input {
    height: 36px;

    input {
      height: 36px;
    }
  }

  .el-button {
    width: 100%;
  }
}

.wallet-card {
  background: linear-gradient(135deg, var(--primaryColor) 0%, #4dbf99 100%);
  border-radius: 8px;
  padding: 30px;
  color: white;
  margin-bottom: 24px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 4px 12px rgba(32, 189, 154, 0.2);

  .balance-info {
    display: flex;
    flex-direction: column;
  }

  .balance-label {
    font-size: 14px;
    opacity: 0.9;
    margin-bottom: 8px;
  }

  .balance-amount {
    font-size: 36px;
    font-weight: bold;
  }

  .el-button {
    width: auto;
    background-color: white;
    color: var(--primaryColor);

    &:hover {
      background-color: #f5f5f5;
    }
  }
}

.input-hint {
  margin-left: 12px;
  color: #666;
  font-size: 14px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
}
</style>
