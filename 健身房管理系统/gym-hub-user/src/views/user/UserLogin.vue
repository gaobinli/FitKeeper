<template>
    <div class="login-main">
      <div class="login">
          <el-form :model="loginForm" :rules="loginRules" ref="loginFormRule" class="login-form">
            <h3 class="title">账号登录</h3>
            
            <el-form-item prop="username">
              <el-input v-model="loginForm.username" placeholder="账号" type="text">
              <template #prefix>
                <svg-icon icon-class="user" class="el-input__icon input-icon" />
              </template>
              </el-input>
            </el-form-item>
            <el-form-item prop="password">
              <el-input v-model="loginForm.password" placeholder="密码" show-password>
                <template #prefix>
                <svg-icon icon-class="password" class="el-input__icon input-icon" />
                </template>
              </el-input>
            </el-form-item>
            <el-form-item prop="code">
              <el-input v-model="loginForm.code" placeholder="验证码" class="login-code-input">
                <template #prefix>
                  <svg-icon icon-class="validCode" class="el-input__icon input-icon" />
                </template>
              </el-input>
              <div class="login-code">
                <img :src="codeUrl" @click="getCode" class="login-code-img"/>
              </div>
            </el-form-item>
            <el-form-item style="width:100%;">
              <el-button v-if="!loading" type="primary" @click="onSubmit">登录</el-button>
              <el-button v-else type="primary">登录中...</el-button>
            </el-form-item>
            <div class="register-class">
                <router-link :to="{path:'/register'}">注册</router-link>
            </div>
          </el-form>
      </div>
    </div>
</template>

<script>
import { reactive, ref } from '@vue/reactivity'
import { onMounted} from 'vue'
import { get,post } from '../../api/http'
import { useRouter } from "vue-router";
import { ElMessage, ElMessageBox } from 'element-plus'

export default {
  setup() {
    const router = useRouter()
    let loading = ref(false)
    let codeUrl = ref('')
    let loginFormRule = ref(null)
    let loginForm = reactive({
      username: '13594877777',
      password: '123456',
      uuid: '',
      code: '1234'
    })

    let loginRules = reactive({
      username: [
          { required: true, trigger: "blur", message: "请输入您的账号" }
      ],
      password: [
          { required: true, trigger: "blur", message: "请输入您的密码" }
      ],
      code: [{ required: true, trigger: "change", message: "请输入验证码" }]
    })

    onMounted(() => {
        getCode()
    })

    function getCode() {
      get('captchaImage', '').then(res => {
        codeUrl.value = "data:image/gif;base64," + res.img
        loginForm.uuid = res.uuid
      })
    }

    function onSubmit() {
      loginFormRule.value.validate((valid) => {
        if(valid) {
          post('user/login', loginForm).then(res => {
            localStorage.setItem('token', res)
            ElMessage({ message: '登录成功，跳转首页中', type: 'success' })
            router.push({path:'/'});
            setTimeout(function () {
              window.location.reload();
            }, 100);
          })
        } else {
            return false
        }
      })
      
    }

    return {
      loading,
      codeUrl,
      loginForm,
      loginRules,
      loginFormRule,
      getCode,
      onSubmit,
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.login-main {
  background-image: url("../../assets/images/background.jpg");
  width: 100%;
  height: 100vh;
  background-color: #707070;
  background-size: cover;
  background-position: center;
  background-attachment: fixed;
}
.login {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
}
.title {
  margin: 0px auto 30px auto;
  text-align: center;
  color: #707070;
  font-size: 30px;
  padding-left: 25px;
}

.login-form {
  border-radius: 6px;
  background: #ffffff;
  width: 400px;
  padding: 25px 60px 25px 60px;
  .el-input {
    height: 38px;
    input {
      height: 38px;
    }
  } 
  .input-icon {
    height: 39px;
    width: 14px;
    margin-left: 2px;
  }
}
.login-code-input {
  float: left;
  width: 66%;
}
.login-tip {
  font-size: 13px;
  text-align: center;
  color: #bfbfbf;
}
.login-code {
  width: 30%;
  padding-left: 10px;
  height: 38px;
  float: right;
  img {
    cursor: pointer;
    vertical-align: middle;
  }
}
.el-login-footer {
  height: 40px;
  line-height: 40px;
  position: fixed;
  bottom: 0;
  width: 100%;
  text-align: center;
  color: #fff;
  font-family: Arial;
  font-size: 12px;
  letter-spacing: 1px;
}
.login-code-img {
  height: 38px;
}
.el-button{
  width: 100%;
}
.register-class{
    text-align: right;
    padding-bottom: 10px;
    font-size: 17px;
}
</style>