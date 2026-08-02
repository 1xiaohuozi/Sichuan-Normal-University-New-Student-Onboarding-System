<template>
  <div>
    <GlobalNavbar />
    <div id="pic">
      <el-carousel
        :interval="4000"
        type="card"
        height="400px"
        indicator-position="outside"
      >
        <el-carousel-item v-for="data in picList" :key="data.id">
          <img :src="data.idView" class="image" />
        </el-carousel-item>
      </el-carousel>
    </div>

    <div id="content" class="announcement-container">
      <div class="announcement-header">
        <h2 class="announcement-title">报到须知</h2>
        <span class="more-content">更多</span>
      </div>
      <div class="msg">
        <div class="announcement-item" v-for="(notice, index) in noticeList" :key="index">
          <a href="#" @click.prevent="showAnnouncementDetails(notice)"
            ><!--修改点-->
            <div class="sk_item">
              <img src="../../assets/anoucegif.png" class="sk_icon" />
              <div class="sk_title">{{ notice.title }}</div>
              <div class="sk_more">>></div>
            </div>
          </a>
        </div>
      </div>
    </div>

    <!-- 弹窗组件 -->
    <!-- 弹窗组件 -->
    <!-- <el-dialog :visible.sync="dialogVisible" title="公告详情" width="50%">
      <div class="announcement-details" style="max-height: 500px; overflow-y: auto">
        <div v-html="currentNoticeContent"></div>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="dialogVisible = false" class="confirm-button"
          >确认</el-button
        >
      </span>
    </el-dialog> -->

    <div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</div>
    <div id="foot">
      <div id="site">
        <div>
          狮子山校区：成都市锦江区静安路5号 (邮编:610066)
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          成龙校区：成都市龙泉驿区成龙大道二段1819号 (邮编:610101)
        </div>
        <div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</div>

        <div>© 版权所有 . 四川师范大学 蜀ICP备05026983号</div>
      </div>
    </div>
  </div>
</template>

<script>
import { Dialog } from "element-ui";
import axios from "axios";

export default {
  data() {
    return {
      picList: [
        { id: "0", idView: require("../../assets/login-background.jpg") },
        { id: "1", idView: require("../../assets/sicnu.jpg") },
        { id: "2", idView: require("../../assets/pc-bg.png") },
      ],
      tableData: [],
      dialogVisible: false,
      currentNoticeContent: "",
      noticeList: [],
    };
  },
  mounted() {
    this.fetchData();
  },
  methods: {
    showAnnouncementDetails(notice) {
      console.log(notice.id);
      // 不再打开对话框，而是导航到 AnnouncementDetail 组件
      this.$router.push({
        path: "/announcement",

        query: { id: notice.id },
      });
    },

    formatDate(dateArray) {
      // 辅助方法，将日期数组格式化为字符串
      const [year, month, day, hour, minute, second] = dateArray;
      return `${year}-${month}-${day} ${hour}:${minute}:${second}`;
    },
    fetchData() {
      axios
        .get("/sys/notice/list")
        .then((response) => {
          this.tableData = response.data.data.records;
          this.noticeList = response.data.data.records;
        })
        .catch((error) => {
          console.error("获取数据时出错:", error);
        });
    },
  },
};
</script>

<style scoped>
.confirm-button {
  background-color: #276e51;
  width: 150px; /* 或者其他你想要的宽度 */
  letter-spacing: 2px;
  margin: 0 auto;
}
.announcement-details {
  padding: 20px;
  font-size: 14px;
  line-height: 1.6;
}
.announcement-container {
  padding: 20px;
  background-color: #e5fff2;
  border-radius: 8px;
  width: 60%;
  margin: 0 auto;
}
.image {
  height: 100%;
  width: 100%;
  background-size: 100%;
}
.announcement-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.announcement-title {
  font-size: 18px;
  margin-bottom: 10px;
  color: #333333;
}
.more-content {
  color: #333333;
  font-size: 12px;
  cursor: pointer;
}
.msg {
  width: 100%;
}
.announcement-item {
  margin-bottom: 10px;
}
.sk_item {
  display: flex;
  align-items: center;
  justify-content: space-between; /* 新增这一行 */
}
.sk_icon {
  margin-right: 10px;
  vertical-align: middle;
  width: 20px;
  height: 20px;
}
.sk_title {
  flex: 1;
  font-size: 14px;
  font-weight: bold;
  color: #333333;
  white-space: pre-line;
}
.sk_more {
  font-size: 14px;
  color: #333333;
}
#foot {
  height: 170px;
  width: 100%;
  background-color: #276e51;
  color: white;
  font-size: 13px;
  text-align: center;
}
#foot #site {
  max-width: 800px;
  margin: 0 auto;
  padding-top: 65px;
  padding-left: 5%;
}
#foot #site div {
  display: block;
  margin-bottom: 10px;
  width: 100%;
}
</style>
