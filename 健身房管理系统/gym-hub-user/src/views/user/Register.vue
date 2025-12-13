<template>
  <div class="register-main">
    <div class="login">
      <el-form :model="registerForm" label-width="120px" class="login-form" :rules="arules" ref="registerFormRule">
        <h3 class="title">注册</h3>
        <el-form-item label="手机号：" prop="phone">
          <el-input v-model="registerForm.phone" placeholder="请输入手机号"></el-input>
        </el-form-item>
        <el-form-item label="姓名：" prop="name">
          <el-input v-model="registerForm.name" placeholder="请输入姓名"></el-input>
        </el-form-item>
        <el-form-item label="密码：" prop="password">
          <el-input v-model="registerForm.password" placeholder="请输入密码" show-password></el-input>
        </el-form-item>
        <el-form-item label="确认密码：" prop="repassword">
          <el-input v-model="registerForm.repassword" placeholder="请输入确认密码" show-password></el-input>
        </el-form-item>
        <el-form-item label="年龄：" prop="age">
          <el-input-number v-model="registerForm.age" :min="1" :max="127" />
        </el-form-item>
        <el-form-item label="性别：" prop="sex">
          <el-radio v-model="registerForm.sex" label="1">男</el-radio>
          <el-radio v-model="registerForm.sex" label="2">女</el-radio>
        </el-form-item>
        <el-form-item style="width:90%;">
          <el-button v-if="!loading" type="primary" @click="onSubmit">注册</el-button>
          <el-button v-else type="primary">注册中...</el-button>
        </el-form-item>
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
    let registerFormRule = ref(null)
    let registerForm = reactive({
      password: '',
      repassword: '',
      age: 1,
      name: '',
      phone: '',
      address: '',
      sex: '1'
    })

    let arules = reactive({
      phone: [
        { required: true, trigger: ["blur", "change"], message: "请输入正确的手机号"},
        { pattern: /^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\d{8}$/, message: '请输入正确的手机号码' }
      ],
      name: [{ required: true, trigger: ["blur", "change"], message: "请输入您的姓名" }],
      password: [{ required: true, trigger: ["blur", "change"], message: "请输入密码" }],
      repassword: [{ required: true, trigger: ["blur", "change"], message: "请输入确认密码" }],
      age: [{ required: true, trigger: ["blur", "change"], message: "请选择年龄" }],
      sex: [{ required: true, trigger: ["blur", "change"], message: "请输入性别" }],
    })

    function onSubmit() {
      registerFormRule.value.validate((valid) => {
        if(valid) {
            if(registerForm.password != registerForm.repassword) {
              ElMessage({ message: '两次密码输入不一致',type: 'error'})
              return false
            }
            post('user/register', registerForm).then(res => {
            ElMessage({
              message: '注册成功',
              type: 'success',
            })
            router.push({path:'/login'});
            setTimeout(function () {
              window.location.reload();
            }, 10);
          })
        } else {
          return false
        }
      })
      
    }

    return {
      loading,
      arules,
      registerForm,
      registerFormRule,
      onSubmit,
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.register-main {
  background-image: url("../../assets/images/background.jpg");
  width: 100vw;
  height: 100vh;
  background-color: #707070;
  background-size: cover;
  background-position: center;
  background-attachment: fixed;
  display: flex;
  justify-content: center;
  align-items: center;
}

.login {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
}
.register-main .login .login-form h3 {
  margin: 0px auto;
  padding-left: 35px;
  text-align: center;
  color: #707070;
  padding-bottom: 20px;
  font-size: 30px;
}

.login-form {
  border-radius: 6px;
  background: #ffffff;
  width: 400px;
  padding: 25px 60px 5px 25px;
  
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

.login-tip {
  font-size: 13px;
  text-align: center;
  color: #bfbfbf;
}
.login-code {
  width: 33%;
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
}
</style>