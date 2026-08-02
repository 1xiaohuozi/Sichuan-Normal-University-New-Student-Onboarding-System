<template>
  <div>
    <!-- 使用断点系统调整菜单 -->
    <el-menu
      :collapse="isCollapse"
      :default-active="activeMenu"
      mode="horizontal"
      background-color="#276e51"
      text-color="#ffffff"
      class="custom-navbar"
    >
      <div class="navbar-container">
        <div class="navbar-left">
          <img src="@/assets/636652789940405402.png" alt="Logo" class="logo-image" />
        </div>
        <div class="navbar-right">
          <el-menu-item index="main" @click="gotomain">网站首页</el-menu-item>
          <el-menu-item index="about" @click="gotoweb">学校官网</el-menu-item>
          <el-menu-item index="login" @click="gotologin">网上报到</el-menu-item>
          <!-- 在这里添加其他菜单项 -->
        </div>
      </div>
    </el-menu>
    <el-dialog :visible.sync="showConfirmation" title="提示" width="30%">
      <p>是否确认跳转到学校官网？</p>
      <div slot="footer" class="dialog-footer">
        <el-button
          type="primary"
          @click="confirmGotoWeb"
          style="background-color: #276e51"
        >
          确定
        </el-button>
        <el-button @click="cancelGotoWeb">取消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "GlobalNavbar",
  data() {
    return {
      showConfirmation: false,
      // 添加isCollapse和activeMenu属性
      isCollapse: false,
      activeMenu: "main", // 默认选中的菜单项
    };
  },
  methods: {
    gotomain() {
      // 检查当前路由是否已经是 "/main"
      if (this.$route.path === "/main") {
        // 什么都不做，或者根据需要进行处理
        return;
      }

      // 如果不是，导航到 "/main"
      this.$router.push({ path: "/main" }).catch((err) => {
        // 处理导航错误，如果有的话
        if (err.name !== "NavigationDuplicated") {
          throw err;
        }
      });
    },
    gotologin() {
      // 检查当前路由是否已经是 "/login"
      if (this.$route.path === "/login") {
        // 什么都不做，或者根据需要进行处理
        return;
      }

      // 如果不是，导航到 "/login"
      this.$router.push("/login").catch((err) => {
        // 处理导航错误，如果有的话
        if (err.name !== "NavigationDuplicated") {
          throw err;
        }
      });
    },
    gotoweb() {
      this.showConfirmation = true; // 显示确认弹窗
    },
    confirmGotoWeb() {
      // 在这里执行跳转到网页的操作
      window.open("http://www.sicnu.edu.cn/");

      // 关闭确认弹窗
      this.showConfirmation = false;
    },
    cancelGotoWeb() {
      // 关闭确认弹窗
      this.showConfirmation = false;
    },
  },
};
</script>

<style scoped>
.custom-navbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px; /* Add padding for spacing */
}

.navbar-container {
  display: flex;
  justify-content: space-between;
  width: 100%;
}

.navbar-left {
  display: flex;
  align-items: center;
}

.logo-image {
  width: 320px; /* Adjust the width as needed */
  height: 55px;
}

.navbar-right {
  display: flex;
}

.el-menu-item {
  margin-right: 20px; /* Adjust margin to push menu items to the right */
}

@media screen and (max-width: 768px) {
  .custom-navbar {
    flex-direction: column; /* 在小屏幕上将菜单项垂直排列 */
    padding: 10px; /* 调整垂直排列时的上下内边距 */
  }

  .navbar-container {
    width: auto; /* 移除容器的固定宽度 */
    flex-direction: column; /* 将容器内的元素垂直排列 */
    align-items: flex-start; /* 将元素左对齐 */
  }

  .navbar-right {
    margin-top: 10px; /* 添加顶部外边距以与logo分隔 */
  }

  .el-menu-item {
    margin-right: 0; /* 移除右侧边距 */
    margin-bottom: 10px; /* 调整菜单项之间的垂直间距 */
  }
}
</style>
