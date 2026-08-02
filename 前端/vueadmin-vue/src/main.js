import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
// vue-json-excel插件来实现简单Excel表格的导出功能

import Element from "element-ui"
import "element-ui/lib/theme-chalk/index.css"

import axios from './axios'
import global from './globalFun'
import GlobalNavbar from './views/inc/GlobalNavbar.vue';// 注册全局导航栏组件
import Map from './views/inc/Map.vue';

import JsonExcel from 'vue-json-excel'
// 将Excel导出器作为vue实例的组件
Vue.component('downloadExcel', JsonExcel)
Vue.component('GlobalNavbar', GlobalNavbar);
Vue.component('Map', Map);
Vue.prototype.$axios = axios //
Vue.config.productionTip = false

// require("./mock.js")

Vue.use(Element)

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
