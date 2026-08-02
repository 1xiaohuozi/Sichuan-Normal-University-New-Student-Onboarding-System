<template>
  <div>
    <el-dialog :visible.sync="mapDialogVisible" title="校区地图" width="55%">
      <div class="map-container">
        <div id="baiduMap" :style="{ height: mapHeight + 'px' }"></div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  data() {
    return {
      mapDialogVisible: false,
      mapHeight: 500,
    };
  },
  props: {
    campus: String,
  },
  watch: {
    campus: {
      immediate: true,
      handler(newVal) {
        if (newVal) {
          this.openMapDialog(newVal);
        }
      },
    },
  },
  methods: {
    openMapDialog(campus) {
      this.mapDialogVisible = true;
      setTimeout(() => {
        this.initBaiduMap(campus);
      }, 500);
    },
    initBaiduMap(campus) {
      const map = new window.BMap.Map("baiduMap");
      map.enableScrollWheelZoom(true);

      let centerPoint, zoomLevel;

      if (campus === "shizishan") {
        centerPoint = new BMap.Point(104.129484, 30.615858);
        zoomLevel = 17;
        this.addMarker(map, centerPoint, "狮子山校区中心");
        const dormitoryPoint = new BMap.Point(104.132386, 30.615555);
        this.addMarker(map, dormitoryPoint, "狮子山校区宿舍区域");
        const registrationPoint = new BMap.Point(104.127733, 30.61484);
        this.addMarker(map, registrationPoint, "狮子山校区报道点");
      } else if (campus === "chenglong") {
        centerPoint = new BMap.Point(104.208306, 30.570984);
        zoomLevel = 17;
        this.addMarker(map, centerPoint, "成龙校区中心");
        const dormitoryPoint = new BMap.Point(104.203993, 30.571741);
        this.addMarker(map, dormitoryPoint, "成龙校区宿舍区域");
        const registrationPoint = new BMap.Point(104.208751, 30.572722);
        this.addMarker(map, registrationPoint, "成龙校区报道点");
      }

      map.centerAndZoom(centerPoint, zoomLevel);
    },
    addMarker(map, point, label) {
      const marker = new BMap.Marker(point);
      map.addOverlay(marker);

      const infoWindow = new BMap.InfoWindow(label);
      marker.addEventListener("click", function () {
        this.openInfoWindow(infoWindow);
      });
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
</style>
