<template>
  <div class="all">
    <GlobalNavbar />
    <el-row class="login-container" style="display: flex; justify-content: center">
      <el-col :span="24" :xl="6" :lg="7" class="login-col">
        <div class="login-form">
          <div class="title-and-image">
            <img src="../assets/logo2.png" alt="Your Image" class="login-image" />
            <h4 class="login-title">迎新身份认证</h4>
          </div>
          <!-- <h4 class="login-title2">账号登录</h4> -->
          <el-form :model="loginForm" :rules="rules" ref="loginForm" label-width="0">
            <el-form-item prop="username">
              <el-input
                v-model="loginForm.username"
                placeholder="请输入学号/工号"
                maxlength="12"
                class="center-input"
              >
                <template slot="prefix">
                  <i class="el-icon-user"></i>
                </template>
              </el-input>
            </el-form-item>

            <el-form-item prop="password">
              <el-input
                v-model="loginForm.password"
                type="password"
                placeholder="请输入密码"
                class="center-input"
                show-password
              >
                <!-- 小眼睛开关显示密码 show-password-->
                <template slot="prefix">
                  <i class="el-icon-lock"></i>
                </template>
              </el-input>
            </el-form-item>

            <el-form-item prop="code">
              <div class="captcha-container">
                <el-input
                  v-model="loginForm.code"
                  maxlength="5"
                  placeholder="请输入验证码"
                  style="width: 200px"
                >
                  <template slot="prefix"> <i class="el-icon-thumb"></i> </template
                ></el-input>

                <el-image
                  :src="captchaImg"
                  class="captchaImg"
                  @click="debouncedGetCaptcha"
                  style="width: 180px"
                ></el-image>
              </div>
            </el-form-item>

            <div style="display: flex; justify-content: flex-end; margin-top: 0px">
              <el-button
                type="text"
                style="margin-top: 3px; color: white"
                @click="helpweb"
                >在线帮助</el-button
              >
              <el-button type="text" style="margin-top: 0px; color: white; size: 10px"
                >|</el-button
              >
              <el-button
                type="text"
                style="margin-right: 80px; margin-top: 3px; color: white"
                @click="showForgetPasswordDialog"
                >忘记密码</el-button
              >
            </div>
            <el-button
              type="primary"
              @click="debouncedSubmit('loginForm')"
              style="
                background-color: #276e51;
                width: 350px;
                margin: 0 auto;
                margin-top: 6px;
                letter-spacing: 20px;
              "
            >
              登录
            </el-button>
            <el-dialog
              title="忘记密码"
              :visible.sync="forgetPasswordDialogVisible"
              width="400px"
              :style="{
                'border-radius': '20px',
                'box-shadow': '0 0 10px rgba(0, 0, 0, 0.2)',
              }"
            >
              <el-form
                :model="findPasswordForm"
                ref="findPasswordForm"
                :rules="rules"
                label-width="60px"
              >
                <el-form-item label="邮箱" prop="email">
                  <el-input
                    prefix-icon="el-icon-message"
                    placeholder="请填写邮箱"
                    maxlength="32"
                    v-model="findPasswordForm.email"
                  ></el-input>
                </el-form-item>

                <el-form-item label="新密码" prop="password1">
                  <el-input
                    prefix-icon="el-icon-lock"
                    placeholder="请填写 6-18 位密码，必须包含字母"
                    type="password"
                    maxlength="18"
                    v-model="findPasswordForm.password1"
                    show-password
                    :rules="validatePassword1"
                  ></el-input>
                </el-form-item>

                <el-form-item label="验证码" prop="code">
                  <el-input
                    prefix-icon="el-icon-key"
                    placeholder="请填写6位数字验证码"
                    type="number"
                    maxlength="6"
                    v-model="findPasswordForm.code"
                  >
                    <el-button
                      slot="append"
                      @click="sendEmailCode()"
                      :disabled="disabled"
                      >{{ msg }}</el-button
                    >
                  </el-input>
                </el-form-item>
              </el-form>
              <span slot="footer" class="dialog-footer">
                <el-button
                  type="primary"
                  style="
                    background-color: #276e51;
                    width: 350px;
                    margin: 0 auto;
                    margin-top: 6px;
                    letter-spacing: 20px; /* 调整字母间距 */
                  "
                  @click="submitFindPassword('findPasswordForm')"
                  >提交确认</el-button
                >

                <!-- <el-button @click="forgetPasswordDialogVisible = false">取消</el-button> -->
              </span>
            </el-dialog>
          </el-form>
        </div>
      </el-col>
    </el-row>

    <div class="footer">
      <p>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;©
        All Right Reserved . 四川师范大学 版权所有
      </p>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import _ from "lodash"; // 引入 lodash 库
import qs from "qs";
export default {
  name: "Login",
  data() {
    var email = (rule, value, callback) => {
      if (!value) {
        return callback(new Error("请输入邮箱"));
      } else if (!/^([a-zA-Z0-9]+[-_\.]?)+@[a-zA-Z0-9]+\.[a-z]+$/.test(value)) {
        return callback(new Error("请输入正确的邮箱"));
      } else {
        callback();
      }
    };
    var password = (rule, value, callback) => {
      if (!value) {
        return callback(new Error("请输入密码"));
      } else if (!/(?=.*[a-zA-Z])[a-zA-Z0-9]{6,18}/.test(value)) {
        return callback(
          new Error("密码长度在6-18个字符，只能包含数字、大小写字母 且 至少包含一个字母")
        );
      } else {
        callback();
      }
    };
    var code = (rule, value, callback) => {
      if (!value) {
        return callback(new Error("请输入5位验证码"));
      } else if (value.length !== 5) {
        return callback(new Error("请输入5位验证码"));
      } else {
        callback();
      }
    };
    return {
      permissionCode: "",
      isCode: false,
      // 倒计时
      disabled: false,
      msg: "点击获取验证码",
      count: 60,
      timer: 0,
      // 按钮加载
      loading: false,
      loginForm: {
        email: "",
        password: "",
      },
      // 找回密码
      dialogFormVisible: false,
      findPasswordForm: {
        email: "",
        password1: "",
        code: "",
      },
      rules: {
        email: [{ validator: email, trigger: "blur" }],
        password: [{ validator: password, trigger: "blur" }],
        code: [{ validator: code, trigger: "blur" }],
      },
      forgetPasswordDialogVisible: false,
      loginForm: {
        username: "2021110335", //admin
        password: "123456", //888888
        code1: "", //验证码
        token: "",
      },
      rules: {
        username: [
          { required: true, message: "请输入学号/工号", trigger: "blur" },
          { min: 3, message: "最小输入4位", trigger: "blur" },
          {
            validator: this.validateAlphanumeric1,
            trigger: "blur",
          },
        ],
        password: [
          { required: true, message: "请输入密码", trigger: "blur" },
          {
            validator: this.validateAlphanumeric2,
            trigger: "blur",
          },
        ],
        code1: [
          { required: true, message: "请输入验证码", trigger: "blur" },
          { min: 5, max: 5, message: "长度为 5 个字符", trigger: "blur" },
          {
            validator: this.validateAlphanumeric3,
            trigger: "blur",
          },
        ],
      },
      captchaImg: null,
    };
  },
  methods: {
    //更改密码校验
    validatePassword1(rule, value, callback) {
      if (!value) {
        callback(new Error("请输入密码"));
      } else if (value.length < 6 || value.length > 18) {
        callback(new Error("密码长度应为6-18位"));
      } else if (!/^(?=.*[a-zA-Z])/.test(value)) {
        callback(new Error("密码必须包含字母"));
      } else {
        // 通过验证
        callback();
      }
    },
    //在线帮助跳转在线文档
    helpweb() {
      window.open("https://docs.qq.com/doc/DR3VxRmpoSXN3TkpT", "_blank");
    },

    // 修改密码
    submitFindPassword(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.loading = true;
          // 请求
          axios
            .post("/sys/user/findPassword", this.findPasswordForm)

            .then((response) => {
              console.log(this.findPasswordForm.password1);
              // 假设响应数据包含成功信息
              if (response.data) {
                // 请求成功
                this.$message({
                  message: "密码修改成功",
                  type: "success",
                });
                this.dialogFormVisible = false;
              }
            })
            .finally(() => {
              this.loading = false;
            });
        }
      });
    },
    sendEmailCode() {
      this.$refs.findPasswordForm.validateField("email", (result) => {
        if (!result) {
          // 按钮倒计时
          this.disabled = true;
          this.msg = this.count-- + "s后重新获取";
          this.timer = setInterval(() => {
            this.msg = this.count-- + "s后重新获取";
            if (this.count < 0) {
              this.msg = "点击获取验证码";
              this.count = 60;
              this.disabled = false;
              clearInterval(this.timer);
            }
          }, 1000);

          // 发送验证码请求
          axios
            .post("/common/code/request", { email: this.findPasswordForm.email })
            .then((response) => {
              // 假设响应数据包含权限码
              this.permissionCode = response.data.data.permissionCode;
              console.log(this.permissionCode); // 确保你能够正确获取权限码
              axios
                .post("/common/code/email", {
                  email: this.findPasswordForm.email,
                  code: this.permissionCode,
                })
                .then((_) => {
                  console.log(this.permissionCode);
                  console.log(this.findPasswordForm.email);

                  if (_) {
                    // 通知邮箱发送
                    this.$notify({
                      title: "邮箱验证码已发送",
                      message: "验证码有效时长5分钟，失效请重新发送",
                      type: "success",
                      duration: 10 * 1000,
                    });
                  }
                });
            });
        }
      });
    },

    //对所有用户输入数据进行验证和过滤，确保输入符合预期的格式、长度和类型。防止sql注入
    validateAlphanumeric1(rule, value, callback) {
      const regExp = /^[A-Za-z0-9]+$/;
      const unsafeKeywords = [
        "SELECT",
        "select",
        "INSERT",
        "insert",
        "DELETE",
        "delete",
        "DROP",
        "drop",
        "UPDATE",
        "update",
      ]; // 可能引发安全问题的SQL关键词

      if (!regExp.test(value)) {
        callback(new Error("只能包含数字和字母"));
      } else if (unsafeKeywords.some((keyword) => value.includes(keyword))) {
        callback(new Error("包含不安全的关键词"));
      } else {
        callback();
      }
    },
    validateAlphanumeric2(rule, value, callback) {
      const regExp = /^[A-Za-z0-9]+$/;
      const unsafeKeywords = [
        "SELECT",
        "select",
        "INSERT",
        "insert",
        "DELETE",
        "delete",
        "DROP",
        "drop",
        "UPDATE",
        "update",
      ]; // 可能引发安全问题的SQL关键词

      if (!regExp.test(value)) {
        callback(new Error("只能包含数字和字母"));
      } else if (unsafeKeywords.some((keyword) => value.includes(keyword))) {
        callback(new Error("包含不安全的关键词"));
      } else {
        callback();
      }
    },
    validateAlphanumeric3(rule, value, callback) {
      const regExp = /^[A-Za-z0-9]+$/;
      const unsafeKeywords = [
        "SELECT",
        "select",
        "INSERT",
        "insert",
        "DELETE",
        "delete",
        "DROP",
        "drop",
        "UPDATE",
        "update",
      ]; // 可能引发安全问题的SQL关键词

      if (!regExp.test(value)) {
        callback(new Error("只能包含数字和字母"));
      } else if (unsafeKeywords.some((keyword) => value.includes(keyword))) {
        callback(new Error("包含不安全的关键词"));
      } else {
        callback();
      }
    },

    showForgetPasswordDialog() {
      this.forgetPasswordDialogVisible = true;
    },

    resetPassword() {
      // 在这里处理重置密码的逻辑
      // 可以在确认按钮点击后执行操作
      this.forgetPasswordDialogVisible = false; // 关闭对话框
    },

    //防止不停点击登录按钮，降低服务器压力和负载,引入 lodash 库
    debouncedSubmit: _.debounce(function (formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.$axios
            .post("/login?" + qs.stringify(this.loginForm))
            .then((res) => {
              console.log(res);
              const jwt = res.headers["authorization"];
              this.$store.commit("SET_TOKEN", jwt);
              this.$router.push("/index");
              this.$message.success("登录成功");
            })
            .catch(() => {
              // 处理登录错误并立即刷新验证码
              this.debouncedGetCaptcha();
            });
        } else {
          console.log("error submit!!");
          this.debouncedGetCaptcha();
          return false;
        }
      });
    }, 280), // 设置防抖时间为500毫秒
    // resetForm(formName) {
    //   this.$refs[formName].resetFields();
    // },

    debouncedGetCaptcha: _.debounce(function () {
      this.$axios.get("/captcha").then((res) => {
        this.loginForm.token = res.data.data.token;
        this.captchaImg = res.data.data.captchaImg;
        this.loginForm.code1 = "";
      });
    }, 280), // 设置防抖时间为 500毫秒
  },
  created() {
    // 在组件创建时调用防抖后的 getCaptcha 方法来获取验证码
    this.debouncedGetCaptcha();
  },
};
</script>

<style scoped>
.captcha-container {
  /* 验证码 */
  display: flex;
  align-items: center;
  width: 350px; /* 设置容器的总宽度，适应输入框和验证码图片的宽度 */
  margin: 0 auto; /* 可以根据需要调整容器的水平位置 */
}

.all {
  background-color: rgba(255, 255, 255, 0.5);
  background-image: url("@/assets/pc-bg.png");
  background-size: cover;
  background-repeat: no-repeat;
  background-position: center;
}

.login-container {
  width: 100%;
  height: 88vh; /* 默认高度 */
  display: flex;
  align-items: center;
  justify-content: center;
}

.login-col {
  /* ... */
  margin: center; /* 根据需要调整值 */
}

.login-form {
  background-color: rgba(0, 0, 0, 0.6);
  padding: 40px; /* 增大内边距 */
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2); /* 增大阴影 */
  width: 500px; /* 增大宽度 */
  margin: 0 auto;
  text-align: center;
}

.title-and-image {
  display: flex;
  align-items: center;
  flex-direction: row; /* 这是默认值，通常不需要额外设置 */
  /* 可选：如果需要在水平方向上居中对齐子元素，可以使用以下属性 */
  justify-content: center;
}

.login-image {
  width: 200px; /* 设置图片宽度 */
  margin-right: 60px; /* 调整标题和图片之间的间距 */
}

.login-title {
  font-size: 30px;
  margin-bottom: 20px;
  color: white;
  font-weight: 400; /* 使用关键词 'bold' 表示粗体 */
}

.center-input {
  width: 350px; /* 自定义输入框宽度 */
}

.captchaImg {
  margin-left: 20px;
  border-radius: 4px;
  cursor: pointer;
}
.footer {
  text-align: center; /* 文本居中 */
  color: white; /* 字体颜色为白色 */
  padding: 10px 0; /* 可以根据需要设置顶部和底部的内边距 */
  margin-top: -20px; /* 向上移动 20 像素，根据需要进行调整 */
  margin: 0 500px;
}
</style>
