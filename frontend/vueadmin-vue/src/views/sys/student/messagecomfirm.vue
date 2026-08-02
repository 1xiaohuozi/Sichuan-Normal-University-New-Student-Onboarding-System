<template>
  <div class="resume">
    <div class="header">
      <el-avatar class="el-avatar" :size="120" :src="userData.avatar"></el-avatar>
      <div class="info">
        <h2>{{ userData.name }}</h2>
        <p>{{ userData.sex }} | {{ userData.institute }}</p>
      </div>
    </div>
    <div class="contact">
      <h3>基本信息</h3>
      <el-row :gutter="20">
        <el-col :span="12">
          <p><strong>学号：</strong> {{ userData.username }}</p>
          <p><strong>邮箱：</strong> {{ userData.email }}</p>
          <p><strong>城市：</strong> {{ userData.city }}</p>
          <p><strong>出生日期：</strong> {{ userData.birth }}</p>
        </el-col>
        <el-col :span="12">
          <p><strong>电话：</strong> {{ userData.phone }}</p>
          <p><strong>宿舍楼：</strong> {{ userData.dormitory }}</p>
          <p><strong>宿舍号：</strong> {{ userData.dormitoryNumber }}</p>
        </el-col>
      </el-row>
      <h3>学籍信息</h3>
      <el-row :gutter="20">
        <el-col :span="12">
          <p><strong>学院：</strong> {{ userData.institute }}</p>
          <p><strong>班级：</strong> {{ userData.classNumber }}</p>
          <p><strong>专业：</strong> {{ userData.speciality }}</p>
          <p><strong>民族：</strong> {{ userData.nation }}</p>
        </el-col>
        <el-col :span="12">
          <p><strong>身份证号：</strong> {{ userData.idCard }}</p>
          <p><strong>学历：</strong> {{ userData.academic }}</p>
          <p><strong>年级：</strong> {{ userData.education }}</p>
          <p><strong>政治面貌：</strong> {{ userData.politic }}</p>
        </el-col>
      </el-row>
    </div>

    <!-- 添加一个弹窗 -->
    <el-dialog title="修改信息" :visible.sync="dialogVisible" width="30%">
      <el-form :model="editData" :rules="editRules" ref="editForm" label-width="80px">
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="editData.email"></el-input>
        </el-form-item>
        <el-form-item label="出生日期" prop="birth">
          <el-date-picker v-model="editData.birth" type="date"></el-date-picker>
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input v-model="editData.phone"></el-input>
        </el-form-item>
        <el-form-item label="政治面貌" prop="politic">
          <el-input v-model="editData.politic"></el-input>
        </el-form-item>
        <el-form-item label="民族" prop="nation">
          <el-input v-model="editData.nation"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmEdit">确认</el-button>
      </div>
    </el-dialog>
    <div class="actions">
      <el-button @click="openEditDialog" type="danger">修改信息</el-button>
      <el-button @click="confirmInfo" type="primary">确认信息</el-button>
    </div>
  </div>
</template>

<script>
import moment from "moment";

export default {
  data() {
    return {
      userData: {},
      dialogVisible: false, // 控制弹窗显示隐藏
      editData: {}, // 存储编辑时的数据
      editRules: {
        // 编辑表单的验证规则
        email: [
          { required: true, message: "请输入邮箱", trigger: "blur" },
          { type: "email", message: "请输入正确的邮箱地址", trigger: ["blur", "change"] },
        ],
        birth: [{ required: true, message: "请选择出生日期", trigger: "change" }],
        phone: [{ required: true, message: "请输入电话号码", trigger: "blur" }],
        politic: [{ required: true, message: "请输入政治面貌", trigger: "blur" }],
        nation: [{ required: true, message: "请输入民族", trigger: "blur" }],
      },
    };
  },
  mounted() {
    this.getUserInfo();
  },
  methods: {
    getUserInfo() {
      return this.$axios.get("/sys/userDetailInfo").then((res) => {
        this.userData = res.data.data;
      });
    },
    reportIncorrectInfo() {
      this.$axios
        .post("/sys/reportIncorrectInfo", this.userData)
        .then(() => {
          this.$message.success("信息报告成功！");
        })
        .catch((error) => {
          console.error("Error reporting incorrect info:", error);
          this.$message.error("信息报告失败，请重试。");
        });
    },
    confirmInfo() {
      this.$confirm("确认信息无误？", "确认", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          // 用户点击确定后执行确认信息逻辑
          return this.$axios.post("/sys/user/status", this.userData.id);
        })
        .then(() => {
          this.$message.success("信息确认成功！");
        })
        .catch((error) => {
          console.error("Error confirming info:", error);
          this.$message.error("信息确认失败，请重试。");
        });
    },

    openEditDialog() {
      this.dialogVisible = true;
      // 将要编辑的数据初始化为用户当前的数据
      this.editData = { ...this.userData };
    },
    confirmEdit() {
      // 调用后端方法来确认编辑信息
      // 请替换成你的实际后端方法调用
      this.editData.birth = moment(this.editData.birth).format("YYYY-MM-DD HH:mm:ss");
      this.$axios
        .post("/sys/user/update", this.editData)
        .then(() => {
          this.dialogVisible = false;
          this.$message.success("信息修改成功！");
          // 刷新用户信息
          this.getUserInfo();
        })
        .catch((error) => {
          console.error("Error updating info:", error);
          this.$message.error("信息修改失败，请重试。");
        });
    },
  },
};
</script>

<style scoped>
.actions {
  margin-top: 20px;
  display: flex;
  justify-content: right; /* 水平居中 */
  align-items: center; /* 垂直居中 */
}

.resume {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
  font-family: "Arial", sans-serif;
}

.header {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.info {
  margin-left: 20px;
}

.contact {
  margin-top: 30px;
}

.contact h3 {
  border-bottom: 2px solid #333;
  padding-bottom: 5px;
  margin-bottom: 15px;
}

.contact p {
  margin: 5px 0;
}

.el-avatar {
  border: 2px solid #333;
}
</style>
