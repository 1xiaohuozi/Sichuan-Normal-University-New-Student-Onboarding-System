import Vue from 'vue'
import VueRouter from 'vue-router'


Vue.use(VueRouter)
const Home = () => import('./views/Home.vue');//路由懒加载

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home,
    children: [
      {
        path: '/index',
        name: 'Index',
        meta: {
          title: '首页'
        },
        component: () => import('@/views/index.vue')//组件懒加载

      },
      {
        path: '/userCenter',
        name: 'UserCenter',
        meta: {
          title: '修改密码'
        },
        component: () => import('@/views/UserCenter.vue')
      },
      {
        path: '/sys/users',
        name: 'SysUser',
        component: () => import('@/views/sys/User.vue')
      },
      {
        path: '/sys/roles',
        name: 'SysRole',
        component: () => import('@/views/sys/Role.vue')
      },
      {
        path: '/sys/menus',
        name: 'SysMenu',
        component: () => import('@/views/sys/Menu.vue')
      },
      {
        path: '/sys/report',
        name: 'SysReport',
        component: () => import('@/views/sys/Report.vue')
      }
    ]
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue')
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})