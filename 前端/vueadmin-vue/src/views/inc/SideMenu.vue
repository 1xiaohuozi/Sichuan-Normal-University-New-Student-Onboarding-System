<template>
  <el-menu
    :default-active="this.$store.state.menus.editableTabsValue"
    class="el-menu-vertical-demo"
    background-color="#39454C"
    text-color="#fff"
    active-text-color="#ffd04b"
  >
    <router-link to="/index">
      <el-menu-item index="Index" @click="selectMenu({ name: 'Index', title: '首页' })">
        <template slot="title">
          <i class="el-icon-s-home"></i>
          <span slot="title">迎新流程</span>
        </template>
      </el-menu-item>
    </router-link>
    <el-submenu :index="menu.name" v-for="menu in menuList">
      <template slot="title">
        <i :class="menu.icon"></i>
        <span>{{ menu.title }}</span>
      </template>
      <el-menu-item-group>
        <router-link :to="item.path" v-for="item in menu.children">
          <el-menu-item :index="item.name" @click="selectMenu(item)">
            <template>
              <i :class="item.icon"></i>
              <span slot="title">{{ item.title }}</span>
            </template>
          </el-menu-item>
        </router-link>
      </el-menu-item-group>
    </el-submenu>
    <router-link to="/userCenter">
      <el-menu-item
        index="UserCenter"
        @click="selectMenu({ name: 'UserCenter', title: '修改密码' })"
      >
        <template slot="title">
          <i class="el-icon-edit"></i>
          <span slot="title">修改密码</span>
        </template>
      </el-menu-item>
    </router-link>
  </el-menu>
</template>
<style>
.header-avatar {
  float: right;
  width: 210px;
  display: flex;
  justify-content: space-around;
  align-items: center;
}
body {
  margin: 0;
  padding: 0;
  height: 100%;
}
.el-aside {
  background-color: #39454c; /* 更改侧边栏背景颜色 */
  color: #fff;
}
.el-dropdown-link {
  cursor: pointer;
  color: #409eff;
}
.el-container {
  padding: 0;
  margin: 0;
  height: 100%;
}
.el-header {
  background-color: #0bd18f;
  color: #fff; /* 更改头部文本颜色 */
  text-align: center;
  line-height: 60px;
}
</style>

<script>
export default {
  name: "SideMenu",
  data() {
    return {};
  },
  computed: {
    menuList: {
      get() {
        return this.$store.state.menus.menuList;
      },
    },
  },
  methods: {
    selectMenu(item) {
      this.$store.commit("addTab", item);
    },
  },
};
</script>
