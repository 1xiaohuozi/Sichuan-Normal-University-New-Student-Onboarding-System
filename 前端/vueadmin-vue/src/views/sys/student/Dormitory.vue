<template>
  <div id="container1">
    <div class="map-container">
      <div class="map-label">成龙校区地图</div>
      <div id="baiduMapChenglong" class="baidu-map"></div>
      <div class="map-label">狮子山校区地图</div>
      <div id="baiduMapShizishan" class="baidu-map"></div>
    </div>

    <div id="ReportNow" class="report-container">
      <div id="reportntc" class="notice-container">
        <p id="notice" class="notice-text">2024级宿舍在线选择</p>
      </div>

      <el-form :model="form" label-width="80px" class="form-container">
        <el-form-item v-if="step === 1" label="选择校区">
          <div class="flex-container">
            <div
              class="flex-item campus-item"
              :class="{
                'is-active': form.selectedCampus === '狮子山校区',
              }"
              @click="form.selectedCampus = '狮子山校区'"
            >
              狮子山校区
            </div>
            <div
              class="flex-item campus-item"
              :class="{
                'is-active': form.selectedCampus === '成龙校区',
              }"
              @click="form.selectedCampus = '成龙校区'"
            >
              成龙校区
            </div>
          </div>
        </el-form-item>

        <el-form-item v-if="form.selectedCampus" label="选择区域">
          <div class="flex-container">
            <div
              v-for="area in areaOptions"
              :key="area"
              class="flex-item area-item"
              :class="{ 'is-active': form.selectArea === area }"
              @click="form.selectArea = area"
            >
              {{ area }}
            </div>
          </div>
        </el-form-item>

        <el-form-item v-if="form.selectArea" label="选择楼栋">
          <div class="flex-container">
            <div
              v-for="dormitory in dormitoryOptions"
              :key="dormitory"
              class="flex-item dormitory-item"
              :class="{ 'is-active': form.selectedDormitory === dormitory }"
              @click="form.selectedDormitory = dormitory"
            >
              {{ dormitory }}
            </div>
          </div>
        </el-form-item>

        <el-form-item v-if="form.selectedDormitory" label="选择楼层">
          <div class="flex-container">
            <div
              v-for="floor in floors"
              :key="floor"
              class="flex-item floor-item"
              :class="{
                'is-active': form.selectedFloor === floor,
              }"
              @click="form.selectedFloor = floor"
            >
              {{ floor }}
            </div>
          </div>
        </el-form-item>

        <el-form-item v-if="form.selectedFloor" label="选择寝室">
          <div class="flex-container">
            <div
              v-for="dormitoryNumber in dormitoryNumbers"
              :key="dormitoryNumber"
              class="flex-item dormitory-number-item"
              :class="{
                'is-active': form.selectedDormitoryNumber === dormitoryNumber,
              }"
              @click="form.selectedDormitoryNumber = dormitoryNumber"
            >
              {{ dormitoryNumber }}
            </div>
          </div>
        </el-form-item>

        <el-form-item v-if="form.selectedDormitoryNumber" label="选择床位">
          <div class="bed-selection-container">
            <div
              class="floor-plan"
              :style="{
                backgroundImage: 'url(' + require('@/assets/dormitary.png') + ')',
              }"
            >
              <div
                v-for="bedNumber in bedNumbers"
                :key="bedNumber"
                class="bed-number-item"
                :class="{ 'is-active': form.selectedBedNumber === bedNumber }"
                @click="form.selectedBedNumber = bedNumber"
                :style="{
                  top: bedPositions[bedNumber].top,
                  left: bedPositions[bedNumber].left,
                }"
              >
                {{ bedNumber }}
              </div>
            </div>
          </div>
        </el-form-item>

        <el-form-item>
          <el-button
            v-if="form.selectedBedNumber"
            type="primary"
            @click="showConfirmationDialog"
            class="custom-button"
          >
            提交选择
          </el-button>
        </el-form-item>

        <el-form-item v-if="submissionSuccess" class="success-message">
          <p>申请提交成功！</p>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      step: 1,
      form: {
        selectedCampus: "",
        selectArea: "",
        selectedDormitory: "",
        selectedFloor: "",
        selectedDormitoryNumber: "",
        selectedBedNumber: "",
      },
      campusData: {
        狮子山校区: {
          areas: ["狮山宿舍区"],
          dormitories: {
            狮山宿舍区: [
              "1栋",
              "2栋",
              "3栋",
              "4栋",
              "5栋",
              "6栋",
              "7栋",
              "8栋",
              "9栋",
              "10栋",
              "11栋",
            ],
          },
        },
        成龙校区: {
          areas: ["东苑", "西苑"],
          dormitories: {
            东苑: ["1栋", "2栋", "3栋", "4栋", "5栋", "6栋", "7栋", "8栋"],
            西苑: ["1栋", "2栋", "3栋", "4栋", "5栋", "6栋"],
          },
        },
      },
      floors: ["1楼", "2楼", "3楼", "4楼", "5楼"],
      dormitoryData: {
        "1楼": ["101", "102", "103", "104", "105", "106", "107", "108"],
        "2楼": ["201", "202", "203", "204", "205", "206", "207", "208"],
        "3楼": ["301", "302", "303", "304", "305", "306", "307", "308"],
        "4楼": ["401", "402", "403", "404", "405", "406", "407", "408"],
        "5楼": ["501", "502", "503", "504", "505", "506", "507", "508"],
      },
      submissionSuccess: false,
      bedNumbers: ["一号床", "二号床", "四号床", "三号床"],
      bedPositions: {
        一号床: { top: "5%", left: "30%" },
        二号床: { top: "5%", left: "55%" },
        四号床: { top: "65%", left: "28%" },
        三号床: { top: "65%", left: "58%" },
      },
      markers: [], // 新增 markers 数组
    };
  },
  computed: {
    areaOptions() {
      return this.campusData[this.form.selectedCampus]?.areas || [];
    },
    dormitoryOptions() {
      const selectedCampus = this.form.selectedCampus;
      const selectedArea = this.form.selectArea;

      if (selectedCampus && selectedArea) {
        return this.campusData[selectedCampus]?.dormitories[selectedArea] || [];
      }

      return [];
    },

    dormitoryNumbers() {
      return this.dormitoryData[this.form.selectedFloor] || [];
    },
  },
  methods: {
    handleFloorChange() {
      this.form.selectedDormitoryNumber = "";
    },
    resetSelection() {
      this.step = 1;
      this.form = {
        selectedCampus: "",
        selectedDormitory: "",
        selectedFloor: "",
        selectedDormitoryNumber: "",
      };
      this.submissionSuccess = false;
    },
    showConfirmationDialog() {
      const confirmationMessage = `
    <table>
      <tr><td colspan="2" style="font-size: 16px;"><strong>确定提交宿舍选择吗?</strong></td></tr>
      <tr><td colspan="2"><hr></td></tr>
      <tr><td><strong>已选择的信息：</strong></td></tr>
      <tr><td>校区：</td><td>${this.form.selectedCampus}</td></tr>
      <tr><td>宿舍楼：</td><td>${this.form.selectedDormitory}</td></tr>
      <tr><td>楼层：</td><td>${this.form.selectedFloor}</td></tr>
      <tr><td>宿舍号：</td><td>${this.form.selectedDormitoryNumber}</td></tr>
      <tr><td>床位号：</td><td>${this.form.selectedBedNumber}</td></tr>
    </table>
  `;

      this.$confirm(confirmationMessage, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        dangerouslyUseHTMLString: true, // 使用 HTML 字符串
        type: "warning",
      })
        .then(() => {
          // 用户点击确定时触发的操作
          this.submitSelection();
        })
        .catch(() => {
          // 用户点击取消时触发的操作
        });
    },

    submitSelection() {
      if (
        !this.form.selectedCampus ||
        !this.form.selectedDormitory ||
        !this.form.selectedFloor ||
        !this.form.selectedDormitoryNumber
      ) {
        this.$message.error("请确保选择所有信息完整");
        return;
      }

      const requestData = {
        userId: this.userInfo.id,
        campus: this.form.selectedCampus,
        dormitory: this.form.selectedDormitory,
        floor: this.form.selectedFloor,
        dormitoryNumber: this.form.selectedDormitoryNumber,
        bedNumber: this.form.selectedBedNumber,
        statu: "1", // 重置审核状态
      };

      this.$axios
        .post("/sys/dormitory/save", requestData)
        .then((response) => {
          this.submissionSuccess = true;
          this.$message.success("申请提交成功！");
        })
        .catch((error) => {
          console.error(error);
        });
    },
    getUserInfo() {
      return this.$axios.get("/sys/userInfo").then((res) => {
        this.userInfo = res.data.data || {};
      });
    },

    // 加载百度地图 API 脚本
    loadBaiduMapScript(campus, mapContainerId) {
      // 百度地图 API 的回调函数

      const script = document.createElement("script");
      script.type = "text/javascript";
      script.src = `https://api.map.baidu.com/api?v=2.0&ak=vxcg6MGtPOGFOnq12BmuLreXrbI4iWr8&callback=baiduMapScriptCallback`;
      script.onerror = () => {
        console.error("Failed to load Baidu Map API");
      };
      document.head.appendChild(script);
      const map = new window.BMap.Map(mapContainerId);

      // 启用滚轮缩放
      map.enableScrollWheelZoom(true);

      // 设置地图中心点和缩放级别
      let centerPoint, zoomLevel;
      console.log("Campus:", campus);
      console.log("Map Container ID:", mapContainerId);
      if (campus === "shizishan") {
        centerPoint = new window.BMap.Point(104.129709, 30.61721);

        zoomLevel = 17;
        // 添加其他相关的标记点

        // 宿舍区域标记点
        const dormitoryPoint = new BMap.Point(104.132386, 30.615555);
        this.addMarker(map, dormitoryPoint, "狮子山校区宿舍区域");

        // 报道点标记点
      } else if (campus === "chenglong") {
        centerPoint = new window.BMap.Point(104.208275, 30.570736);

        zoomLevel = 17;
        // 添加其他相关的标记点

        // 宿舍区域标记点
        const dormitoryPoint = new BMap.Point(104.203993, 30.571741);
        this.addMarker(map, dormitoryPoint, "成龙校区西苑");
        // 宿舍区域标记点
        const dormitoryPoint2 = new BMap.Point(104.211086, 30.56737);
        this.addMarker(map, dormitoryPoint2, "成龙校区东苑");
        // 报道点标记点
      }

      map.centerAndZoom(centerPoint, zoomLevel);
      console.log("Center Point:", centerPoint);
      console.log("Zoom Level:", zoomLevel);
    },

    addMarker(map, point, label) {
      const marker = new BMap.Marker(point);
      map.addOverlay(marker);

      // 添加标注的信息窗口
      const infoWindow = new BMap.InfoWindow(label);
      marker.addEventListener("click", function () {
        this.openInfoWindow(infoWindow);
      });
      this.markers.push(marker); // 将标注添加到数组，以便后续管理
    },
  },
  created() {
    this.getUserInfo();
  },
  mounted() {
    // 延迟 1 秒初始化百度地图
    setTimeout(() => {
      this.loadBaiduMapScript("shizishan", "baiduMapShizishan");
      this.loadBaiduMapScript("chenglong", "baiduMapChenglong");
    }, 200);
  },
};
</script>

<style scoped>
.map-container {
  display: flex;
  flex-direction: column; /* Display maps in a column */
  align-items: center; /* Center the maps horizontally */
  margin-top: 25px;
  margin-bottom: 100px;
}

.baidu-map {
  width: 600px;
  height: 400px;
  margin-bottom: 50px; /* 两张地图之间的间距*/
}
.bed-number-item {
  position: absolute;
  cursor: pointer;
  padding: 10px;
  border-radius: 5px;
  background-color: #789cc0;
  color: #fff;
}

.bed-selection-container {
  position: relative;
  width: 100%;
  height: 300px; /* 根据你的平面图调整高度 */
  margin-top: 20px;
  border: 1px solid #ccc; /* 添加边框定义平面图区域 */
  overflow: hidden;
}
.bed-number-item.is-active {
  background-color: #c22424; /* 选中时的背景色 */
}

.floor-plan {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-size: cover;
  background-position: center;
}
.flex-container {
  width: 100%;
  display: flex;
  justify-content: center;
}

.flex-item.is-active {
  background-color: #c22424;
  color: #fff;
}

/* 校区 */
.campus-item,
.area-item,
.dormitory-item,
.floor-item,
.dormitory-number-item,
.bed-number-item {
  cursor: pointer;
  margin: 5px;
  padding: 10px;
  border-radius: 10px;
  text-align: center;
}

.campus-item {
  background-color: rgba(39, 110, 81, 0.8);
}

.area-item {
  background-color: rgba(39, 110, 81, 0.8);
}

.dormitory-item {
  background-color: rgba(39, 110, 81, 0.8);
}

.floor-item {
  background-color: rgba(39, 110, 81, 0.8);
}

.dormitory-number-item {
  background-color: rgba(39, 110, 81, 0.8);
}

.bed-number-item {
  background-color: #789cc0;
}

#container1 {
  display: flex;
  justify-content: center;
  align-items: flex-start;
  height: 5vh;
}

.report-container {
  max-width: 600px;
  width: 100%;
  margin: 20px;
  padding: 20px;
  background-color: #fff;
  border-radius: 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

.notice-container {
  height: 50px;
  background-color: rgba(39, 110, 81, 0.8);
  display: flex;
  justify-content: center;
  align-items: center;
  border-top-left-radius: 10px;
  border-top-right-radius: 10px;
}

.notice-text {
  font-size: 16px;
  color: #ffffff;
  margin: 0;
}

.form-container {
  margin-top: 20px;
}

.custom-button {
  width: 100%;
}

.success-message {
  margin-top: 10px;
  color: green;
  text-align: center;
}
.map-label {
  text-align: center;
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 10px;
  color: #333; /* 设置颜色，根据您的设计选择 */
}
</style>
