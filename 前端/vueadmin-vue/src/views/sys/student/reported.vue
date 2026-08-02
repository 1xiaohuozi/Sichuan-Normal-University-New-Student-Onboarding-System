<template>
  <div id="container1">
    <div id="Reportinfo">
      <div id="reportntc">
        <p id="addr-time">
          迎新报到时间为2024.9.1至2024.9.3日早上9点到晚上7点，地点为训练馆一楼.
        </p>
      </div>
      <div id="container">
        <div id="title">
          <p id="p1">预填学生报到信息</p>
        </div>
        <div id="prewrite">
          <div class="form-group">
            <label for="arrival_time" class="label">预计到校时间：</label>
            <div class="flex-container">
              <el-date-picker
                v-if="flag"
                v-model="arrival_time"
                type="datetime"
                format="yyyy-MM-dd HH:mm"
                placeholder="选择日期"
              ></el-date-picker>
            </div>
          </div>
          <div class="form-group">
            <label for="vehicle" class="label">到校交通方式：</label>
            <select id="vehicle" v-if="flag" v-model="vehicle" class="select">
              <option value="公交">公交</option>
              <option value="私家车">私家车</option>
              <option value="出租车">出租车</option>
              <option value="飞机">飞机</option>
            </select>
          </div>
          <div class="form-group">
            <label for="number" class="label">随行人员数量：</label>
            <select id="number" v-if="flag" v-model="number" class="select">
              <option value="0">0</option>
              <option value="1">1</option>
              <option value="2">2</option>
              <option value="3">3</option>
              <option value="4">4</option>
              <option value="5">5</option>
              <option value="6">6</option>
              <option value="7">7</option>
              <option value="8">8</option>
              <option value="9">9</option>
              <option value="10">10</option>
            </select>
          </div>
          <div class="form-group">
            <label for="other-matters" class="label">车辆信息（车牌号等）：</label>
            <textarea
              id="other-matters"
              v-if="flag"
              v-model="otherMatters"
              class="textarea"
            ></textarea>
          </div>
          <div class="submit-btns">
            <el-button
              type="primary"
              v-if="flag"
              id="submitbtns"
              @click="submit"
              icon="el-icon-circle-check"
            >
              提交
            </el-button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import { Message } from "element-ui";

export default {
  name: "Report",
  data() {
    return {
      flag: true,
      arrival_time: "",
      vehicle: "",
      number: 0,
      otherMatters: "",
      userInfo: {
        id: 1,
        username: "",
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
        console.log(this.userInfo.id);
      });
    },
    submit() {
      if (!this.arrival_time) {
        Message.error("请先选择到校时间");
        return;
      }
      const formattedArrivalTime = new Intl.DateTimeFormat("zh-CN", {
        year: "numeric",
        month: "2-digit",
        day: "2-digit",
        hour: "2-digit",
        minute: "2-digit",
        second: "2-digit",
        timeZone: "Asia/Shanghai",
      }).format(this.arrival_time);

      const reportData = {
        arrivalTime: formattedArrivalTime,
        transportation: this.vehicle,
        members: this.number,
        notes: this.otherMatters,
        userId: this.userInfo.id,
        username: this.userInfo.username,
        name: this.userInfo.name,
      };

      axios
        .post("/sys/report/save", reportData)
        .then((response) => {
          console.log(response.data);
          Message.success("报到信息提交成功");
        })
        .catch((error) => {
          console.error(error);
          Message.error("提交失败，请稍后重试");
        });
    },
    fetchReportInfo() {
      axios
        .get("/sys/report/info", {
          params: {
            id: this.userInfo.id,
          },
        })
        .then((response) => {
          const reportInfo = response.data.data;

          // Update the input fields with fetched data
          this.arrival_time = reportInfo.arrival_time;
          this.vehicle = reportInfo.transportation;
          this.number = reportInfo.members;
          this.otherMatters = reportInfo.notes;

          Message.success("报到信息获取成功");
        })
        .catch((error) => {
          console.error(error);
          Message.error("获取报到信息失败，请稍后重试");
        });
    },
  },
};
</script>

<style scoped>
#container1 {
  display: flex;
  justify-content: center;
}
#Reportinfo {
  width: 60%;
  background-color: #f0f4f2;
  padding: 20px;
  box-sizing: border-box;
}

#reportntc {
  background-color: #275c51;
  color: #fff;
  padding: 10px;
}

#container {
  width: 100%;
  background-color: #fff;
  padding: 20px;
  box-sizing: border-box;
  border-radius: 8px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  padding: 30px;
}

#title {
  margin-bottom: 20px;
}

#prewrite {
  display: flex;
  flex-direction: column;
}

.form-group {
  margin-bottom: 20px;
  display: flex;
  align-items: center;
}

.label {
  width: 180px;
  font-size: 16px;
  color: #333;
}

.flex-container {
  display: flex;
  align-items: center;
  width: 70%;
}

.select,
.textarea,
.el-date-picker {
  width: 100%;
  padding: 8px;
  font-size: 14px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.submit-btns {
  text-align: center;
  margin-top: 20px;
}

#submitbtns {
  width: 120px;
  height: 40px;
  background-color: #409eff;
  color: #fff;
  border: none;
  border-radius: 4px;
}
</style>
