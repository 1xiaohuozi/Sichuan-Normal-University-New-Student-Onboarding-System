<template>
  <el-container style="height: 100vh">
    <el-aside :style="{ width: '200px', flexDirection: 'column' }">
      <SideMenu></SideMenu>
    </el-aside>
    <el-container>
      <el-header>
        <img src="../assets/636652789940405402.png" alt="" />
        <!-- <strong class="header-title">迎新服务管理系统</strong> -->
        <div class="header-avatar">
          <el-avatar size="medium" :src="userInfo.avatar"></el-avatar>
          <el-dropdown trigger="hover">
            <span class="el-dropdown-link">
              {{ userInfo.name }}
              <i class="el-icon-arrow-down el-icon--right"></i>
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item>
                <router-link :to="{ name: 'UserCenter' }">修改密码</router-link>
              </el-dropdown-item>
              <el-dropdown-item @click.native="logout">退出</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </el-header>
      <el-main>
        <Tabs></Tabs>
        <div class="content-container">
          <router-view />
        </div>
      </el-main>
    </el-container>
  </el-container>
</template>

<style scoped>
.header-title {
  color: #fff;
  font-size: 20px;
  margin-left: 20px;
}

.header-avatar {
  float: right;
  display: flex;
  align-items: center;
  margin-right: 20px;
}

.el-dropdown-link {
  cursor: pointer;
  color: #fff;
}

.el-header {
  background-color: #276e51;
  /* #17b3a3 顶部导航栏颜色*/
  color: #fff;
  text-align: left;
  padding: 0;
  line-height: 60px;
  display: flex;
  justify-content: space-between;
}

.content-container {
  margin: 20px;
}

a {
  text-decoration: none;
}
</style>

<script>
import SideMenu from "./inc/SideMenu";
import Tabs from "./inc/Tabs";

export default {
  name: "Home",
  components: {
    SideMenu,
    Tabs,
  },
  data() {
    return {
      userInfo: {
        id: "",
        username: "",
        avatar: "",
      },
    };
  },
  created() {
    this.getUserInfo();
  },
  methods: {
    getUserInfo() {
      this.$axios.get("/sys/userInfo").then((res) => {
        this.userInfo = res.data.data;
      });
    },

    logout() {
      this.$axios.post("/logout").then((res) => {
        // 清除本地存储和会话存储
        localStorage.clear();
        sessionStorage.clear();

        // 重置 Vuex 状态
        this.$store.commit("resetState");

        // 跳转到登录页面
        this.$router.push("/login");
      });
    },
  },
};
</script>
