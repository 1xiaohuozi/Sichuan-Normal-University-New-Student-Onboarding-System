<template>
  <div class="form-container">
    <h2 class="greeting">你好！{{ userInfo.name }} 同学</h2>

    <el-form
      :model="passForm"
      status-icon
      :rules="rules"
      ref="passForm"
      label-width="100px"
      class="custom-form"
    >
      <el-form-item label="旧密码" prop="currentPass" style="margin-top: 10px">
        <el-input
          type="password"
          v-model="passForm.currentPass"
          autocomplete="off"
          placeholder="请输入旧密码"
        ></el-input>
      </el-form-item>
      <el-form-item label="新密码" prop="password">
        <el-input
          type="password"
          v-model="passForm.password"
          autocomplete="off"
          placeholder="请输入新密码"
        ></el-input>
      </el-form-item>
      <el-form-item label="确认密码" prop="checkPass">
        <el-input
          type="password"
          v-model="passForm.checkPass"
          autocomplete="off"
          placeholder="请确认新密码"
        ></el-input>
      </el-form-item>
      <el-form-item class="form-actions">
        <el-button
          type="primary"
          @click="submitForm('passForm')"
          style="margin-right: 0px"
        >
          <i class="el-icon-upload"></i> 提交
        </el-button>
        <el-button @click="resetForm('passForm')" style="margin-right: 80px"
          >重置</el-button
        >
      </el-form-item>
    </el-form>
  </div>
</template>

<style scoped>
.form-container {
  text-align: center;
  margin: 20px;
}

.greeting {
  color: #333;
  font-size: 24px;
  margin-bottom: 20px;
}

.custom-form {
  max-width: 400px;
  margin: 0 auto;
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 8px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

.el-input {
  width: 100%;
}
.form-actions {
  display: flex;
  justify-content: center; /* Center-align the content horizontally */
  align-items: center; /* Center-align the content vertically */
  margin-top: 30px;
}
</style>

<script>
export default {
  name: "Login",
  data() {
    var validatePass = (rule, value, callback) => {
      const regExp = /^[A-Za-z0-9]+$/; // 仅包含字母和数字的正则表达式
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

      if (value === "") {
        callback(new Error("请再次输入密码"));
      } else if (!regExp.test(value)) {
        callback(new Error("密码只能包含数字和字母"));
      } else if (value.length < 6 || value.length > 12) {
        callback(new Error("长度在 6 到 12 个字符"));
      } else if (
        unsafeKeywords.some((keyword) => value.toUpperCase().includes(keyword))
      ) {
        callback(new Error("密码包含不安全的关键词"));
      } else if (value !== this.passForm.password) {
        callback(new Error("两次输入密码不一致!"));
      } else {
        callback();
      }
    };

    return {
      userInfo: {},
      passForm: {
        password: "",
        checkPass: "",
        currentPass: "",
      },
      rules: {
        password: [
          { required: true, message: "请输入新密码", trigger: "blur" },
          { min: 6, max: 12, message: "长度在 6 到 12 个字符", trigger: "blur" },
        ],
        checkPass: [{ required: true, validator: validatePass, trigger: "blur" }],
        currentPass: [{ required: true, message: "请输入当前密码", trigger: "blur" }],
      },
    };
  },
  created() {
    this.getUserInfo();
  },
  methods: {
    getUserInfo() {
      this.$axios.get("/sys/userInfo").then((res) => {
        this.userInfo = res.data.data;
      });
    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          const _this = this;
          this.$axios.post("/sys/user/updatePass", this.passForm).then((res) => {
            _this.$alert(res.data.msg, "提示", {
              confirmButtonText: "确定",
              callback: (action) => {
                this.$refs[formName].resetFields();
              },
            });
          });
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
  },
};
</script>
