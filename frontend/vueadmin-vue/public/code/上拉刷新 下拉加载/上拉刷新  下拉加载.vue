<template>
  <div>
    <!-- 上拉刷新区域 -->
    <div ref="pullup" class="pullup-wrapper">
      <div class="before-trigger">{{ pullupText }}</div>
      <div class="after-trigger">释放加载</div>
      <div class="loading">{{ loadingText }}</div>
    </div>

    <!-- 列表区域 -->
    <div ref="scrollWrapper" class="scroll-wrapper">
      <ul>
        <!-- 列表项 -->
        <li v-for="item in list" :key="item.id">
          <img :src="item.url" alt="Placeholder Image" />
          <p>{{ item.author }}</p>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
import BScroll from "better-scroll"; // 引入 better-scroll 库
import axios from "axios"; // 引入 axios 库，用于发起网络请求

export default {
  data() {
    return {
      list: [], // 存放列表数据的数组
      scroll: null, // BScroll 实例
      page: 1, // 当前页数
      limit: 10, // 每页数据条数
      pullupText: "上拉加载更多", // 上拉加载提示文本
      loadingText: "加载中...", // 加载中提示文本
    };
  },

  mounted() {
    this.initScroll(); // 初始化滚动
    this.loadData(); // 加载初始数据
  },

  methods: {
    // 初始化滚动
    initScroll() {
      this.scroll = new BScroll(this.$refs.scrollWrapper, {
        probeType: 3, // 滚动的实时位置，可用于判断上拉或下拉
        pullUpLoad: true, // 启用上拉加载
      });

      // 监听滚动事件
      this.scroll.on("scroll", (pos) => {
        if (pos.y < this.scroll.maxScrollY - 30) {
          this.pullupText = "释放加载"; // 判断是否达到释放加载的条件
        } else {
          this.pullupText = "上拉加载更多";
        }
      });

      // 监听上拉加载事件
      this.scroll.on("pullingUp", () => {
        this.loadingText = "加载中...";
        this.loadData(); // 上拉加载时执行加载数据的方法
      });
    },

    // 加载数据
    async loadData() {
      try {
        // 发起网络请求获取数据
        const response = await axios.get(
          `https://picsum.photos/v2/list?page=${this.page}&limit=${this.limit}`
        );
        const newData = response.data.map((item) => ({
          id: item.id,
          url: item.download_url,
          author: item.author,
        }));

        // 更新列表数据
        this.list = this.list.concat(newData);

        // 完成上拉加载
        this.scroll.finishPullUp();

        // 刷新滚动
        this.scroll.refresh();

        // 更新页数
        this.page++;

        // 加载完成提示
        this.loadingText = "加载完成";
      } catch (error) {
        console.error("Error loading data:", error);

        // 加载失败提示
        this.loadingText = "加载失败，请重试";
      }
    },
  },
};
</script>

<style scoped>
.pullup-wrapper {
  text-align: center;
  padding: 10px;
  font-size: 14px;
}

.before-trigger,
.after-trigger,
.loading {
  display: none;
}

.loading {
  color: #888;
}

.scroll-wrapper {
  overflow: hidden;
  height: 300px;
}

ul {
  padding: 0;
  margin: 0;
  list-style: none;
}

li {
  padding: 10px;
  border-bottom: 1px solid #eee;
}

img {
  width: 100%;
  max-height: 200px;
  object-fit: cover;
}
</style>
