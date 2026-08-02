<template>
  <div id="container1">
    <div id="ReportNow">
      <div id="reportntc">
        <p id="notice">
          各位{{
            nianfen
          }}级新生请详细阅读录取通知书上的学院和校区信息，按时到校完成入学报道手续。
        </p>
      </div>
      <div id="container">
        <div class="campus" id="shizishan">
          <h4>狮子山校区</h4>
          <viewer :images="arrayList">
            <img
              v-for="(src, index) in arrayList"
              :src="require('../../../assets/shizishan.jpg')"
              :key="index"
              :style="{ width: '250px', height: '250px' }"
              class="pro-img"
            />
          </viewer>
          <p class="t">校区地址：成都市锦江区静安路5号。</p>
          <p class="t">{{ szsmsg }}</p>
          <el-button type="success" @click="openMapDialog('shizishan')"
            >查看狮子山校区地图</el-button
          >
        </div>
        <div class="campus" id="chenglong">
          <h4>成龙校区</h4>
          <viewer :images="arrayList">
            <img
              v-for="(src, index) in arrayList"
              :src="require('../../../assets/chenglong.jpg')"
              :key="index"
              :style="{ width: '250px', height: '250px' }"
              class="pro-img"
            />
          </viewer>
          <p class="t">校区地址：成都市龙泉驿区成龙大道1819号。</p>
          <p class="t">{{ clmsg }}</p>
          <el-button type="success" @click="openMapDialog('chenglong')"
            >查看成龙校区地图</el-button
          >
        </div>
      </div>
    </div>

    <el-dialog :visible.sync="mapDialogVisible" title="校区地图" width="55%">
      <div class="map-container">
        <div id="baiduMap" :style="{ height: mapHeight + 'px' }"></div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { Button, Dialog, Slider } from "element-ui";
import "element-ui/lib/theme-chalk/index.css";

export default {
  components: {
    "el-button": Button,
    "el-dialog": Dialog,
    "el-slider": Slider,
  },
  data() {
    return {
      arrayList: ["../../../assets/sicnu.jpg"],
      nianfen: "2024",
      szsmsg:
        "疫情原因，学生的陪同人员不能进入校园。学生需带好相关证件， 测量体温后凭通知书一书一入。学生从校门进来后，先根据个人信息找到对应宿舍楼，拿到宿舍钥匙放置好行李。携带身份证和录取通知书到一教楼下找到对应的学院报道点，在网上缴清学费的学生可以直接排队报道领取一卡通和相关资料。绿色通道的学生需带好相关凭证，到绿色通道排队拿到报名许可，再到学院报道点注册报道。再到学院报道点注册报道再到学院报道点注册报道再到学院报道点注册报道再到学院报",
      clmsg:
        "疫情原因，成龙校区西岑门暂不予开放。陪同家属不能进入校园。学生需携带好健康码，15日所在地登记表，以及录取通知书并测量体温后才能进入校园。学生从校门进来后，先根据个人信息找到对应宿舍楼，拿到宿舍钥匙放置好行李。携带身份证和录取通知书到训练馆找到对应的学院报道点，在网上缴清学费的学生可以直接排队报道领取一卡通和相关资料。绿色通道的学生需带好相关凭证，到绿色通道排队完成相关手续，再到学院报道点注册报道。",
      mapDialogVisible: false,
      selectedMapUrl: "", // 存储选定校区的地图路径
      mapHeight: 500, // 初始地图高度
      markers: [], // 用于存储标记点
    };
  },
  methods: {
    openMapDialog(campus) {
      // 设置对应校区地图弹窗可见
      this.mapDialogVisible = true;

      // 初始化百度地图
      setTimeout(() => {
        this.initBaiduMap(campus);
      }, 500);
    },

    initBaiduMap(campus) {
      // 创建地图实例
      const map = new window.BMap.Map("baiduMap");

      // 启用滚轮缩放
      map.enableScrollWheelZoom(true);

      // 设置地图中心点和缩放级别
      let centerPoint, zoomLevel;

      if (campus === "shizishan") {
        centerPoint = new BMap.Point(104.129484, 30.615858);
        zoomLevel = 17;
        this.addMarker(map, centerPoint, "狮子山校区中心");

        // 宿舍区域标记点
        const dormitoryPoint = new BMap.Point(104.132386, 30.615555);
        this.addMarker(map, dormitoryPoint, "狮子山校区宿舍区域");

        // 报道点标记点
        const registrationPoint = new BMap.Point(104.127733, 30.61484);
        this.addMarker(map, registrationPoint, "狮子山校区报道点");
      } else if (campus === "chenglong") {
        centerPoint = new BMap.Point(104.208306, 30.570984);
        zoomLevel = 17;
        this.addMarker(map, centerPoint, "成龙校区中心");

        // 宿舍区域标记点
        const dormitoryPoint = new BMap.Point(104.203993, 30.571741);
        this.addMarker(map, dormitoryPoint, "成龙校区宿舍区域");

        // 报道点标记点
        const registrationPoint = new BMap.Point(104.208751, 30.572722);
        this.addMarker(map, registrationPoint, "成龙校区报道点");
      }

      map.centerAndZoom(centerPoint, zoomLevel);
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
};
</script>

<style scoped>
.map-container {
  display: flex;
  justify-content: center;
}

#baiduMap {
  width: 100%;
}
.el-button {
  margin-top: 10px; /* 调整按钮的上外边距 */
}
#container1 {
  display: flex;
  justify-content: center;
}

#ReportNow {
  position: relative;
  max-width: 920px;
  margin: 20px auto;
}

#reportntc {
  width: 100%;
  height: 50px;
  background-color: rgba(39, 110, 81, 0.8);
  display: flex;
  justify-content: center;
  align-items: center;
}

#container {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
  margin-top: 20px;
}

.campus {
  width: 48%;
  margin-bottom: 20px;
  flex-direction: column; /* 确保子元素垂直排列 */
}

#shizishan h4,
#chenglong h4 {
  font-weight: bolder;
  color: #333;
  margin-bottom: 10px;
  flex-direction: column; /* 确保子元素垂直排列 */
}

.pro-img {
  width: 100%;
  height: auto;
  border-radius: 10px;
  margin-bottom: 10px;
}

.t {
  color: #797979;
  line-height: 1.5;
  margin-bottom: 10px;
}

.map-container {
  display: flex;
  justify-content: center;
}

#notice {
  font-size: 16px;
  color: #ffffff;
  margin-top: 10px;
  margin-bottom: 20px;
}
</style>
