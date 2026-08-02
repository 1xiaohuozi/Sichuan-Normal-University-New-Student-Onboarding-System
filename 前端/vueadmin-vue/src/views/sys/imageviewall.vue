<template>
  <div class="dashboard">
    <div class="background-image"></div>
    <div class="content">
      <div class="filters">
        <label for="college">选择学院：</label>
        <el-select v-model="selectedCollege" @change="handleCollegeChange">
          <el-option label="全部" value="all"></el-option>
          <el-option label="计算机科学学院" value="计算机科学学院"></el-option>
          <el-option label="生命科学学院" value="生命科学学院"></el-option>
        </el-select>

        <label for="classNumber">&nbsp;选择班级：</label>
        <el-select v-model="selectedClassNumber" @change="refreshData">
          <el-option label="全部" value="all"></el-option>
          <el-option label="1班" value="1班"></el-option>
          <el-option label="2班" value="2班"></el-option>
          <el-option label="3班" value="3班"></el-option>
        </el-select>

        <el-button @click="debouncedRefresh" class="el-icon-refresh">刷新</el-button>
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
        <div class="report-button">
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
  </div>
</template>

<style>
.dashboard {
  position: relative;
  height: 100vh;
  overflow: hidden;
}
/*
背景图片样式
*/
.background-image {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-image: url("@/assets/pc-bg.png");
  background-size: cover;
  opacity: 0.5; /* 透明度 */
  z-index: -1;
}

.content {
  padding: 20px;
}

.filters {
  margin-bottom: 20px;
}
/*
选择学院 选择班级字体样式
*/
.filters label {
  margin-right: 10px;
  color: #090909;
}

.filters button {
  margin-left: 10px;
}

.chart-container {
  display: flex;
  justify-content: space-around;
}

.chart {
  background-color: #fff;
  padding: 15px;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  text-align: center;
  opacity: 0.9;
}

.chart h2 {
  color: #333; /* Adjust chart title color for better visibility */
}

.buttons-container {
  display: flex;
  justify-content: space-around;
  margin-top: 30px;
}

.buttons-container .report-button,
.buttons-container .payment-buttons,
.buttons-container .green-channel-buttons {
  display: flex;
  margin-right: 15px;
}

.buttons-container button {
  margin-right: 10px;
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  background-color: #4caf50;
  color: #fff;
}

.report-button {
  margin-left: 10px;
}

/* Additional spacing adjustments */
.buttons-container .report-button button:first-child {
  margin-left: 0px;
}

.buttons-container .green-channel-buttons button:last-child {
  margin-right: 0px;
}
</style>

<script>
import axios from "axios";
import { Chart, registerables } from "chart.js";
import { Select, Option, Button } from "element-ui";
import "element-ui/lib/theme-chalk/index.css";
import XLSX from "xlsx";
import { saveAs } from "file-saver";
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
      selectedCollege: "all",
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
        institute: this.selectedCollege,
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
        institute: this.selectedCollege,
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
        institute: this.selectedCollege,
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
        type: "radar", // 设置图表类型为雷达图
        data: {
          labels: ["审核中", "审核通过", "未申请"],
          datasets: [
            {
              label: "绿色通道申请情况",
              data: this.greenChannelData,
              backgroundColor: "rgba(75, 192, 192, 0.2)", // 填充颜色
              borderColor: "rgba(75, 192, 192, 1)", // 边框颜色
              borderWidth: 2, // 边框宽度
              pointBackgroundColor: "rgba(75, 192, 192, 1)", // 数据点颜色
              pointBorderColor: "#fff", // 数据点边框颜色
              pointRadius: 5, // 数据点半径
            },
          ],
        },
        options: {
          scale: {
            // 自定义雷达图的刻度线
            ticks: {
              beginAtZero: true,
            },
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
      this.$message.success("刷新成功");
    },
    // 使用防抖的刷新方法
    debouncedRefresh: _.debounce(function () {
      this.refreshData();
    }, 500), // 500毫秒的防抖延迟，根据需要调整

    handleCollegeChange() {
      // Add any specific logic when the selected college changes
    },

    // 导出报到信息表格
    async exportReportData() {
      const requestData = {
        institute: this.selectedCollege,
        classNumber: this.selectedClassNumber,
      };

      try {
        const response = await axios.post("/sys/report/exportExcel", requestData, {
          responseType: "blob",
        });

        // 判断学院和班级编号是否为'all'
        const collegePart =
          this.selectedCollege.toLowerCase() === "all" ? "全部" : this.selectedCollege;
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
        institute: this.selectedCollege,
        classNumber: this.selectedClassNumber,
      };

      try {
        axios
          .post("/sys/payment/exportExcel", requestData, {
            responseType: "blob",
          })
          .then(async (response) => {
            // 判断学院和班级编号是否为'all'
            const collegePart =
              this.selectedCollege.toLowerCase() === "all"
                ? "全部"
                : this.selectedCollege;
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
        institute: this.selectedCollege,
        classNumber: this.selectedClassNumber,
      };

      try {
        axios
          .post("/sys/channel/exportExcel", requestData, {
            responseType: "blob",
          })
          .then(async (response) => {
            // 判断学院和班级编号是否为'all'
            const collegePart =
              this.selectedCollege.toLowerCase() === "all"
                ? "全部"
                : this.selectedCollege;
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
