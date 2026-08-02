<template>
  <div class="resume">
    <!-- 头部信息 -->
    <div class="header">
      <el-avatar :size="120" :src="userInfo.avatar"></el-avatar>
      <div class="info">
        <h2>{{ userInfo.name }}</h2>
        <p>学院：{{ userInfo.institute }} | 学号：{{ userInfo.username }}</p>
      </div>
    </div>
    <div class="section">
      <h3>信息确认状态</h3>
      <p :style="{ color: userInfo.state === 1 ? 'green' : 'red', fontWeight: 'bold' }">
        {{
          userInfo.state === 1
            ? "已成功确认信息无误"
            : "未确认信息无误，请前往功能中心确认信息"
        }}
      </p>
    </div>
    <!-- 报到状态 -->
    <div class="section">
      <h3>报到状态</h3>
      <p :style="{ color: reportStatus.code === 200 ? 'green' : 'red' }">
        <strong>{{
          reportStatus.data?.arrivalTime
            ? "成功报到"
            : "未报到，请前往功能中心填写报道信息"
        }}</strong>
      </p>
      <el-row :gutter="20" v-if="reportStatus.data?.arrivalTime">
        <el-col :span="12">
          <p><strong>到达时间:</strong> {{ reportStatus.data.arrivalTime }}</p>
          <p><strong>随行人数:</strong> {{ reportStatus.data.members }}</p>
        </el-col>
        <el-col :span="12">
          <p><strong>交通工具:</strong> {{ reportStatus.data.transportation }}</p>
          <p><strong>车牌号:</strong> {{ reportStatus.data.notes }}</p>
        </el-col>
      </el-row>
    </div>

    <!-- 缴费状态 -->
    <div class="section">
      <h3>缴费状态</h3>
      <p :style="{ fontWeight: 'bold', color: getStatusColor() }">
        <strong>{{ getPaymentStatus() }}</strong>
      </p>
      <div class="image-container">
        <p><strong>缴费证明:</strong></p>
        <el-image :src="getImageUrl(pay.paymentImage)" fit="contain"></el-image>
      </div>
      <div class="image-container">
        <p><strong>身份证明:</strong></p>
        <el-image :src="getImageUrl(pay.idCardImage)" fit="contain"></el-image>
      </div>
    </div>
    <!-- 绿色通道状态 -->
    <div class="section">
      <h3>宿舍选择状态</h3>
      <p :style="{ color: getDormiColor() }">
        <strong>{{ getDormiStatus() }}</strong>
      </p>
      <el-row :gutter="20">
        <el-col :span="12">
          <p><strong>校区:</strong> {{ this.dormitory.campus }}</p>
          <p><strong>寝室楼:</strong> {{ this.dormitory.dormitory }}</p>
        </el-col>
        <el-col :span="12">
          <p><strong>地点:</strong> {{ this.dormitory.region }}</p>
          <p><strong>寝室号:</strong> {{ this.dormitory.dormitoryNumber }}</p>
        </el-col>
      </el-row>
    </div>
    <!-- 绿色通道状态 -->
    <div class="section">
      <h3>绿色通道(按需申请)</h3>
      <p :style="{ color: getGreenChannelColor() }">
        <strong>{{ getGreenChannelStatus() }}</strong>
      </p>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      userInfo: {
        id: "",
        avatar: "",
        name: "",
        institute: "",
        username: "",
        speciality: "",
        classNumber: "",
        phone: "",
        email: "",
        city: "",
        sex: "",
        dormitory: "",
        dormitory_number: "",
        state: "",
      },
      reportStatus: {
        code: "",
        data: {
          arrivalTime: "",
          transportation: "",
          members: "",
          notes: "",
        },
      },
      statu: undefined, // 添加缴费状态
      pay: {
        paymentImage: "",
        idCardImage: "",
      },
      statu2: undefined,
      statu3: undefined,
      dormitory: {
        campus: "",
        region: "",
        dormitory: "",
        dormitoryNumber: "",
      },
    };
  },
  created() {
    this.getUserInfo().then(() => {
      this.fetchStatusFromBackend();
      this.fetchgreenwayStatusFromBackend();
      this.fetchDormiStatusFromBackend();
    });
  },
  methods: {
    getImageUrl(base64Data) {
      return base64Data ? `data:image;base64,${base64Data}` : "";
    },
    getUserInfo() {
      return this.$axios.get("/sys/userDetailInfo").then((res) => {
        this.userInfo = res.data.data;
        this.getReportStatus();
      });
    },
    fetchStatusFromBackend() {
      this.$axios
        .post("/sys/payment/info", this.userInfo.id)
        .then((response) => {
          // 使用可选链来安全地访问可能不存在的属性
          this.statu = response.data?.data?.statu;
          this.pay.paymentImage = response.data?.data?.paymentImage;
          this.pay.idCardImage = response.data?.data?.idCardImage;
        })
        .catch((error) => {
          console.error("获取状态码时发生错误:", error);
        });
    },

    fetchgreenwayStatusFromBackend() {
      this.$axios
        .post("/sys/channel/info", this.userInfo.id)
        .then((response) => {
          this.statu2 = response.data.data.statu;
        })
        .catch((error) => {
          console.error("获取状态码时发生错误:", error);
        });
    },
    fetchDormiStatusFromBackend() {
      this.$axios
        .post("/sys/dormitory/info", this.userInfo.id)
        .then((response) => {
          this.statu3 = response.data.data.statu;
          this.dormitory.campus = response.data.data.campus;
          this.dormitory.region = response.data.data.region;
          this.dormitory.dormitory = response.data.data.dormitory;
          this.dormitory.dormitoryNumber = response.data.data.dormitoryNumber;
        })
        .catch((error) => {
          console.error("获取状态码时发生错误:", error);
        });
    },
    getReportStatus() {
      this.$axios
        .post("/sys/report/status", this.userInfo.id)
        .then((res) => {
          if (res.data.code === 200) {
            this.reportStatus = res.data;
          } else {
            console.error("Failed to get report status");
          }
        })
        .catch((error) => {
          console.error("Error in request:", error);
        });
    },
    getPaymentStatus() {
      if (this.statu === undefined) {
        return "未缴费，请前往 功能中心 填写缴费信息";
      } else if (this.statu === 1) {
        return "缴费审核中";
      } else if (this.statu === 2) {
        return "缴费成功";
      } else if (this.statu === 3) {
        return "审核不通过，请前往 功能中心 重新上传";
      } else {
        return "";
      }
    },
    getStatusColor() {
      if (this.statu === undefined) {
        return "red"; // 未缴费红色
      } else if (this.statu === 1) {
        return "#e5e619"; // 审核中黄色
      } else if (this.statu === 2) {
        return "green"; // 缴费成功绿色
      } else if (this.statu === 3) {
        return "red"; // 审核不通过红色
      } else {
        return ""; // 根据实际情况处理其他状态
      }
    },
    getGreenChannelStatus() {
      if (this.statu2 === undefined) {
        return "未申请绿色通道";
      } else if (this.statu2 === 1) {
        return "绿色通道审核中";
      } else if (this.statu2 === 2) {
        return "绿色通道已通过";
      } else if (this.statu2 === 3) {
        return "绿色通道申请不通过，请重新上传";
      } else {
        return ""; // 根据实际情况处理其他状态
      }
    },

    getGreenChannelColor() {
      if (this.statu2 === undefined) {
        return "red"; // 未申请绿色通道红色
      } else if (this.statu2 === 1) {
        return "#e5e619"; // 绿色通道审核中黄色
      } else if (this.statu2 === 2) {
        return "green"; // 绿色通道已通过绿色
      } else if (this.statu2 === 3) {
        return "red"; // 绿色通道申请不通过红色
      } else {
        return ""; // 根据实际情况处理其他状态
      }
    },
    getDormiStatus() {
      if (this.statu3 === undefined) {
        return "未选择宿舍";
      } else if (this.statu3 === 1) {
        return "已选择宿舍";
      } else {
        return ""; // 根据实际情况处理其他状态
      }
    },

    getDormiColor() {
      if (this.statu3 === undefined) {
        return "red"; // 未申请绿色通道红色
      } else if (this.statu3 === 1) {
        return "green"; // 绿色通道已通过绿色
      } else {
        return ""; // 根据实际情况处理其他状态
      }
    },
  },
};
</script>

<style>
.image-container {
  width: 30%; /* 设置图片容器的宽度为48% */
  margin-right: 20%; /* 设置图片容器之间的右边距为2% */
  display: inline-block; /* 将图片容器设为行内块级元素，使其左右排列 */
  vertical-align: top; /* 使图片容器顶部对齐 */
}

.resume {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
  font-family: "Arial", sans-serif;
  background-color: #f8f8f8;
}

.header {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
  background-color: #fff;
  padding: 15px;
  border-radius: 8px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

.info {
  margin-left: 20px;
}

.section {
  margin-top: 15px;
  background-color: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

.section h3 {
  border-bottom: 2px solid #333;
  padding-bottom: 5px;
  margin-bottom: 15px;
}

.section p {
  margin: 10px 0;
}

.el-avatar {
  border: 2px solid #333;
}
</style>
