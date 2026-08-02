<template>
  <div>
    <!-- GlobalNavbar组件可以根据你的项目结构添加 -->
    <GlobalNavbar />

    <!-- 公告内容 -->
    <div class="notice-detail">
      <h1 class="title">{{ notice.title }}</h1>
      <p class="meta">
        作者：{{ notice.author }}，发布时间：{{ formatDate(notice.created) }}
      </p>
      <div class="content" v-html="notice.content"></div>
    </div>

    <!-- 底部信息 -->
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
import axios from "axios";

export default {
  data() {
    return {
      notice: {},
    };
  },
  methods: {
    // 格式化日期
    formatDate(dateArray) {
      // 根据实际情况实现日期格式化
      // 这里简单地将数组转为字符串
      return dateArray.join("-");
    },
  },
  mounted() {
    let id = this.$route.query.id;
    console.log(this.$route.query.id);
    axios
      .post("/sys/notice/info", id, {
        headers: {
          "Content-Type": "application/json",
        },
      })
      .then((response) => {
        this.notice = response.data.data;
      })
      .catch((error) => {
        console.error("获取数据时出错:", error);
      });
  },
};
</script>

<style scoped>
/* 根据实际需要添加样式 */
.notice-detail {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

.notice-detail h1.title {
  font-family: "微软雅黑";
  font-size: 20px;
  text-align: center;
  color: #27624e;
  margin-bottom: 10px;
  position: relative; /* 添加相对定位 */
}

.notice-detail h1.title::after {
  content: "";
  position: absolute;
  left: 0;
  bottom: -50px; /* 调整与标题的距离 */
  width: 100%;
  height: 1px;
  background-color: rgba(39, 98, 78, 0.1); /* 下划线颜色 */
}

.notice-detail .meta {
  font-size: 14px;
  color: #9b9c9c;
  text-align: center;
  margin-bottom: 20px;
}

.notice-detail .content {
  font-family: "宋体";
  font-size: 15px;
  margin-top: 10px; /* 添加标题和内容之间的空间 */
}

/* 其他样式 */
#foot {
  height: 170px;
  width: 100%;
  background-color: #27624e;
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
