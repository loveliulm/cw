<template>
  <div class="login-body">
    <div class="login-top-bar">
      <div class="login-topbar-header">
        <a class="lgoin-topbar-header-log" href="/" target="_self">
          <img class="login-logo" src="../assets/image/logo.png" alt="企万慧">
        </a>
        <h2 class="login-topbar-title">企万慧云财务</h2>
        <a href="http://www.jhqkg.com" target="_blank" class="lgoin-sub-topic-text">官方网站</a>
        <a @click="showImage(imageStatus)" class="lgoin-sub-topic-text">联系我们</a>
      </div>
      <img @click="showImage(imageStatus)" v-if="imageStatus" class="contact" src="../assets/image/contactus.jpg" alt="联系我们">
    </div>
    <div class="login-main-content">
      <div class="login-main-text"></div>
      <div class="root-login">
        <div class="login-title">
          企万慧云财税
        </div>
        <div v-if="isLogin">
          <div v-if="loginType === 1" class="root-login-account">
            <p class="loginByPwd selectClass" @click="changeLoginType(1)">密码登录</p><p class="loginByCode" @click="changeLoginType(2)">短信登录</p>
          </div>
          <div v-else class="root-login-account">
            <p class="loginByPwd" @click="changeLoginType(1)">密码登录</p><p class="loginByCode selectClass" @click="changeLoginType(2)">短信登录</p>
          </div>
          <div v-if="loginType === 1">
            <el-form ref="loginForm" :model="loginForm" :rules="rules" label-position="left" label-width="0px">
              <el-form-item prop="username">
                <el-input v-model="loginForm.username" type="text" auto-complete="off" placeholder="请输入账号">
                  <svg-icon slot="prefix" icon-class="user" style="height: 45px;width: 14px;"/>
                </el-input>
              </el-form-item>
              <el-form-item prop="password">
                <el-input v-model="loginForm.password" type="password" auto-complete="off" placeholder="请输入密码">
                  <svg-icon slot="prefix" icon-class="password" style="height: 45px;width: 14px;"/>
                </el-input>
              </el-form-item>
              <el-form-item prop="code">
                <el-input v-model="loginForm.code" auto-complete="off" placeholder="请输入验证码" style="width: 63%" @keyup.enter.native="handleLogin">
                  <svg-icon slot="prefix" icon-class="validCode" style="height: 45px;width: 14px;"/>
                </el-input>
                <div class="login-code">
                  <img :src="codeUrl" @click="getCode">
                </div>
              </el-form-item>
              <el-form-item style="width:100%;">
                <el-button :loading="loading" size="medium" class="root-login-btn" style="width:100%;" @click.native.prevent="handleLogin">
                  <span v-if="!loading">登 录</span>
                  <span v-else>登 录 中...</span>
                </el-button>
              </el-form-item>

              <div style="display: flex; justify-content: space-between;">
                <a class="to-other" @click="toRegister">立即注册</a>
                <a class="to-other" @click="toUpdate">忘记密码</a>
              </div>

              <div style="font-size: 14px; margin-top: 30px;color: #9b9b9b;text-align: center">
                <input type="checkbox" v-model="agreeChecked" style="vertical-align:middle;">
                <a @click="openAgreement" style="vertical-align:middle">登录即同意《用户服务协议》</a>
              </div>
            </el-form>
          </div>
          <div v-else>
            <el-form ref="phoneLoginForm" :model="phoneLoginForm" :rules="rules" label-position="left" label-width="0px">
              <el-form-item prop="phonenumber">
                <el-input v-model="phoneLoginForm.phonenumber" type="text" auto-complete="off" placeholder="请输入手机号">
                  <svg-icon slot="prefix" icon-class="phone" style="height: 45px;width: 14px;"/>
                </el-input>
              </el-form-item>
              <el-form-item prop="phonecode">
                <el-input v-model="phoneLoginForm.phonecode" auto-complete="off" placeholder="请输入手机验证码" style="width:63%;" @keyup.enter.native="handleLogin">
                  <svg-icon slot="prefix" icon-class="password" style="height: 45px;width: 14px;"/>
                </el-input>
                <el-button class="login-code" :loading="codeLoading" :disabled="isDisabled" size="small" @click="sendSmsCodeL">{{ buttonName }}</el-button>
              </el-form-item>
              <el-form-item style="width:100%;">
                <el-button :loading="loading" size="medium" class="root-login-btn" style="width:100%;" @click.native.prevent="handleLogin">
                  <span v-if="!loading">登 录</span>
                  <span v-else>登 录 中...</span>
                </el-button>
              </el-form-item>

              <div style="display: flex; justify-content: space-between;">
                <a class="to-other" @click="toRegister">立即注册</a>
                <a class="to-other" @click="toUpdate">忘记密码</a>
              </div>

              <div style="font-size: 14px; margin-top: 30px;color: #9b9b9b;text-align: center">
                <input type="checkbox" v-model="agreeChecked" style="vertical-align:middle;">
                <a @click="openAgreement" style="vertical-align:middle">登录即同意《用户服务协议》</a>
              </div>
            </el-form>
          </div>
        </div>
        <div v-if="isRegister">
          <el-form ref="registerForm" :model="registerForm" :rules="rules" label-position="left" label-width="0px" class="root-account">
            <el-form-item prop="deptname">
              <el-input v-model="registerForm.deptname" type="text" auto-complete="off" placeholder="请输入公司名称">
                <svg-icon slot="prefix" icon-class="peoples" style="height: 45px;width: 14px;"/>
              </el-input>
            </el-form-item>
            <el-form-item prop="phonenumber">
              <el-input v-model="registerForm.phonenumber" type="text" auto-complete="off" placeholder="请输入手机号">
                <svg-icon slot="prefix" icon-class="phone" style="height: 45px;width: 14px;"/>
              </el-input>
            </el-form-item>
            <el-form-item prop="phonecode">
              <el-input v-model="registerForm.phonecode" auto-complete="off" placeholder="请输入验证码" style="width:63%;">
                <svg-icon slot="prefix" icon-class="validCode" style="height: 45px;width: 14px;"/>
              </el-input>
              <el-button class="login-code" :loading="codeLoading" :disabled="isDisabled" size="small" @click="sendSmsCodeR">
                {{ buttonName }}
              </el-button>
            </el-form-item>
            <el-form-item prop="username">
              <el-input v-model="registerForm.username" type="text" auto-complete="off" placeholder="请输入账号">
                <svg-icon slot="prefix" icon-class="user" style="height: 45px;width: 14px;"/>
              </el-input>
            </el-form-item>
            <el-form-item prop="password">
              <el-input v-model="registerForm.password" type="password" auto-complete="off" placeholder="请输入密码">
                <svg-icon slot="prefix" icon-class="password" style="height: 45px;width: 14px;"/>
              </el-input>
            </el-form-item>
            <el-form-item style="width:100%;">
              <el-button :loading="loading" size="medium" type="primary" class="root-login-btn" @click.native.prevent="handleRegister">
                <span v-if="!loading">立 即 注 册</span>
                <span v-else>注 册 中...</span>
              </el-button>
            </el-form-item>

            <div style="display: flex; justify-content: space-between;">
              <a class="to-other" @click="toLogin">返回登录</a>
            </div>
          </el-form>
        </div>
        <div v-if="isUpdate">
          <el-form ref="forgetForm" :model="forgetForm" :rules="rules" label-position="left" label-width="0px" class="root-account">
            <el-form-item prop="phonenumber">
              <el-input v-model="forgetForm.phonenumber" type="text" auto-complete="off" placeholder="请输入手机号">
                <svg-icon slot="prefix" icon-class="user" style="height: 45px;width: 14px;"/>
              </el-input>
            </el-form-item>
            <el-form-item prop="phonecode">
              <el-input v-model="forgetForm.phonecode" auto-complete="off" placeholder="请输入验证码" style="width:63%;">
                <svg-icon slot="prefix" icon-class="validCode" style="height: 45px;width: 14px;"/>
              </el-input>
              <el-button class="login-code" :loading="codeLoading" :disabled="isDisabled" size="small" @click="sendSmsCodeU">{{ buttonName }}</el-button>
            </el-form-item>
            <el-form-item prop="password">
              <el-input v-model="forgetForm.password" type="password" auto-complete="off" placeholder="请输入新密码">
                <svg-icon slot="prefix" icon-class="password" style="height: 45px;width: 14px;"/>
              </el-input>
            </el-form-item>
            <el-form-item prop="repeatpassword">
              <el-input v-model="forgetForm.repeatpassword" type="password" auto-complete="off" placeholder="请再次输入新密码">
                <svg-icon slot="prefix" icon-class="password" style="height: 45px;width: 14px;"/>
              </el-input>
            </el-form-item>

            <el-form-item style="width:100%;">
              <el-button :loading="loading" size="medium" type="primary" class="root-login-btn" @click.native.prevent="handleForgetPassword">
                <span v-if="!loading">修 改 密 码</span>
                <span v-else>修 改 中...</span>
              </el-button>
            </el-form-item>

            <div style="display: flex; justify-content: space-between;">
              <a class="to-other" @click="toLogin">返回登录</a>
            </div>
          </el-form>
        </div>
      </div>
      <UserAgreement ref="form"/>
    </div>
    <!--  底部  -->
    <div class="el-login-footer">
      <span>官网首页 隐私政策 联系我们 2023 qwh365.com All Rights Reserved Powered by 金华企客管网络科技有限公司 企万慧云财税</span>
    </div>

  </div>
</template>

<script>
import { getCodeImg, sendSmsCodeL, sendSmsCodeR, register, sendSmsCodeU, updatePassword } from "@/api/login";
import Cookies from "js-cookie";
import { encrypt, decrypt } from '@/utils/jsencrypt';
import { isvalidPhone } from '@/utils/validate';
import UserAgreement from './UserAgreement'

export default {
  name: "Login",
  components: { UserAgreement },
  data () {
    const validatePhone = (rule, value, callback) => {
      if (value === '' || value === undefined || value == null) {
        callback(new Error('请输入手机号码'))
        this.isDisabled = true
      } else if (isvalidPhone(value)) {
        callback()
        if (this.time === 60) {
          this.isDisabled = false
        }
      } else {
        callback(new Error('请输入正确的手机号码'))
        this.isDisabled = true
      }
    }
    const ConfirmPassword = (rule, value, callback) => {
      if (value !== this.forgetForm.password) {
        callback(new Error('两次输入密码不一致'))
      } else {
        callback()
      }
    }
    return {
      codeUrl: "",
      loginForm: {
        username: '',
        password: '',
        code: '',
        uuid: ''
      },
      phoneLoginForm: {
        phonenumber: '',
        phonecode: '',
      },
      registerForm: {
        deptname: '',
        phonenumber: '',
        phonecode: '',
        username: '',
        password: '',
      },
      forgetForm: {
        phonenumber: '',
        phonecode: '',
        password: '',
        repeatpassword: '',
      },
      rules: {
        username: [{required: true, message: '请输入用户名', trigger: 'blur'}],
        password: [{ required: true, trigger: 'blur', message: '请输入密码' }],
        deptname: [{ required: true, trigger: 'blur', message: '请输入公司名称' }],
        repeatpassword: [{ required: true, trigger: 'blur', message: '请再次输入密码' },
          { trigger: 'blur', validator: ConfirmPassword }]
        ,
        code: [{ required: true, trigger: 'blur', message: '请输入验证码' }],
        phonenumber: [{required: true, validator: validatePhone, trigger: 'blur'}],
        phonecode: [{ required: true, trigger: 'blur', message: '请输入手机验证码' }],
      },
      loading: false,
      redirect: undefined,
      codeLoading: false,
      isDisabled: true,
      time: 60,
      buttonName: '获取验证码',
      loginType: 1,
      agreeChecked: true,
      imageStatus: false,
      isLogin: true,
      isRegister: false,
      isUpdate: false,
    };
  },
  watch: {
    $route: {
      handler: function (route) {
        this.redirect = route.query && route.query.redirect;
      },
      immediate: true
    }
  },
  created () {
    this.getCode();
  },
  methods: {
    changeLoginType(type) {
      this.loginType = type
      if (type === 1) {
        this.$refs['phoneLoginForm'].clearValidate();
      } else {
        this.$refs['loginForm'].clearValidate();
      }
    },
    getCode () {
      getCodeImg().then(res => {
        // this.codeUrl = "data:image/gif;base64," + res.img;
        this.codeUrl = res.img;
        this.loginForm.uuid = res.uuid;
      });
    },
    sendSmsCodeL() {
      if (this.phoneLoginForm.phonenumber && !this.isDisabled) {
        this.params = {'phonenumber': this.phoneLoginForm.phonenumber}
        sendSmsCodeL(this.params).then(res => {
          if (res.code === 200) {
            this.$message.success(res.msg)
            this.setDisabled(true);
            this.timer();
          } else {
            this.codeLoading = false
            return false;
          }
        }).catch(err => {})
      } else {
        return false;
      }
    },
    sendSmsCodeR() {
      if (this.registerForm.phonenumber && !this.isDisabled) {
        this.params = {'phonenumber': this.registerForm.phonenumber}
        sendSmsCodeR(this.params).then(res => {
          if (res.code === 200) {
            this.$message.success(res.msg)
            this.setDisabled(true);
            this.timer();
          } else {
            this.codeLoading = false
            return false;
          }
        }).catch(err => {})
      } else {
        return false;
      }
    },
    sendSmsCodeU() {
      if (this.forgetForm.phonenumber && !this.isDisabled) {
        this.params = {'phonenumber': this.forgetForm.phonenumber}
        sendSmsCodeU(this.params).then(res => {
          if (res.code === 200) {
            this.$message.success(res.msg)
            this.setDisabled(true);
            this.timer();
          } else {
            this.codeLoading = false
            return false;
          }
        }).catch(err => {})
      } else {
        return false;
      }
    },
    // 倒计时
    timer() {
      if (this.time > 0) {
        this.buttonName = this.time + '秒后重新发送';
        this.time--;
        setTimeout(this.timer, 1000);
      } else {
        // 设置disable的值
        this.setDisabled(false);
        this.time = 60;
        this.buttonName = '获取验证码';
      }
    },

    // 设置disable的值
    setDisabled(val) {
      this.isDisabled = val;
    },

    // 账号登录
    handleLogin () {
      if (!this.agreeChecked) {
        this.$message.warning({
          title: '提示',
          message: '请阅读并同意用户服务协议'
        })
        return false;
      }
      if (this.loginType === 1) {
        this.$refs.loginForm.validate(valid => {
          if (valid) {
            this.loading = true;
            this.$store.dispatch("Login", this.loginForm)
              .then(() => {
                this.$router.push({ path: this.redirect || "/" });
              })
              .catch(() => {
                this.loading = false;
                this.getCode();
              });
          }
        });
      }
      // 手机验证码登录
      if (this.loginType === 2) {
        this.$refs.phoneLoginForm.validate(valid => {
          if (valid) {
            this.loading = true;
            this.$store.dispatch("PhoneLogin", this.phoneLoginForm)
              .then(() => {
                this.$router.push({ path: this.redirect || "/" });
              })
              .catch(() => {
                this.loading = false;
              });
          }
        });
      }
    },

    // 注册接口
    handleRegister() {
      var that = this;
      this.$refs.registerForm.validate(valid => {
        if (valid) {
          that.loading = true;
          register(this.registerForm).then(res => {
            this.$message.success('注册成功，请前往登录')
            setTimeout(function () {
              location.reload()
              that.loading = false;
            }, 1500)
          }).catch(error => {
            this.loading = false;
          })
        }
      });
    },

    // 修改密码接口
    handleForgetPassword() {
      var that = this;
      this.$refs.forgetForm.validate(valid => {
        if (valid) {
          that.loading = true;
          updatePassword(this.forgetForm).then(res => {
            this.$message.success('密码修改成功，请前往登录')
            setTimeout(function () {
              location.reload()
              that.loading = false;
            }, 1500)
          }).catch(error => {
            this.loading = false;
          })
        }
      });
    },

    toLogin () {
      this.isLogin = true;
      this.isRegister = false;
      this.isUpdate = false;
    },
    toRegister () {
      this.isLogin = false;
      this.isRegister = true;
      this.isUpdate = false;
    },
    toUpdate () {
      this.isLogin = false;
      this.isRegister = false;
      this.isUpdate = true;
    },

    openAgreement(){
      const _this = this.$refs.form
      _this.dialog = true
    },

    showImage(imageStatus) {
      this.imageStatus = imageStatus !== true;
    }
  }
};
</script>

<style rel="stylesheet/scss" lang="scss">
.login-body {
  min-height: 100vh;
  width: 100%;
  background-image: url("../assets/image/logo_bg.png");
  position: relative;
  background-size: cover;
}

.login-top-bar {
  background-color: #fff;
  height: 80px;
  width: 100%;
  box-shadow: 0 2px 4px 0 rgba(0, 0, 0, 0.1);
  box-sizing: border-box;
}

.login-topbar-header {
  position: relative;
  max-width: 1200px;
  margin: 0 auto;
  padding: 18px 0;
  height: 80px;
}

.lgoin-topbar-header-log {
  display: inline-block;
  margin-right: 10px;
  width: 110px;
}

.login-logo {
  width: 110px;
  height: 45px
}

.login-topbar-title {
  display: inline-block;
  padding: 0 15px;
  font-size: 20px;
  border-left: 1px solid #999;
  top: 50%;
  transform: translateY(-60%);
  font-weight: initial;
}

.lgoin-sub-topic-text {
  float: right;
  line-height: 45px;
  font-size: 14px;
  color: #73777a;
  margin-right: 20px;
}

.contact {
  left: 71%;
  z-index: 2000;
  position: absolute;
  transition: 0.2s;
  width: 230px;
  box-shadow: 0px 0px 20px 0px rgba(179, 179, 179, 0.3);
}

.login-main-content {
  max-width: 1200px;
  min-height: 800px;
  min-height: calc(100vh - 81px - 89px);
  margin: 0 auto;
  display: flex;
  justify-content: center;
  align-items: center;
  box-sizing: border-box;
}

.login-main-text {
  box-sizing: border-box;
  width: 600px;
  height: 500px;
  background-image: url("../assets/image/dashboard.png");
  background-color: #fff;
  box-shadow: 0px 2px 10px 0px rgba(188, 202, 224, 0.25);
  background-size: cover;
}

.root-login {
  position: relative;
  width: 380px;
  height: 500px;
  background-color: #fff;
  font-size: 14px;
  border-color: #e4e4e4;
  overflow: hidden;
  box-shadow: 0px 2px 10px 0px rgba(188, 202, 224, 0.25);
  padding: 0 25px 20px 25px;
  .el-input {
    height: 45px;
    input {
      height: 45px;
    }
  }
}

.login-title {
  font-size: 25px;
  text-align: center;
  margin: 20px 0 0 0;
  font-weight: bold;
}

.root-login-account p {
  color: #999999;
  font-size: 18px !important;
  font-weight: 400 !important;
  font-style: normal;
  text-align: center;
  line-height: 50px;
  box-sizing: border-box;
  width: 50%;
  cursor: pointer;
  display: inline-block;
}

.selectClass {
  color: #333333 !important;
  border-bottom: 2px solid #006eff !important;
}

.root-account {
  margin-top: 25px;
}

.login-code {
  width: 33%;
  display: inline-block;
  height: 45px;
  float: right;
  img{
    cursor: pointer;
    vertical-align:middle
  }
}

.to-other {
  font-size: 14px;
  color: #9b9ea0;
  &:hover {
    color: #f60 !important;
  }
}

.root-login-btn {
  height: 40px;
  background-color: #006dfd;
  border-radius: 20px;
  color: #fff;
  cursor: default;
  width: 100%;
}
.root-login-btn:hover, .root-login-btn:focus {
  background-color: #006dfd;
  border-radius: 20px;
  color: #fff;
}

.el-login-footer {
  text-align: center;
  font-size: 12px;
  color: #777;
  margin-top: 16px;
}
</style>
