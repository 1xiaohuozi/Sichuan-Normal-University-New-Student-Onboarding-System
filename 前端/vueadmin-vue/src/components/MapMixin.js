// MapMixin.js
export const MapMixin = {
    data() {
        return {
            mapDialogVisible: false,
            mapHeight: 500,
            markers: [],
        };
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

            let centerPoint, zoomLevel, dormitoryPoint, registrationPoint;

            if (campus === "shizishan") {
                centerPoint = new BMap.Point(104.129484, 30.615858);
                zoomLevel = 17;
                dormitoryPoint = new BMap.Point(104.132386, 30.615555);
                registrationPoint = new BMap.Point(104.127733, 30.61484);
            } else if (campus === "chenglong") {
                centerPoint = new BMap.Point(104.208306, 30.570984);
                zoomLevel = 17;
                dormitoryPoint = new BMap.Point(104.203993, 30.571741);
                registrationPoint = new BMap.Point(104.208751, 30.572722);
            }

            this.addMarker(map, centerPoint, "校区中心");
            this.addMarker(map, dormitoryPoint, "宿舍区域");
            this.addMarker(map, registrationPoint, "报道点");

            map.centerAndZoom(centerPoint, zoomLevel);
        },
        addMarker(map, point, label) {
            const marker = new BMap.Marker(point);
            map.addOverlay(marker);

            const infoWindow = new BMap.InfoWindow(label);
            marker.addEventListener("click", function () {
                this.openInfoWindow(infoWindow);
            });
            this.markers.push(marker);
        },
    },
};
