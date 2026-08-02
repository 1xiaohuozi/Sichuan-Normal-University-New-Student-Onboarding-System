<template>
  <div>
    <div>
      <h2>{{ userInfo.institute }}</h2>
      <label for="classNumber">选择班级：</label>
      <el-select v-model="selectedClassNumber" @change="refreshData">
        <el-option label="全部" value="all"></el-option>
        <el-option label="1班" value="1班"></el-option>
        <el-option label="2班" value="2班"></el-option>
        <el-option label="3班" value="3班"></el-option>
      </el-select>

      <el-button @click="refreshData" class="el-icon-refresh">刷新</el-button>
    </div>

    <div class="chart-container">
      <div class="chart">
        <h2>学生报道情况</h2>
        <canvas ref="pieChart" width="300" height="300"></canvas>
      </div>
      <div class="chart">
        <h2>学生缴费情况</h2>
        <canvas ref="barChart" width="300" height="300"></canvas>
      </div>
      <div class="chart">
        <h2>绿色通道申请情况</h2>
        <canvas ref="greenChannelChart" width="300" height="300"></canvas>
      </div>
    </div>
    <div class="buttons-container">
      <!-- Report buttons -->
      <div class="report-buttons">
        <el-button @click="exportReportData()">未报到表格</el-button>
      </div>

      <!-- Payment buttons -->
      <div class="payment-buttons">
        <el-button @click="exportPaymentData()">未缴费表格</el-button>
      </div>

      <!-- Green channel buttons -->
      <div class="green-channel-buttons">
        <el-button @click="exportGreenChannelData()">未申请表格</el-button>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import { Chart, registerables } from "chart.js";
import { Select, Option, Button } from "element-ui";
import "element-ui/lib/theme-chalk/index.css";
import { saveAs } from "file-saver";
import XLSX from "xlsx";

Chart.register(...registerables);

export default {
  components: {
    ElSelect: Select,
    ElOption: Option,
    ElButton: Button,
  },

  data() {
    return {
      reportData: [],
      paymentData: [],
      greenChannelData: [],
      userInfo: {},
      selectedClassNumber: "all",
      pieChart: null,
      barChart: null,
      greenChannelChart: null,
    };
  },
  mounted() {
    this.fetchData();
  },
  created() {
    this.getUserInfo().then(() => {
      this.fetchData();
    });
  },
  watch: {
    selectedClassNumber: "fetchData",
    selectedCollege: "fetchData",
  },
  methods: {
    getUserInfo() {
      return this.$axios.get("/sys/userDetailInfo").then((res) => {
        this.userInfo = res.data.data;
      });
    },
    fetchData() {
      this.fetchReportData();
      this.fetchPaymentData();
      this.fetchGreenChannelData();
    },
    fetchReportData() {
      const requestData = {
        institute: this.userInfo.institute,
        classNumber: this.selectedClassNumber,
      };
      axios
        .post("/sys/report/count/institute", requestData, {
          headers: {
            "Content-Type": "application/json; charset=utf-8",
          },
        })
        .then((response) => {
          const rawData = response.data.data;
          this.reportData = JSON.parse(rawData);
          this.renderPieChart();
        })
        .catch((error) => {
          console.error("获取报道数据时出错", error);
        });
    },
    fetchPaymentData() {
      const requestData = {
        institute: this.userInfo.institute,
        classNumber: this.selectedClassNumber,
      };
      axios
        .post("/sys/payment/count/institute", requestData, {
          headers: {
            "Content-Type": "application/json; charset=utf-8",
          },
        })
        .then((response) => {
          const rawData = response.data.data;
          this.paymentData = JSON.parse(rawData);
          this.renderBarChart();
        })
        .catch((error) => {
          console.error("获取缴费数据时出错", error);
        });
    },
    fetchGreenChannelData() {
      const requestData = {
        institute: this.userInfo.institute,
        classNumber: this.selectedClassNumber,
      };
      axios
        .post("/sys/channel/count/institute", requestData, {
          headers: {
            "Content-Type": "application/json; charset=utf-8",
          },
        })
        .then((response) => {
          const rawData = response.data.data;
          this.greenChannelData = JSON.parse(rawData);

          this.renderGreenChannelChart();
        })
        .catch((error) => {
          console.error("获取绿色通道数据时出错", error);
        });
    },
    renderPieChart() {
      const ctx = this.$refs.pieChart.getContext("2d");

      if (this.pieChart) {
        this.pieChart.destroy();
      }

      this.pieChart = new Chart(ctx, {
        type: "doughnut",
        data: {
          labels: ["已报道", "未报道"],
          datasets: [
            {
              data: this.reportData,
              backgroundColor: ["#36A2EB", "#FF6384"],
            },
          ],
        },
      });
    },
    renderBarChart() {
      const ctx = this.$refs.barChart.getContext("2d");

      if (this.barChart) {
        this.barChart.destroy();
      }

      this.barChart = new Chart(ctx, {
        type: "doughnut",
        data: {
          labels: ["审核中", "已缴费", "未缴费"],
          datasets: [
            {
              data: this.paymentData,
              backgroundColor: ["#FF6384", "#36A2EB", "#4CAF50"],
            },
          ],
        },
      });
    },
    renderGreenChannelChart() {
      const ctx = this.$refs.greenChannelChart.getContext("2d");

      if (this.greenChannelChart) {
        this.greenChannelChart.destroy();
      }

      this.greenChannelChart = new Chart(ctx, {
        type: "doughnut",
        data: {
          labels: ["审核中", "审核通过", "未申请"],
          datasets: [
            {
              label: "审核中",
              data: this.greenChannelData,
            },
          ],
        },
        options: {
          x: {
            type: "category",
            labels: ["审核中", "审核通过", "未申请"],
          },
          y: {
            beginAtZero: true,
          },
        },
      });
    },
    refreshData() {
      this.fetchData();

      setTimeout(() => {
        this.renderPieChart();
        this.renderBarChart();
        this.renderGreenChannelChart();
      }, 500);
      this.$message.success("刷新成功！");
    },

    // 导出报到信息表格
    // 导出报到信息表格
    async exportReportData() {
      const requestData = {
        institute: this.userInfo.institute,
        classNumber: this.selectedClassNumber,
      };

      try {
        const response = await axios.post("/sys/report/exportExcel", requestData, {
          responseType: "blob",
        });

        // 判断学院和班级编号是否为'all'
        const collegePart = this.userInfo.institute;
        const classNumberPart =
          this.selectedClassNumber.toLowerCase() === "all"
            ? "全部"
            : this.selectedClassNumber;

        // 使用学院和班级编号作为文件名的一部分
        const fileName = `${collegePart}${classNumberPart}未报到名单.xlsx`;

        const blob = new Blob([response.data], {
          type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
        });
        saveAs(blob, fileName);
      } catch (error) {
        console.error("Error exporting Excel:", error);
      }
    },

    // 导出缴费信息表格
    exportPaymentData() {
      const requestData = {
        institute: this.userInfo.institute,
        classNumber: this.selectedClassNumber,
      };

      try {
        axios
          .post("/sys/payment/exportExcel", requestData, {
            responseType: "blob",
          })
          .then(async (response) => {
            // 判断学院和班级编号是否为'all'
            const collegePart = this.userInfo.institute;
            const classNumberPart =
              this.selectedClassNumber.toLowerCase() === "all"
                ? "全部"
                : this.selectedClassNumber;

            // 使用学院和班级编号作为文件名的一部分
            const fileName = `${collegePart}${classNumberPart}未缴费名单.xlsx`;

            const blob = new Blob([response.data], {
              type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
            });

            // 使用 await 等待异步操作完成
            await saveAs(blob, fileName);
          });
      } catch (error) {
        console.error("Error exporting Excel:", error);
      }
    },

    // 导出绿色通道信息表格
    exportGreenChannelData() {
      const requestData = {
        institute: this.userInfo.institute,
        classNumber: this.selectedClassNumber,
      };

      try {
        axios
          .post("/sys/channel/exportExcel", requestData, {
            responseType: "blob",
          })
          .then(async (response) => {
            // 判断学院和班级编号是否为'all'
            const collegePart = this.userInfo.institute;
            const classNumberPart =
              this.selectedClassNumber.toLowerCase() === "all"
                ? "全部"
                : this.selectedClassNumber;

            // 使用学院和班级编号作为文件名的一部分
            const fileName = `${collegePart}${classNumberPart}绿色通道申请名单.xlsx`;

            const blob = new Blob([response.data], {
              type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
            });

            // 使用 await 等待异步操作完成
            await saveAs(blob, fileName);
          });
      } catch (error) {
        console.error("Error exporting Excel:", error);
      }
    },
  },
};
</script>
<style>
.chart-container {
  display: flex;
  justify-content: space-around;
}

.filters {
  margin-bottom: 20px;
}

.filters label {
  margin-right: 10px;
}

.filters button {
  margin-left: 10px;
}

.buttons-container {
  display: flex;
  justify-content: space-around;
  margin-top: 30px; /* Adjust the margin as needed */
}

.buttons-container .report-buttons,
.buttons-container .payment-buttons,
.buttons-container .green-channel-buttons {
  display: flex;
  margin-right: 15px;
}
.buttons-container button {
  margin-right: 10px; /* 按钮之间的右边距，根据需要调整 */
}

/* 额外的间距调整 */
.buttons-container .report-buttons button:first-child {
  margin-left: 55px; /* 报到按钮组中第一个按钮的左边距，根据需要调整 */
}

.buttons-container .green-channel-buttons button:last-child {
  margin-right: 0px; /* 绿色通道按钮组中最后一个按钮的右边距，根据需要调整 */
}
</style>
